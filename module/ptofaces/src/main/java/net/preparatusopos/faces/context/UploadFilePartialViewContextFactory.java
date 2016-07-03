package net.preparatusopos.faces.context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

import net.preparatusopos.faces.component.behavior.UploadFileBehavior;
import net.preparatusopos.faces.event.UploadFileBehaviorEvent;

public class UploadFilePartialViewContextFactory
extends PartialViewContextFactory
{
	public static final String PARTIAL_VIEW_ID =
			"net.preparatusopos.faces.uploadFile";
	
	private static final String FACES_PARTIAL_EVENT_PROPERTY_NAME =
			"javax.faces.partial.event";
	private static final String FACES_SOURCE_PROPERTY_NAME =
			"javax.faces.source";
	
	private PartialViewContextFactory wrapped;
	
	public UploadFilePartialViewContextFactory(PartialViewContextFactory
			wrapped)
	{
		this.wrapped = wrapped;
	}
	
	@Override
	public PartialViewContext getPartialViewContext(FacesContext context)
	{
		PartialViewContext wrappedContext = wrapped.getPartialViewContext(
				context);
		
		ExternalContext externalContext = context.getExternalContext();
		Map<String, String> params = externalContext.getRequestParameterMap();
		String eventName = params.get(FACES_PARTIAL_EVENT_PROPERTY_NAME);
		String sourceId = params.get(FACES_SOURCE_PROPERTY_NAME);
		UIComponent component = findComponent(context, sourceId);
		if (eventName != null && component != null)
		{
			UploadFileBehaviorEvent event = getEvent(context, component,
					eventName);
			if (event != null)
				return new UploadFilePartialViewContext(wrappedContext, event);
		}
		return wrappedContext;
	}
	
	private static UIComponent findComponent(FacesContext context,
			String sourceId)
	{
		try
		{
			UIViewRoot root = context.getViewRoot();
			return sourceId == null ? null : root.findComponent(sourceId);
		}
		catch (IllegalArgumentException exception)
		{
			return null;
		}
	}
	
	private static UploadFileBehaviorEvent getEvent(FacesContext context,
			UIComponent component, String eventName)
	{
		try
		{
			Map<String, String> properties = getProperties(context);
			if (properties.isEmpty())
				return null;
			
			UploadFileBehavior behavior = getBehavior(component, eventName);
			int index = Integer.parseInt(properties.get("index"));
			MimeType contentType = new MimeType(properties.get("contentType"));
			long completedSize = Long.parseLong(properties.get(
					"completedSize"));
			long totalSize = Long.parseLong(properties.get("totalSize"));
			
			String refStr = properties.get("ref");
			URI ref = refStr == null ? null : new URI(refStr);
			
			UploadFileBehaviorEvent event = new UploadFileBehaviorEvent(
					component, behavior, index, ref, contentType, completedSize,
					totalSize);
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			
			return event;
		}
		catch (MimeTypeParseException | URISyntaxException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	private static UploadFileBehavior getBehavior(UIComponent component,
			String eventName)
	{
		if (component instanceof UIComponentBase)
		{
			List<UploadFileBehavior> result = new ArrayList<>();
			List<ClientBehavior> behaviors =
					((UIComponentBase) component).getClientBehaviors().get(
					eventName);
			if (behaviors != null)
			{
				for (ClientBehavior behavior : behaviors)
					if (behavior instanceof UploadFileBehavior)
						result.add((UploadFileBehavior) behavior);
			}
			if (result.isEmpty())
				return null;
			if (result.size() > 1)
			{
				StringBuilder msg = new StringBuilder();
				msg.append("More than one upload file behavior for event ");
				msg.append(eventName).append(" and component ");
				msg.append(component.getClientId());
				throw new IllegalArgumentException(msg.toString());
			}
			return result.get(0);
		}
		return null;
	}
	
	private static Map<String, String> getProperties(FacesContext context)
	{
		String prefix = String.format("%s.", PARTIAL_VIEW_ID);
		
		ExternalContext externalContext = context.getExternalContext();
		Map<String, String> params = externalContext.getRequestParameterMap();
		Map<String, String> properties = new HashMap<>();
		for (Entry<String, String> param : params.entrySet())
		{
			String key = param.getKey();
			if (key.startsWith(prefix))
			{
				String name = key.substring(prefix.length());
				properties.put(name, decodeURL(context, param.getValue()));
			}
		}
		return properties;
	}
	
	private static String decodeURL(FacesContext context, String value)
	{
		try
		{
			ExternalContext externalContext = context.getExternalContext();
			return URLDecoder.decode(value,
					externalContext.getRequestCharacterEncoding());
		}
		catch (UnsupportedEncodingException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	private static class UploadFilePartialViewContext
	extends PartialViewContextWrapper
	{
		private PartialViewContext wrapped;
		private UploadFileBehaviorEvent event;
		private PartialResponseWriter writer;
		
		public UploadFilePartialViewContext(PartialViewContext wrapped,
				UploadFileBehaviorEvent event)
		{
			this.wrapped = wrapped;
			this.event = event;
			writer = new EventPartialResponseWriter(
					this.wrapped.getPartialResponseWriter(), event);
		}
		
		@Override
		public PartialViewContext getWrapped()
		{
			return wrapped;
		}
		
		public PartialResponseWriter getPartialResponseWriter()
		{
			return writer;
		}
		
		public void processPartial(PhaseId phaseId)
		{
			wrapped.processPartial(phaseId);
			if (PhaseId.UPDATE_MODEL_VALUES.equals(phaseId))
				event.queue();
		}
	}
	
	private static class EventPartialResponseWriter
	extends PartialResponseWriter
	{
		private UploadFileBehaviorEvent event;
		
		public EventPartialResponseWriter(ResponseWriter writer,
				UploadFileBehaviorEvent event)
		{
			super(writer);
			this.event = event;
		}
		
		@Override
		public void endDocument()
		throws IOException
		{
			Behavior behavior = event.getBehavior();
			if (behavior instanceof UploadFileBehavior)
			{
				Map<String, String> attrs = new HashMap<>();
				attrs.put("id", PARTIAL_VIEW_ID);
				startExtension(attrs);
				startCDATA();
				
				JsonGenerator generator = Json.createGenerator(this);
				((UploadFileBehavior) behavior).generate(generator);
				generator.flush();
				
				endCDATA();
				endExtension();
			}
			super.endDocument();
		}
	}
}

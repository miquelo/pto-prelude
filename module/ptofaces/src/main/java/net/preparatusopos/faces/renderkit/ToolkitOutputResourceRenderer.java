package net.preparatusopos.faces.renderkit;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitOutputResource;
import net.preparatusopos.faces.util.Util;

@ListenerFor(
	systemEventClass=PostAddToViewEvent.class
)
public abstract class ToolkitOutputResourceRenderer
extends Renderer
implements ComponentSystemEventListener
{
	protected ToolkitOutputResourceRenderer()
	{
	}
	
	@Override
	public final boolean getRendersChildren()
	{
		return true;
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOutputResource)
			encodeBegin(context, (ToolkitOutputResource) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOutputResource)
			encodeEnd(context, (ToolkitOutputResource) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void processEvent(ComponentSystemEvent event)
	throws AbortProcessingException
	{
		UIComponent component = event.getComponent();
		if (component instanceof ToolkitOutputResource)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot viewRoot = getViewRoot(component);
			viewRoot.addComponentResource(context, component, "head");
		}
		else
			Util.unsupportedComponent(component);
	}
	
	protected abstract String getRel();
	
	private void encodeBegin(FacesContext context,
			ToolkitOutputResource component)
	throws IOException
	{
		try
		{
			String ref = createResourceRef(context, component);
			
			ResponseWriter writer = context.getResponseWriter();
			writer.startElement("link", null);
			writer.writeAttribute("media", component.getMedia(), null);
			writer.writeAttribute("type", component.getType(), null);
			writer.writeAttribute("rel", getRel(), null);
			writer.writeAttribute("href", ref, null);
			writer.endElement("link");
		}
		catch (FacesException exception)
		{
			context.addMessage(component.getClientId(), new FacesMessage(
					FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
		}
	}
	
	private void encodeEnd(FacesContext context,
			ToolkitOutputResource component)
	throws IOException
	{
	}
	
	private static String createResourceRef(FacesContext context,
			ToolkitOutputResource component)
	{
		ResourceHandler rh = context.getApplication().getResourceHandler();
		String name = component.getName();
		String library = component.getLibrary();
		String type = component.getType();
		Resource resource = rh.createResource(name, library, type);
		if (resource == null)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Unable to find resource ");
			msg.append(library);
			msg.append(", ").append(name);
			throw new FacesException(msg.toString());
		}
		return resource.getRequestPath();
	}
	
	private static UIViewRoot getViewRoot(UIComponent component)
	{
		UIComponent current = component;
		while (current != null)
		{
			if (current instanceof UIViewRoot)
				return (UIViewRoot) current;
			current = current.getParent();
		}
		StringBuilder msg = new StringBuilder();
		msg.append("There is not any UIViewRoot at component tree");
		throw new IllegalStateException(msg.toString());
	}
}
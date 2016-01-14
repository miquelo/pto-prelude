package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitOAuthInit;
import net.preparatusopos.faces.component.ToolkitToggleClass;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Output",
	rendererType="net.preparatusopos.faces.ToolkitToggleClass"
)
@ResourceDependencies({
	@ResourceDependency(
		library="javax.faces",
		name="jsf.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="toggle-class.js",
		target="head"
	)
})
public class ToolkitToggleClassRenderer
extends Renderer
{
	private static final String BEHAVIOR_EVENT_PROPERTY_NAME =
			"javax.faces.behavior.event";
	private static final String SOURCE_PROPERTY_NAME = "javax.faces.source";
	
	public ToolkitToggleClassRenderer()
	{
	}
	
	@Override
	public final boolean getRendersChildren()
	{
		return true;
	}
	
	@Override
	public void decode(FacesContext context, UIComponent component)
	{
		if (component instanceof ToolkitOAuthInit)
			decode(context, (ToolkitToggleClass) component);
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitToggleClass)
			encodeBegin(context, (ToolkitToggleClass) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitToggleClass)
			encodeChildren(context, (ToolkitToggleClass) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitToggleClass)
			encodeEnd(context, (ToolkitToggleClass) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void decode(FacesContext context, ToolkitToggleClass component)
	{
		Map<String, List<ClientBehavior>> behaviorsMap =
				component.getClientBehaviors();
		
		if (!behaviorsMap.isEmpty())
		{
			ExternalContext externalContext = context.getExternalContext();
			Map<String, String> params =
					externalContext.getRequestParameterMap();
			String behaviorEvent = params.get(BEHAVIOR_EVENT_PROPERTY_NAME);
			
			if (behaviorEvent != null)
			{
				List<ClientBehavior> behaviors = behaviorsMap.get(
						behaviorEvent);
				if (behaviors != null && !behaviors.isEmpty())
				{
					String behaviorSource = params.get(SOURCE_PROPERTY_NAME);
					if (isBehaviorSource(context, behaviorSource,
							component.getClientId()))
						for (ClientBehavior behavior : behaviors)
							behavior.decode(context, component);
				}
			}
		}
	}
	
	private void encodeBegin(FacesContext context,
			ToolkitToggleClass component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("script", null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("type", "text/javascript", null);
	}
	
	private void encodeChildren(FacesContext context,
			ToolkitToggleClass component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		
		StringBuilder js = new StringBuilder();
		js.append("net.preparatusopos.faces.toggleClass.init('");
		js.append(component.getSelect());
		js.append("','");
		js.append(component.getStyleClass());
		js.append("',");
		js.append(component.getDelay());
		js.append(",");
		
		Map<String, List<ClientBehavior>> clientBehaviors =
				component.getClientBehaviors();
		List<ClientBehavior> beforeToggleBehaviors = clientBehaviors.get(
				ToolkitToggleClass.EVENT_BEFORE_TOGGLE);
		List<ClientBehavior> afterToggleBehaviors = clientBehaviors.get(
				ToolkitToggleClass.EVENT_AFTER_TOGGLE);
		List<ClientBehavior> transitionEndBehaviors = clientBehaviors.get(
				ToolkitToggleClass.EVENT_TRANSITION_END);
		
		if (beforeToggleBehaviors == null)
		{
			js.append("null");
		}
		else
		{
			ClientBehaviorContext behaviorContext =
					ClientBehaviorContext.createClientBehaviorContext(context,
					component, ToolkitToggleClass.EVENT_BEFORE_TOGGLE, null,
					Collections.<Parameter>emptyList());
			js.append("function(element,event){");
			for (ClientBehavior behavior : beforeToggleBehaviors)
				js.append(behavior.getScript(behaviorContext));
			js.append("}");
		}
		
		js.append(",");
		
		if (afterToggleBehaviors == null)
		{
			js.append("null");
		}
		else
		{
			ClientBehaviorContext behaviorContext =
					ClientBehaviorContext.createClientBehaviorContext(context,
					component, ToolkitToggleClass.EVENT_AFTER_TOGGLE, null,
					Collections.<Parameter>emptyList());
			js.append("function(element,event){");
			for (ClientBehavior behavior : afterToggleBehaviors)
				js.append(behavior.getScript(behaviorContext));
			js.append("}");
		}
		
		js.append(",");
		
		if (transitionEndBehaviors == null)
		{
			js.append("null");
		}
		else
		{
			ClientBehaviorContext behaviorContext =
					ClientBehaviorContext.createClientBehaviorContext(context,
					component, ToolkitToggleClass.EVENT_TRANSITION_END, null,
					Collections.<Parameter>emptyList());
			js.append("function(element,event){");
			for (ClientBehavior behavior : transitionEndBehaviors)
				js.append(behavior.getScript(behaviorContext));
			js.append("}");
		}
		
		js.append(");");
		writer.writeText(js, null);
	}
	
	private void encodeEnd(FacesContext context, ToolkitToggleClass component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("script");
	}
	
	private boolean isBehaviorSource(FacesContext context,
			String behaviorSource, String clientId)
	{
		return true;
	}
}
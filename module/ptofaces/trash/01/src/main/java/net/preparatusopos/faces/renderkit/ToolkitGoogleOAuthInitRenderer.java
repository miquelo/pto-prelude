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
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Output",
	rendererType="net.preparatusopos.faces.ToolkitGoogleOAuthInit"
)
@ResourceDependencies({
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="google-oauth.js",
		target="head"
	)
})
public class ToolkitGoogleOAuthInitRenderer
extends Renderer
{
	private static final String BEHAVIOR_EVENT_PROPERTY_NAME =
			"javax.faces.behavior.event";
	private static final String SOURCE_PROPERTY_NAME = "javax.faces.source";
	
	private static final String DEFAULT_SCOPES = "profile";
	
	public ToolkitGoogleOAuthInitRenderer()
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
			decode(context, (ToolkitOAuthInit) component);
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOAuthInit)
			encodeBegin(context, (ToolkitOAuthInit) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOAuthInit)
			encodeChildren(context, (ToolkitOAuthInit) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOAuthInit)
			encodeEnd(context, (ToolkitOAuthInit) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void decode(FacesContext context, ToolkitOAuthInit component)
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
	
	private void encodeBegin(FacesContext context, ToolkitOAuthInit component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("script", null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("type", "text/javascript", null);
	}
	
	private void encodeChildren(FacesContext context, ToolkitOAuthInit component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		
		StringBuilder js = new StringBuilder();
		js.append("net.preparatusopos.faces.googleOAuth.init('");
		js.append(component.getClientId());
		js.append("','");
		js.append(component.getAuthClientId());
		js.append("','");
		if (component.getScopes() == null)
			js.append(DEFAULT_SCOPES);
		else
			js.append(component.getScopes());
		js.append("'");
		
		Map<String, List<ClientBehavior>> clientBehaviors =
				component.getClientBehaviors();
		List<ClientBehavior> successBehaviors = clientBehaviors.get(
				ToolkitOAuthInit.EVENT_SUCCESS);
		
		if (successBehaviors != null)
		{
			ClientBehaviorContext behaviorContext =
					ClientBehaviorContext.createClientBehaviorContext(context,
					component, ToolkitOAuthInit.EVENT_SUCCESS, null,
					Collections.<Parameter>emptyList());
			js.append(",function(event){");
			for (ClientBehavior successBehavior : successBehaviors)
			{
				js.append(successBehavior.getScript(behaviorContext));
				js.append(";");
			}
			js.append("}");
		}
		
		js.append(");");
		
		writer.writeText(js, null);
	}
	
	private void encodeEnd(FacesContext context, ToolkitOAuthInit component)
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
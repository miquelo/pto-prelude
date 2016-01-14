package net.preparatusopos.faces.renderkit;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitOutputRedirect;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Output",
	rendererType="net.preparatusopos.faces.ToolkitOutputRedirect"
)
@ResourceDependencies({
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="redirect.js",
		target="head"
	)
})
public class ToolkitOutputRedirectRenderer
extends Renderer
{
	public static final String META_TAG_NAME = "faces-redirect";
	
	public ToolkitOutputRedirectRenderer()
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
		
		if (component instanceof ToolkitOutputRedirect)
			encodeBegin(context, (ToolkitOutputRedirect) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitOutputRedirect)
			encodeEnd(context, (ToolkitOutputRedirect) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context,
			ToolkitOutputRedirect component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("meta", null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("name", META_TAG_NAME, null);
		writer.writeAttribute("content", component.getPath(), null);
		
	}
	
	private void encodeEnd(FacesContext context, ToolkitOutputRedirect component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("meta");
	}
}
package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitPanelFieldGroup;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Panel",
	rendererType="net.preparatusopos.faces.ToolkitPanelFieldGroup"
)
public class ToolkitPanelFieldGroupRenderer
extends Renderer
{
	public ToolkitPanelFieldGroupRenderer()
	{
	}
	
	@Override
	public final boolean getRendersChildren()
	{
		return false;
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelFieldGroup)
			encodeBegin(context, (ToolkitPanelFieldGroup) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelFieldGroup)
			encodeEnd(context, (ToolkitPanelFieldGroup) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context,
			ToolkitPanelFieldGroup component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("ul", null);
		writer.writeAttribute("id", component.getClientId(), null);
		
		Set<String> styleClasses = new HashSet<>();
		if (component.getStyleClass() != null)
			styleClasses.add(component.getStyleClass());
		Util.writeStyleClass(writer, styleClasses, null);
	}
	
	private void encodeEnd(FacesContext context,
			ToolkitPanelFieldGroup component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("ul");
	}
}
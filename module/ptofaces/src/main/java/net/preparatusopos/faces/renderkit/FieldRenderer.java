package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitPanelField;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Panel",
	rendererType="net.preparatusopos.faces.Field"
)
public class FieldRenderer
extends Renderer
{
	public FieldRenderer()
	{
	}
	
	@Override
	public boolean getRendersChildren()
	{
		return false;
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelField)
			encodeBegin(context, (ToolkitPanelField) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		if (component instanceof ToolkitPanelField)
			encodeEnd(context, (ToolkitPanelField) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context, ToolkitPanelField component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(), null);
		Set<String> styleClasses = new HashSet<>();
		if (component.getStyleClass() != null)
			styleClasses.add(component.getStyleClass());
		if (!Util.isValid(component))
		{
			String errorClass = component.getErrorClass();
			if (errorClass != null)
				styleClasses.add(errorClass);
		}
		Util.encodeStyleClass(context, styleClasses, null);
	}
	
	private void encodeEnd(FacesContext context, ToolkitPanelField component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
}

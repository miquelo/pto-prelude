package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitPanelField;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Panel",
	rendererType="net.preparatusopos.faces.ToolkitPanelField"
)
public class ToolkitPanelFieldRenderer
extends Renderer
{
	public ToolkitPanelFieldRenderer()
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
		
		if (component instanceof ToolkitPanelField)
			encodeBegin(context, (ToolkitPanelField) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelField)
			encodeEnd(context, (ToolkitPanelField) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context, ToolkitPanelField component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("li", null);
		writer.writeAttribute("id", component.getClientId(), null);
		
		boolean valid = true;
		Iterator<UIComponent> it = component.getChildren().iterator();
		while (valid && it.hasNext())
		{
			UIComponent child = it.next();
			if (child instanceof UIInput)
				valid = valid && ((UIInput) child).isValid();
		}
		
		Set<String> styleClasses = new HashSet<>();
		if (component.getStyleClass() != null)
			styleClasses.add(component.getStyleClass());
		if (!valid)
			styleClasses.add(component.getErrorClass());
		Util.writeStyleClass(writer, styleClasses, null);
	}
	
	private void encodeEnd(FacesContext context, ToolkitPanelField component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("li");
	}
}
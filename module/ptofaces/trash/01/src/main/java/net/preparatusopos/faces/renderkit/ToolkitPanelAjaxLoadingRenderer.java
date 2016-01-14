package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitPanelAjaxLoading;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Panel",
	rendererType="net.preparatusopos.faces.ToolkitPanelAjaxLoading"
)
@ResourceDependencies({
	@ResourceDependency(
		library="javax.faces",
		name="jsf.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="ajax-loading.js",
		target="head"
	)
})
public class ToolkitPanelAjaxLoadingRenderer
extends Renderer
{
	public ToolkitPanelAjaxLoadingRenderer()
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
		
		if (component instanceof ToolkitPanelAjaxLoading)
			encodeBegin(context, (ToolkitPanelAjaxLoading) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelAjaxLoading)
			encodeEnd(context, (ToolkitPanelAjaxLoading) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context,
			ToolkitPanelAjaxLoading component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", null);
		writer.writeAttribute("id", component.getClientId(), null);
		
		Set<String> styleClasses = new HashSet<>();
		if (component.getHiddenClass() != null)
			styleClasses.add(component.getHiddenClass());
		Util.writeStyleClass(writer, styleClasses, null);
	}
	
	private void encodeEnd(FacesContext context,
			ToolkitPanelAjaxLoading component)
	throws IOException
	{
		StringBuilder js = new StringBuilder();
		js.append("net.preparatusopos.faces.ajaxLoading.init('");
		js.append(component.getClientId());
		js.append("',");
		if (component.getVisibleClass() == null)
			js.append("null");
		else
			js.append("'").append(component.getVisibleClass()).append("'");
		js.append(",");
		if (component.getHiddenClass() == null)
			js.append("null");
		else
			js.append("'").append(component.getHiddenClass()).append("'");
		js.append(");");
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText(js, null);
		writer.endElement("script");
		
		writer.endElement("div");
	}
}
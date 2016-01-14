package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.SelectOne",
	rendererType="net.preparatusopos.faces.HtmlSelectOneMenu"
)
@ResourceDependencies({
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="select-one-menu.js",
		target="head"
	)
})
public class HtmlSelectOneMenuRenderer
extends Renderer
{
	private static final String WRAPPER_ID = "wrapper";
	private static final String VALUE_INPUT_ID = "value";
	
	public static final String META_TAG_NAME = "faces-redirect";
	
	public HtmlSelectOneMenuRenderer()
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
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			decode(context, (HtmlSelectOneMenu) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			encodeBegin(context, (HtmlSelectOneMenu) component);
		else
			Util.unsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			encodeEnd(context, (HtmlSelectOneMenu) component);
		else
			Util.unsupportedComponent(component);
	}
	
	private void decode(FacesContext context, HtmlSelectOneMenu component)
	{
		// TODO ...
	}
	
	private void encodeBegin(FacesContext context, HtmlSelectOneMenu component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", null);
		writer.writeAttribute("id", getClientId(context, component, WRAPPER_ID),
				null);
		
		Set<String> styleClasses = new HashSet<>();
		styleClasses.add(component.getStyleClass());
		Util.writeStyleClass(writer, styleClasses, null);
		
		writer.startElement("input", null);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.endElement("input");
		
		writer.startElement("div", null);
		
		writer.startElement("input", null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getClientId(context, component,
				VALUE_INPUT_ID), null);
		writer.endElement("input");
		
		writer.startElement("span", null);
		writer.writeAttribute("onclick", "return false;", null);
		writer.writeText("\u25BC", null);
		writer.endElement("span");
		
		writer.endElement("div");
	}
	
	private void encodeEnd(FacesContext context, HtmlSelectOneMenu component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
	
	private static String getClientId(FacesContext context,
			UIComponent component, String childId)
	{
		return String.format("%s%s%s",
				component.getClientId(), UINamingContainer.getSeparatorChar(
				context), childId);
	}
}
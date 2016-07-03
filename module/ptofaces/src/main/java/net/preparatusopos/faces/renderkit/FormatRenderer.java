package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.component.ToolkitPanelFormat;
import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.Panel",
	rendererType="net.preparatusopos.faces.Format"
)
public class FormatRenderer
extends Renderer
{
	public FormatRenderer()
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
		
		if (component instanceof ToolkitPanelFormat)
			encodeBegin(context, (ToolkitPanelFormat) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelFormat)
			encodeEnd(context, (ToolkitPanelFormat) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof ToolkitPanelFormat)
			encodeChildren(context, (ToolkitPanelFormat) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	private void encodeBegin(FacesContext context, ToolkitPanelFormat component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement(getContainerElementName(component), component);
		
		Set<String> styleClasses = new HashSet<>();
		if (component.getStyleClass() != null)
			styleClasses.add(component.getStyleClass());
		Util.encodeStyleClass(context, styleClasses, null);
	}
	
	private void encodeEnd(FacesContext context, ToolkitPanelFormat component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(getContainerElementName(component));
	}
	
	private void encodeChildren(FacesContext context,
			ToolkitPanelFormat component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		List<String> contentList = new ArrayList<>();
		for (UIParameter param : Util.getParameters(component))
		{
			StringWriter out = new StringWriter();
			ResponseWriter childWriter = writer.cloneWithWriter(out);
			context.setResponseWriter(childWriter);
			param.encodeAll(context);
			context.setResponseWriter(writer);
			childWriter.flush();
			contentList.add(out.toString());
			childWriter.close();
		}
		
		MessageFormat mf = new MessageFormat(component.getText());
		writer.write(mf.format(contentList.toArray()));
	}
	
	private static String getContainerElementName(ToolkitPanelFormat component)
	{
		String layout = component.getLayout();
		switch (layout == null ? "inline" : layout)
		{
			case "block":
			return "div";
			
			case "paragraph":
			return "p";
			
			default:
			return "span";
		}
	}
}

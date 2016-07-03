package net.preparatusopos.faces.renderkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import net.preparatusopos.faces.util.Util;

@FacesRenderer(
	componentFamily="javax.faces.SelectOne",
	rendererType="net.preparatusopos.faces.Menu"
)
@ResourceDependencies({
	@ResourceDependency(
		library="javax.faces",
		name="jsf.js",
		target="head"
	),
	@ResourceDependency(
		library="net.preparatusopos.faces",
		name="script/select-one-menu.js",
		target="head"
	)
})
public class MenuRenderer
extends Renderer
{
	public static final String CSS_CLASS = "select-one-menu";
	
	public MenuRenderer()
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
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			encodeBegin(context, (HtmlSelectOneMenu) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			encodeEnd(context, (HtmlSelectOneMenu) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
	throws IOException
	{
		Util.renderParamsNotNull(context, component);
		
		if (component instanceof HtmlSelectOneMenu)
			encodeChildren(context, (HtmlSelectOneMenu) component);
		else
			throw Util.newUnsupportedComponent(component);
	}
	
	private void decode(FacesContext context, HtmlSelectOneMenu component)
	{
		boolean disabled = Util.getAttribute(component, "disabled",
				Boolean.class, false);
		boolean readonly = Util.getAttribute(component, "readonly",
				Boolean.class, false);
		if (!disabled && !readonly)
		{
			String clientId = component.getClientId(context);
			Converter converter = component.getConverter();
			
			ExternalContext externalContext = context.getExternalContext();
			String value = externalContext.getRequestParameterMap().get(
					clientId);
			if (value != null && value.isEmpty())
				value = null;
			if (value == null)
			{
				if (Util.getAttribute(component, "required", Boolean.class,
					false))
				{
					String requiredMessage = Util.getAttribute(component,
							"requiredMessage", String.class, null);
					if (requiredMessage != null)
					context.addMessage(clientId, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, requiredMessage, null));
					component.setValid(false);
				}
				// FIX Method expression is not called after calling
				// component.setSubmittedValue(null)
				else
					component.setValue(null);
			}
			else
				component.setSubmittedValue(converter == null ? value
						: converter.getAsObject(context, component, value));
		}
		Util.decodeBehaviors(context, component);
	}
	
	private void encodeBegin(FacesContext context, HtmlSelectOneMenu component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		
		// Container
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(), null);
		
		Set<String> styleClasses = new HashSet<>();
		styleClasses.add(CSS_CLASS);
		String styleClass = component.getStyleClass();
		if (styleClass != null)
			styleClasses.add(styleClass);
		Util.encodeStyleClass(context, styleClasses, null);
		
		writer.writeAttribute("style", component.getStyle(), null);
	}
	
	private void encodeEnd(FacesContext context, HtmlSelectOneMenu component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		Converter converter = component.getConverter();
		
		// List items
		writer.endElement("ul");
		
		// List wrapper
		writer.endElement("div");
		
		// List
		writer.endElement("div");
		
		// Input
		Object value = component.getValue();
		if (converter != null)
			value = converter.getAsString(context, component, value);
		writer.startElement("input", null);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.writeAttribute("value", value, null);
		if (component.isDisabled())
			writer.writeAttribute("disabled", "", null);
		if (component.isReadonly())
			writer.writeAttribute("readonly", "", null);
		
		Util.encodeOnEventAttribute(context, component, "onchange",
				"valueChange");
		writer.endElement("input");
		
		// Container
		writer.endElement("div");
	}
	
	private void encodeChildren(FacesContext context,
			HtmlSelectOneMenu component)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		Converter converter = component.getConverter();
		List<SelectItem> selectItems = Util.selectItems(context, component);
		
		// Button
		writer.startElement("div", component);
		if (!component.isDisabled())
		{
			String tabindex = component.getTabindex();
			if (tabindex == null || tabindex.isEmpty())
				tabindex = "0";
			writer.writeAttribute("tabindex", tabindex, null);
		}
		
		Set<String> styleClasses = new HashSet<>();
		String buttonClass = Util.getAttribute(component, "buttonClass",
				String.class, null);
		if (buttonClass != null)
			styleClasses.add(buttonClass);
		Util.encodeStyleClass(context, styleClasses, null);
		
		encodeItem(context, component, findInitialItem(component, selectItems));
		
		writer.endElement("div");
		
		// List
		writer.startElement("div", component);
		
		// List wrapper
		writer.startElement("div", component);
		
		// List items
		writer.startElement("ul", component);
		
		int index = 0;
		boolean hideNoSelectionOption = Util.getAttribute(component,
				"hideNoSelectionOption", Boolean.class, false);
		for (SelectItem selectItem : selectItems)
		{
			if (hideNoSelectionOption && selectItem.isNoSelectionOption())
				continue;
			Object value = selectItem.getValue();
			if (converter != null)
				value = converter.getAsString(context, component, value);
			writer.startElement("li", null);
			writer.writeAttribute("data-index", index++, null);
			writer.writeAttribute("data-value", value, null);
			encodeItem(context, component, selectItem);
			writer.endElement("li");
		}
	}
	
	private static void encodeItem(FacesContext context, UIComponent component,
			SelectItem selectItem)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		UIComponent facet = component.getFacet("content");
		String varName = Util.getAttribute(component, "var", String.class,
				null);
		if (facet == null)
		{
			String label = selectItem.getLabel();
			if (label != null)
				writer.writeText(label, null);
		}
		else
		{
			Object oldVariable = null;
			if (varName != null)
			{
				oldVariable = Util.getVariable(context, varName);
				Util.putVariable(context, varName, selectItem);
			}
			facet.encodeAll(context);
			if (oldVariable != null)
				Util.putVariable(context, varName, oldVariable);
		}
	}
	
	private static SelectItem findInitialItem(UIInput component,
			Iterable<SelectItem> selectItems)
	{
		Object value = component.getValue();
		if (value != null)
			for (SelectItem selectItem : selectItems)
				if (value.equals(selectItem.getValue()))
					return selectItem;
		for (SelectItem selectItem : selectItems)
			if (selectItem.isNoSelectionOption())
				return selectItem;
		return new SelectItem();
	}
}

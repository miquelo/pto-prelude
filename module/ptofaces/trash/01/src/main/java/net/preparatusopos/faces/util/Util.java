package net.preparatusopos.faces.util;

import java.io.IOException;
import java.util.Set;

import javax.faces.application.Resource;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.preparatusopos.faces.component.FacesResource;

public class Util
{
	private static final String STYLE_CLASS_ATTRIBUTE_NAME = "class";
	
	private Util()
	{
	}
	
	public static String getClientId(ClientBehaviorContext context,
			String clientId)
	{
		return getClientId(context.getFacesContext(), context.getComponent(),
				clientId);
	}
	
	public static String getClientId(FacesContext context,
			UIComponent component, String clientId)
	{
		UIComponent namingContainer = findNamingContainer(component);
		if (namingContainer == null)
			return clientId;
		
		StringBuilder resultId = new StringBuilder();
		resultId.append(namingContainer.getClientId());
		resultId.append(UINamingContainer.getSeparatorChar(context));
		resultId.append(clientId);
		return resultId.toString();
	}
	
	public static void notNull(String varname, Object var)
	{
		if (var == null)
			throw new NullPointerException(varname + " cannot be null");
	}
	
	public static void renderParamsNotNull(FacesContext context,
			UIComponent component)
	{
		Util.notNull("context", context);
		Util.notNull("component", component);
	}
	
	public static void unsupportedComponent(UIComponent component)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported component of type ");
		msg.append(component.getClass());
		throw new IllegalStateException(msg.toString());
	}
	
	public static String unsupportedClientBehavior(ClientBehavior behavior)
	{
		StringBuilder js = new StringBuilder();
		js.append("console.log(\"Unsupported behavior of type ");
		js.append(behavior.getClass()).append("\");return false;");
		return js.toString();
	}
	
	public static String createResourceRef(FacesContext context,
			FacesResource resource)
	{
		Resource res = context.getApplication().getResourceHandler(
				).createResource(resource.getName(), resource.getLibrary());
		return res.getRequestPath();
	}
	
	public static void writeStyleClass(ResponseWriter writer,
			Set<String> styleClasses, String propertyName)
	throws IOException
	{
		writer.writeAttribute(STYLE_CLASS_ATTRIBUTE_NAME, getStyleClassValue(
				styleClasses), propertyName);
	}
	
	private static String getStyleClassValue(Set<String> styleClasses)
	{
		if (styleClasses.isEmpty())
			return null;
		
		StringBuilder value = new StringBuilder();
		String sep = "";
		for (String styleClass : styleClasses)
		{
			value.append(sep).append(styleClass);
			sep = " ";
		}
		return value.toString();
	}
	
	private static UIComponent findNamingContainer(UIComponent component)
	{
		UIComponent parent = component.getParent();
		while (parent != null)
		{
			if (parent instanceof NamingContainer)
				return parent;
			parent = parent.getParent();
		}
		return null;
	}
}

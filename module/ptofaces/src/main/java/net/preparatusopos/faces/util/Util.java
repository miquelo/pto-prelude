package net.preparatusopos.faces.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class Util
{
	private Util()
	{
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
}

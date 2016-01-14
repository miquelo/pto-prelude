package net.preparatusopos.app.ma.web.util;

import java.security.Principal;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class Util
{
	private Util()
	{
	}
	
	public static Logger getLogger()
	{
		return Logger.getLogger("net.preparatusopos.app.ma.web");
	}
	public static boolean isLogged()
	{
		return getUserPrincipal() != null;
	}
	
	public static Principal getUserPrincipal()
	{
		return getServletRequest().getUserPrincipal();
	}
	
	private static ExternalContext getExternalContext()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext();
	}
	
	private static HttpServletRequest getServletRequest()
	{
		ExternalContext context = getExternalContext();
		Object request = context.getRequest();
		if (request instanceof HttpServletRequest)
			return (HttpServletRequest) request;
		
		StringBuilder msg = new StringBuilder();
		msg.append("Request of type ").append(request.getClass());
		msg.append(" is not supported");
		throw new UnsupportedOperationException(msg.toString());
	}
}
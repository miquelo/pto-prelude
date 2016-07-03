package net.preparatusopos.app.ma.web.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class Util
{
	private static final Charset PASSWORD_CHARSET = StandardCharsets.UTF_8;
	private static final String PASSWORD_DIGEST_ALG = "SHA-256";
	
	private static MessageDigest passwordDigest;
	
	static
	{
		passwordDigest = null;
	}
	
	private Util()
	{
	}
	
	public static String nullableString(String str)
	{
		return str == null || str.isEmpty() ? null : str;
	}
	
	public static Logger getLogger()
	{
		return Logger.getLogger("net.preparatusopos.app.ma.web");
	}
	
	public static Locale getRequestLocale()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getRequestLocale();
	}
	
	public static String getViewURI(String relativeURI)
	{
		HttpServletRequest req = getServletRequest();
		StringBuilder uri = new StringBuilder();
		uri.append(req.getScheme()).append("://");
		uri.append(req.getServerName());
		uri.append(req.getContextPath()).append(relativeURI);
		return uri.toString();
	}
	
	public static boolean isLogged()
	{
		return getUserPrincipal() != null;
	}
	
	public static Principal getUserPrincipal()
	{
		return getServletRequest().getUserPrincipal();
	}
	
	public static String getRequestParameter(String name)
	{
		HttpServletRequest req = getServletRequest();
		return req.getParameter(name);
	}
	
	public static void login(String username, byte[] password)
	throws ServletException
	{
		HttpServletRequest req = getServletRequest();
		req.login(username, password == null ? null : new String(password));
	}
	
	public static void logout()
	{
		try
		{
			getServletRequest().logout();
		}
		catch (ServletException exception)
		{
			// TODO Report error
		}
	}
	
	public static String withRequestContextPath(String path)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		StringBuilder result = new StringBuilder();
		result.append(externalContext.getRequestContextPath());
		result.append(path);
		return result.toString();
	}
	
	public static void redirectToHome()
	throws IOException
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		// XXX Meeec!
		externalContext.redirect(withRequestContextPath("/home.html"));
	}
	
	public static byte[] passwordHash(char[] password)
	{
		try
		{
			byte[] bytes = String.valueOf(password).getBytes(PASSWORD_CHARSET);
			return getPasswordDigest().digest(bytes);
		}
		catch (NoSuchAlgorithmException exception)
		{
			throw new FaceletException(exception);
		}
	}
	
	public static <T> T findParent(UIComponent component, Class<T> type)
	{
		UIComponent current = component;
		while (current != null)
		{
			if (type.isAssignableFrom(current.getClass()))
				return type.cast(current);
			current = current.getParent();
		}
		return null;
	}
	
	private static MessageDigest getPasswordDigest()
	throws NoSuchAlgorithmException
	{
		if (passwordDigest == null)
			passwordDigest = MessageDigest.getInstance(PASSWORD_DIGEST_ALG);
		return passwordDigest;
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
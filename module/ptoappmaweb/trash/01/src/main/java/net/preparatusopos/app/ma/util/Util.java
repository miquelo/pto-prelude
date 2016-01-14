package net.preparatusopos.app.ma.web.util;

import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.CredentialRequestInfo;
import net.preparatusopos.app.domain.MemberAdded;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Util
{
	private static AtomicReference<String> loginFormErrorPath;
	
	static
	{
		loginFormErrorPath = new AtomicReference<>();
	}
	
	private Util()
	{
	}
	
	public static Logger getLogger()
	{
		return Logger.getLogger("net.preparatusopos.app.ma.web");
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
	
	public static String getRequestParameter(String name)
	{
		HttpServletRequest req = getServletRequest();
		return req.getParameter(name);
	}
	
	public static String getContextPath()
	{
		HttpServletRequest req = getServletRequest();
		return req.getContextPath();
	}
	
	public static String getRedirectPath(String path)
	{
		StringBuilder redirectStr = new StringBuilder();
		redirectStr.append(getContextPath());
		redirectStr.append("/");
		redirectStr.append(path);
		return redirectStr.toString();
	}
	
	public static CredentialRequestInfo register(Membership membership,
			CredentialRequest req)
	throws MembershipException
	{
		if (isLogged())
			return membership.addCredential(req);
		MemberAdded added = membership.addMember(new MemberInfo(), req);
		return added.getRequestInfo();
	}
	
	public static String login(String username, char[] password)
	{
		return login(username, password, null);
	}
	
	public static String login(String username, char[] password,
			String redirectPath)
	{
		try
		{
			HttpServletRequest req = getServletRequest();
			req.login(username, String.valueOf(password));
			return redirectPath;
		}
		catch (ServletException exception)
		{
			return getLoginFormErrorPath();
		}
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
	
	public static boolean isLogged()
	{
		return getUserPrincipal() != null;
	}
	
	public static Principal getUserPrincipal()
	{
		return getServletRequest().getUserPrincipal();
	}
	
	public static String normalized(String str)
	{
		String trimmed = str == null ? null : str.trim();
		return trimmed == null || trimmed.isEmpty() ? null : trimmed;
	}
	
	private static String getLoginFormErrorPath()
	{
		synchronized (loginFormErrorPath)
		{
			if (loginFormErrorPath.get() == null)
			{
				ServletContext context = Util.getServletContext();
				String errorPage = getLoginFormErrorPage();
				if (errorPage == null)
					throw new IllegalStateException(
							"Login error page is not defined");
				
				StringBuilder path = new StringBuilder();
				path.append(context.getContextPath()).append(errorPage);
				loginFormErrorPath.set(path.toString());
			}
			return loginFormErrorPath.get();
		}
	}
	
	private static String getLoginFormErrorPage()
	{
		try
		{
			XPathFactory fact = XPathFactory.newInstance();
			XPath path = fact.newXPath();
			
			StringBuilder exprStr = new StringBuilder();
			exprStr.append("web-app");
			exprStr.append("/login-config");
			exprStr.append("/form-login-config");
			exprStr.append("/form-error-page");
			XPathExpression expr = path.compile(exprStr.toString());
			return expr.evaluate(getDeploymentDescriptor());
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	
	private static ExternalContext getExternalContext()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext();
	}
	
	private static ServletContext getServletContext()
	{
		ExternalContext context = getExternalContext();
		Object ctx = context.getContext();
		if (ctx instanceof ServletContext)
			return (ServletContext) ctx;
		
		StringBuilder msg = new StringBuilder();
		msg.append("Context of type ").append(ctx.getClass());
		msg.append(" is not supported");
		throw new UnsupportedOperationException(msg.toString());
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
	
	private static Document getDeploymentDescriptor()
	throws ParserConfigurationException, SAXException, IOException
	{
		ServletContext context = getServletContext();
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		return builder.parse(context.getResourceAsStream("/WEB-INF/web.xml"));
	}
}
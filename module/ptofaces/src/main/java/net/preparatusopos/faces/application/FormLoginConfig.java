package net.preparatusopos.faces.application;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.preparatusopos.faces.util.Util;

import org.w3c.dom.Document;

public class FormLoginConfig
{
	private static FormLoginConfig instance;
	
	private URI loginPageURI;
	private URI errorPageURI;
	
	static
	{
		instance = null;
	}
	
	protected FormLoginConfig(URI loginPageURI, URI errorPageURI)
	{
		this.loginPageURI = loginPageURI;
		this.errorPageURI = errorPageURI;
	}

	public URI getLoginPageURI()
	{
		return loginPageURI;
	}
	
	public URI getErrorPageURI()
	{
		return errorPageURI;
	}
	
	public static FormLoginConfig get()
	{
		if (instance != null)
			return instance;
		
		try
		{
			ServletContext context = Util.getServletContext();
			
			DocumentBuilderFactory builderFact =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFact.newDocumentBuilder();
			Document descriptor = builder.parse(context.getResourceAsStream(
					"/WEB-INF/web.xml"));
			
			
			XPathFactory xPathFact = XPathFactory.newInstance();
			XPath path = xPathFact.newXPath();
			
			StringBuilder exprStr = new StringBuilder();
			exprStr.append("web-app");
			exprStr.append("/login-config");
			exprStr.append("/form-login-config");
			exprStr.append("/form-login-page");
			XPathExpression expr = path.compile(exprStr.toString());
			URI loginPageURI = new URI(expr.evaluate(descriptor));
			
			exprStr = new StringBuilder();
			exprStr.append("web-app");
			exprStr.append("/login-config");
			exprStr.append("/form-login-config");
			exprStr.append("/form-error-page");
			expr = path.compile(exprStr.toString());
			URI errorPageURI = new URI(expr.evaluate(descriptor));
			
			return instance = new FormLoginConfig(loginPageURI, errorPageURI);
		}
		catch (Exception exception)
		{
			throw new IllegalStateException(exception);
		}
	}
}
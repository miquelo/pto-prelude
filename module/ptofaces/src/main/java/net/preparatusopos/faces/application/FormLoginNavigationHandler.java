package net.preparatusopos.faces.application;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.ConfigurableNavigationHandlerWrapper;
import javax.faces.application.NavigationCase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FormLoginNavigationHandler
extends ConfigurableNavigationHandlerWrapper
{
	private ConfigurableNavigationHandler wrapped;
	
	public FormLoginNavigationHandler(ConfigurableNavigationHandler wrapped)
	{
		this.wrapped = wrapped;
	}
	
	@Override
	public ConfigurableNavigationHandler getWrapped()
	{
		return wrapped;
	}
	
	@Override
	public Map<String, Set<NavigationCase>> getNavigationCases()
	{
		FormLoginConfig config = FormLoginConfig.get();
		Map<String, Set<NavigationCase>> navigationCasesMap =
				wrapped.getNavigationCases();
		
		String loginViewId = getViewId(config.getLoginPageURI());
		URI errorViewURI = config.getErrorPageURI();
		
		Set<NavigationCase> navigationCases = navigationCasesMap.get(
				loginViewId);
		if (navigationCases == null)
		{
			navigationCases = new HashSet<>();
			navigationCasesMap.put(loginViewId, navigationCases);
		}
		navigationCases.add(new SuccessNavigationCase(loginViewId));
		navigationCases.add(new FailureNavigationCase(loginViewId,
				errorViewURI));
		
		return navigationCasesMap;
    }
	
	private static String getViewId(URI uri)
	{
		String path = uri.getPath();
		if (path != null)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
			if (contextPath != null && path.startsWith(contextPath))
				path = path.substring(contextPath.length());
			// XXX Non-portable
			return path.replace(".html", ".xhtml");
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("Could not get viewId from ").append(uri);
		throw new IllegalStateException(msg.toString());
	}
	
	private static Map<String, List<String>> getRedirectParameters(URI uri)
	{
		Map<String, List<String>> params = new HashMap<>();
		String queryStr = uri.getQuery();
		if (queryStr != null)
			for (String entryStr : queryStr.split(" "))
			{
				String[] entry = entryStr.split("=");
				String key = entry[0];
				List<String> values = params.get(key);
				if (values == null)
				{
					values = new ArrayList<>();
					params.put(key, values);
				}
				if (entry.length > 0)
					values.add(entry[1]);
				
			}
		return params;
	}
	
	private static URI getRefererURI(FacesContext context)
	throws URISyntaxException
	{
		ExternalContext externalContext = context.getExternalContext();
		Object request = externalContext.getRequest();
		if (request instanceof HttpServletRequest)
		{
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			String referer = servletRequest.getHeader("Referer");
			return referer == null ? null : new URI(referer);
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("Request of type ").append(request.getClass());
		msg.append(" is not supported");
		throw new UnsupportedOperationException(msg.toString());
	}
	
	private static URI getRefererURI()
	throws URISyntaxException
	{
		return getRefererURI(FacesContext.getCurrentInstance());
	}
	
	private static class SuccessNavigationCase
	extends NavigationCase
	{
		public SuccessNavigationCase(String loginViewId)
		{
			super(loginViewId, null, "login-success", null, loginViewId,
					Collections.<String, List<String>>emptyMap(), true, true);
		}
		
		@Override
		public String getToViewId(FacesContext context)
		{
			try
			{
				URI refererURI = getRefererURI(context);
				return refererURI == null ? super.getToViewId(context)
						: getViewId(refererURI);
			}
			catch (URISyntaxException exception)
			{
				throw new IllegalStateException(exception);
			}
		}
		
		@Override
		public Map<String, List<String>> getParameters()
		{
			try
			{
				URI refererURI = getRefererURI();
				return refererURI == null ? super.getParameters()
						: getRedirectParameters(refererURI);
			}
			catch (URISyntaxException exception)
			{
				throw new IllegalStateException(exception);
			}
		}
		
		@Override
		public URL getRedirectURL(FacesContext context)
		{
			try
			{
				return getRefererURI(context).toURL();
			}
			catch (URISyntaxException | MalformedURLException exception)
			{
				throw new IllegalStateException(exception);
			}
		}
	}
	
	private static class FailureNavigationCase
	extends NavigationCase
	{
		public FailureNavigationCase(String loginViewId, URI errorViewURI)
		{
			super(loginViewId, null, "login-failure", null, getViewId(
					errorViewURI), getRedirectParameters(errorViewURI), true,
					true);
		}
	}
}

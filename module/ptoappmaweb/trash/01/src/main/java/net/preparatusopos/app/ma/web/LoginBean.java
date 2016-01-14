package net.preparatusopos.app.ma.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.ExternalUIDType;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.ma.web.util.Util;

@ManagedBean(
	name="loginBean"
)
@ViewScoped
public class LoginBean
implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String REDIRECT_PARAMETER_NAME = "r";
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private transient Membership membership;
	
	private String username;
	private String password;
	private String authorizationCode;
	private String redirect;
	private PanelVisibility mailPanelVisibility;
	
	public LoginBean()
	{
		membership = null;
		username = null;
		password = null;
		authorizationCode = null;
		redirect = null;
		mailPanelVisibility = new PanelVisibility(false);
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getAuthorizationCode()
	{
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode)
	{
		this.authorizationCode = authorizationCode;
	}
	
	public String getRedirect()
	{
		return redirect;
	}

	public void setRedirect(String redirect)
	{
		this.redirect = redirect;
	}

	public PanelVisibility getMailPanelVisibility()
	{
		return mailPanelVisibility;
	}

	public void setMailPanelVisibility(PanelVisibility mailPanelVisibility)
	{
		this.mailPanelVisibility = mailPanelVisibility;
	}

	@PostConstruct
	public void init()
	{
		if (Util.isLogged())
			throw new IllegalStateException("Already logged");
		
		String redirectVal = Util.getRequestParameter(REDIRECT_PARAMETER_NAME);
		if (redirectVal != null)
			setRedirect(Util.getRedirectPath(redirectVal));
	}
	
	public void loginMail(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			String normMailAddress = membership.normalizeMailAddress(
					getUsername());
			login(normMailAddress, getPassword().toCharArray());
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	public void loginGoogle(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			String username = membership.fetchExternalUID(
					ExternalUIDType.GOOGLE, getAuthorizationCode());
			login(username);
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	private void login(String username)
	{
		setRedirect(Util.login(username, new char[0], getRedirect()));
	}
	
	private void login(String username, char[] password)
	{
		setRedirect(Util.login(username, password, getRedirect()));
	}
}
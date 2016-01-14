package net.preparatusopos.app.ma.web;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.ma.web.util.Util;

@ManagedBean(
	name="registerBean"
)
@ViewScoped
public class RegisterBean
{
	public static final String TOKEN_HEX_PARAMETER_NAME = "t";
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private Membership membership;
	
	private String tokenHex;
	private RegisterState state;
	private String passwordRepeat;
	private PanelVisibility mailPanelVisibility;
	
	public RegisterBean()
	{
		membership = null;
		tokenHex = null;
		state = null;
		passwordRepeat = null;
		mailPanelVisibility = new PanelVisibility(false);
	}
	
	public String getTokenHex()
	{
		return tokenHex;
	}
	
	public void setTokenHex(String tokenHex)
	{
		this.tokenHex = tokenHex;
	}
	
	public String getStateName()
	{
		return state.getStateName();
	}
	
	public String getMailAddress()
	{
		return state.getMailAddress();
	}
	
	public void setMailAddress(String mailAddress)
	{
		state.setMailAddress(mailAddress);
	}
	
	public String getPassword()
	{
		return state.getPassword();
	}
	
	public void setPassword(String password)
	{
		state.setPassword(password);
	}
	
	public String getPasswordRepeat()
	{
		return passwordRepeat;
	}
	
	public void setPasswordRepeat(String passwordRepeat)
	{
		this.passwordRepeat = passwordRepeat;
	}

	public PanelVisibility getMailPanelVisibility()
	{
		return mailPanelVisibility;
	}

	public void setMailPanelVisibility(PanelVisibility mailPanelVisibility)
	{
		this.mailPanelVisibility = mailPanelVisibility;
	}

	public String getAuthorizationCode()
	{
		return state.getAuthorizationCode();
	}

	public void setAuthorizationCode(String authorizationCode)
	{
		state.setAuthorizationCode(authorizationCode);
	}
	
	@PostConstruct
	public void init()
	{
		setTokenHex(Util.getRequestParameter(TOKEN_HEX_PARAMETER_NAME));
		if (getTokenHex() == null)
			changeState(RegisterStateBegin.class);
		else
			changeState(RegisterStateMailVerified.class);
	}
	
	public void registerMailRequest(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		state.registerMailRequest(event);
	}
	
	public void registerMail(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		state.registerMail(event);
	}
	
	public void registerGoogle(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		state.registerGoogle(event);
	}
	
	public void changeState(Class<? extends RegisterState> stateClass)
	{
		try
		{
			Constructor<? extends RegisterState> constructor =
					stateClass.getConstructor(RegisterBean.class);
			RegisterState state = (RegisterState) constructor.newInstance(this);
			state.setMembership(membership);
			state.init();
			this.state = state;
		}
		catch (NoSuchMethodException | InvocationTargetException |
				IllegalAccessException | InstantiationException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
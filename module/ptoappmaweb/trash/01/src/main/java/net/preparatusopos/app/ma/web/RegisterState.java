package net.preparatusopos.app.ma.web;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.Membership;

public abstract class RegisterState
{
	private RegisterBean owner;
	
	protected Membership membership;
	
	protected RegisterState(RegisterBean owner)
	{
		this.owner = owner;
	}
	
	public void setMembership(Membership membership)
	{
		this.membership = membership;
	}
	
	public abstract String getStateName();
	
	public String getMailAddress()
	{
		throw new IllegalStateException();
	}
	
	public void setMailAddress(String mailAddress)
	{
		throw new IllegalStateException();
	}
	
	public String getPassword()
	{
		throw new IllegalStateException();
	}
	
	public void setPassword(String password)
	{
		throw new IllegalStateException();
	}
	
	public String getAuthorizationCode()
	{
		throw new IllegalStateException();
	}
	
	public void setAuthorizationCode(String authorizationCode)
	{
		throw new IllegalStateException();
	}
	
	public abstract void init();

	public void registerMailRequest(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		throw new IllegalStateException();
	}
	
	public void registerMail(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		throw new IllegalStateException();
	}
	
	public void registerGoogle(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		throw new IllegalStateException();
	}
	
	protected String getTokenHex()
	{
		return owner.getTokenHex();
	}
	
	protected void changeState(Class<? extends RegisterState> stateClass)
	{
		owner.changeState(stateClass);
	}
}
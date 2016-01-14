package net.preparatusopos.app.ma.web;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.CredentialRequestInfo;
import net.preparatusopos.app.domain.CredentialType;
import net.preparatusopos.app.ma.web.util.Util;

public class RegisterStateMailVerified
extends RegisterState
{
	private static final String STATE_NAME = "mail-verified";
	
	private String password;
	
	public RegisterStateMailVerified(RegisterBean owner)
	{
		super(owner);
		password = null;
	}

	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public String getPassword()
	{
		return password;
	}
	
	@Override
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void registerMail(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			char[] passwd = getPassword().toCharArray();
			CredentialRequest req = new CredentialRequest(
					CredentialType.MAIL_ADDRESS);
			req.setTokenHex(getTokenHex());
			req.setPassword(getPassword().toCharArray());
			CredentialRequestInfo requestInfo = Util.register(membership, req);
			Util.login(requestInfo.getMailAddress(), passwd);
			changeState(RegisterStateCompleted.class);
		}
		catch (Exception exception)
		{
			// TODO Report error
			throw new AbortProcessingException(exception);
		}
	}
}
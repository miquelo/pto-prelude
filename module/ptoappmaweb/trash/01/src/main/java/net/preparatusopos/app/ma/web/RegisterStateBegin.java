package net.preparatusopos.app.ma.web;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.CredentialRequestInfo;
import net.preparatusopos.app.domain.CredentialRequestResult;
import net.preparatusopos.app.domain.CredentialType;
import net.preparatusopos.app.domain.ExternalUIDType;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.ma.web.util.Util;

public class RegisterStateBegin
extends RegisterState
{
	private static final String STATE_NAME = "begin";
	
	private static final String TOKEN_REF = "/register.html?t=${token}";
	
	private String mailAddress;
	private String authorizationCode;
	
	public RegisterStateBegin(RegisterBean owner)
	{
		super(owner);
		mailAddress = null;
		authorizationCode = null;
	}

	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public String getMailAddress()
	{
		return mailAddress;
	}

	@Override
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}
	
	@Override
	public String getAuthorizationCode()
	{
		return authorizationCode;
	}
	
	@Override
	public void setAuthorizationCode(String authorizationCode)
	{
		this.authorizationCode = authorizationCode;
	}
	
	@Override
	public void init()
	{
	}
	
	@Override
	public void registerMailRequest(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			CredentialRequestInfo requestInfo = new CredentialRequestInfo(
					CredentialType.MAIL_ADDRESS);
			requestInfo.setTokenRef(Util.getViewURI(TOKEN_REF));
			requestInfo.setMailAddress(getMailAddress());
			membership.requestCredential(requestInfo);
			changeState(RegisterStateMailRequested.class);
		}
		catch (MembershipException exception)
		{
			// TODO Report error
			throw new AbortProcessingException(exception);
		}
	}

	@Override
	public void registerGoogle(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			CredentialRequestInfo requestInfo = new CredentialRequestInfo(
					CredentialType.EXTERNAL_UID);
			requestInfo.setExternalUIDType(ExternalUIDType.GOOGLE);
			requestInfo.setAuthorizationCode(getAuthorizationCode());
			CredentialRequestResult result = membership.requestCredential(
					requestInfo);
			
			CredentialRequest req = result.getRequest();
			if (req == null)
				throw new IllegalStateException("Google ID not available");
			
			Util.register(membership, req);
			Util.login(req.getExternalUserID(), new char[0]);
			changeState(RegisterStateCompleted.class);
		}
		catch (MembershipException exception)
		{
			// TODO Report error
			throw new AbortProcessingException(exception);
		}
	}
}
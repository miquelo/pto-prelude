package net.preparatusopos.app.ma.web;

import java.io.IOException;

import javax.faces.FacesException;
import javax.servlet.ServletException;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.DirectProfilesInfo;
import net.preparatusopos.app.domain.MailCredentialInfo;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.MemberRegistered;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.Token;
import net.preparatusopos.app.ma.web.util.Util;
import net.preparatusopos.tools.file.FileStorageException;

public class RegisterStateVerified
extends RegisterState
{
	private static final String STATE_NAME = "verified";
	
	private CredentialRequest request;
	
	public RegisterStateVerified(RegisterBean owner)
	{
		super(owner);
		request = null;
	}
	
	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public void init()
	{
		try
		{
			Token token = Token.fromHexValue(getTokenHex());
			DirectProfilesInfo dpInfo = new DirectProfilesInfo();
			dpInfo.setCredentialInfo(new MailCredentialInfo());
			MemberInfo memberInfo = new MemberInfo();
			MemberRegistered registered = membership.register(token, dpInfo,
					memberInfo);
			request = registered.getCredentialRequest();
		}
		catch (MembershipException exception)
		{
			throw new FacesException(exception);
		}
	}
	
	@Override
	public void login(UserBean userBean)
	throws ServletException, IOException, MembershipException,
			FileStorageException
	{
		userBean.login(request.getUsername(), request.getPassword());
		Util.redirectToHome();
	}
}
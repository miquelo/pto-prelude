package net.preparatusopos.app.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.DirectProfilesRequest;
import net.preparatusopos.app.domain.ProfileRequest;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.Token;

public abstract class AbstractAccreditator
implements Accreditator
{
	protected AbstractAccreditator()
	{
	}
	
	@PermitAll
	@Override
	public List<ProfileRequestHints> fetchProfileRequestHints(ProfileType type,
			ProfileRequestType requestType)
	throws AccreditatorException
	{
		List<ProfileRequestHints> hintsList = new ArrayList<>();
		switch (requestType)
		{
			case AUTOMATIC:
			fetchProfileRequestHintsAutomatic(hintsList, type);
			break;
			
			case MAIL_DOMAIN_MATCH:
			fetchProfileRequestHintsMailDomainMatch(hintsList, type);
			break;
		}
		return hintsList;
	}
	
	@PermitAll
	@Override
	public boolean supports(CredentialRequest request)
	throws AccreditatorException
	{
		return false;
	}
	
	@PermitAll
	@Override
	public boolean supports(ProfileRequest request)
	throws AccreditatorException
	{
		return false;
	}
	
	@PermitAll
	@Override
	public boolean supports(DirectProfilesRequest request)
	throws AccreditatorException
	{
		return false;
	}
	
	@PermitAll
	@Override
	public Token request(String tokenRef, CredentialRequest request)
	throws AccreditatorException
	{
		throw newUnsupportedRequest(request);
	}
	
	@PermitAll
	@Override
	public Token request(String tokenRef, ProfileRequest request)
	throws AccreditatorException
	{
		throw newUnsupportedRequest(request);
	}
	
	@PermitAll
	@Override
	public Token request(String tokenRef, DirectProfilesRequest request)
	throws AccreditatorException
	{
		throw newUnsupportedRequest(request);
	}
	
	protected void fetchProfileRequestHintsAutomatic(
			List<ProfileRequestHints> hintsList, ProfileType type)
	throws AccreditatorException
	{
	}
	
	protected void fetchProfileRequestHintsMailDomainMatch(
			List<ProfileRequestHints> hintsList, ProfileType type)
	throws AccreditatorException
	{
	}
	
	private static AccreditatorException newUnsupportedRequest(Object request)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported request ").append(request);
		return new AccreditatorException(msg.toString());
	}
}

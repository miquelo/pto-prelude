package net.preparatusopos.app.core.domain;

import java.util.List;

import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.DirectProfilesRequest;
import net.preparatusopos.app.domain.ProfileRequest;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.Token;

public interface Accreditator
{
	public List<ProfileRequestHints> fetchProfileRequestHints(ProfileType type,
			ProfileRequestType requestType)
	throws AccreditatorException;
	
	public boolean supports(CredentialRequest request)
	throws AccreditatorException;
	
	public boolean supports(ProfileRequest request)
	throws AccreditatorException;
	
	public boolean supports(DirectProfilesRequest request)
	throws AccreditatorException;
	
	public Token request(String tokenRef, CredentialRequest request)
	throws AccreditatorException;
	
	public Token request(String tokenRef, ProfileRequest request)
	throws AccreditatorException;
	
	public Token request(String tokenRef, DirectProfilesRequest request)
	throws AccreditatorException;
}

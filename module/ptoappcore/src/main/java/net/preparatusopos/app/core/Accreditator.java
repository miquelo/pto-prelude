package net.preparatusopos.app.core;

import java.util.List;

import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestInfo;
import net.preparatusopos.app.domain.ProfileRequestResult;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.security.auth.PTOPrincipal;

public interface Accreditator
{
	public ProfileRequestType getImplementedType()
	throws AccreditatorException;
	
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException;
	
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo)
	throws AccreditatorException, NoSuchMemberException;
}

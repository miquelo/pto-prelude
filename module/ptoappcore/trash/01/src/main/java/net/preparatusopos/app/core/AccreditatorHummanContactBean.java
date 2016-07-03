package net.preparatusopos.app.core;

import java.util.Collections;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestInfo;
import net.preparatusopos.app.domain.ProfileRequestResult;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.security.auth.PTOPrincipal;

@Remote(
	Accreditator.class
)
@Stateless(
	mappedName="ejb/AccreditatorHummanContact"
)
public class AccreditatorHummanContactBean
implements Accreditator
{
	public AccreditatorHummanContactBean()
	{
	}
	
	@Override
	public ProfileRequestType getImplementedType()
	throws AccreditatorException
	{
		return ProfileRequestType.HUMAN_CONTACT;
	}
	
	@Override
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException
	{
		return Collections.emptyList();
	}
	
	@Override
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo, String tokenRef)
	throws AccreditatorException, NoSuchMemberException
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
}

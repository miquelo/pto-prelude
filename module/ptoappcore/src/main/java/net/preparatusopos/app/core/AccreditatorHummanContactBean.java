package net.preparatusopos.app.core;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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
@DeclareRoles({
	"MEMBER"
})
public class AccreditatorHummanContactBean
implements Accreditator
{
	public AccreditatorHummanContactBean()
	{
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ProfileRequestType getImplementedType()
	throws AccreditatorException
	{
		return ProfileRequestType.HUMAN_CONTACT;
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException
	{
		return Collections.emptyList();
	}

	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo)
	throws AccreditatorException, NoSuchMemberException
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
}

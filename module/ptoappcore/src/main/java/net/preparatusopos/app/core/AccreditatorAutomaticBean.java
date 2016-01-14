package net.preparatusopos.app.core;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.util.Util;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileInfo;
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
	mappedName="ejb/AccreditatorAutomatic"
)
@DeclareRoles({
	"MEMBER"
})
public class AccreditatorAutomaticBean
implements Accreditator
{
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;
	
	public AccreditatorAutomaticBean()
	{
		em = null;
	}

	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ProfileRequestType getImplementedType()
	throws AccreditatorException
	{
		return ProfileRequestType.AUTOMATIC;
	}

	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException
	{
		List<ProfileRequestHints> hintsList = new ArrayList<>();
		if (type.equals(ProfileType.CANDIDATE))
			hintsList.add(new ProfileRequestHints());
		return hintsList;
	}

	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo)
	throws AccreditatorException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		
		ProfileInfo profileInfo = new ProfileInfo();
		profileInfo.setType(requestInfo.getType());
		member.addProfile(profileInfo);
		
		ProfileRequestResult result = new ProfileRequestResult();
		result.setInfo(profileInfo);
		return result;
	}
}

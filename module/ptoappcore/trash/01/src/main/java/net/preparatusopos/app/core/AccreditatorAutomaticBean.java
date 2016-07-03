package net.preparatusopos.app.core;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public ProfileRequestType getImplementedType()
	throws AccreditatorException
	{
		return ProfileRequestType.AUTOMATIC;
	}
	
	@Override
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException
	{
		List<ProfileRequestHints> hintsList = new ArrayList<>();
		if (type.equals(ProfileType.CANDIDATE))
			hintsList.add(new ProfileRequestHints());
		return hintsList;
	}
	
	@Override
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo, String tokenRef)
	throws AccreditatorException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		
		ProfileInfo profileInfo = new ProfileInfo();
		profileInfo.setType(requestInfo.getType());
		member.addProfile(profileInfo);
		
		ProfileRequestResult result = new ProfileRequestResult();
		// TODO result.setRequest(...);
		return result;
	}
}

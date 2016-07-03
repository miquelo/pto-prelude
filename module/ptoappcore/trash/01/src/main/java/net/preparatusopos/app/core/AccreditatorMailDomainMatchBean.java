package net.preparatusopos.app.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.model.RequestProfileMail;
import net.preparatusopos.app.core.util.Util;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestInfo;
import net.preparatusopos.app.domain.ProfileRequestResult;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.notification.Notifier;
import net.preparatusopos.app.notification.NotifierException;
import net.preparatusopos.app.notification.NotifierMessage;
import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOManagedPrincipal;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.PTORealmBootstrap;
import net.preparatusopos.security.auth.PTORealmManager;
import net.preparatusopos.security.auth.PTORealmManagerException;
import net.preparatusopos.security.auth.PTORealmManagerFactory;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;

@Remote(
	Accreditator.class
)
@Stateless(
	mappedName="ejb/AccreditatorMailDomainMatch"
)
public class AccreditatorMailDomainMatchBean
implements Accreditator
{
	private static Map<ProfileType, List<String>> profileDomains;
	
	@Resource(
		lookup="jdbc/PTORealmDataSource"
	)
	private DataSource realmDataSource;
	
	@Resource(
		lookup="ejb/MailNotifier"
	)
	private Notifier mailNotifier;
	
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;
	
	public AccreditatorMailDomainMatchBean()
	{
		em = null;
	}
	
	@Override
	public ProfileRequestType getImplementedType()
	throws AccreditatorException
	{
		return ProfileRequestType.MAIL_DOMAIN_MATCH;
	}
	
	@Override
	public List<ProfileRequestHints> requestHints(ProfileType type)
	throws AccreditatorException
	{
		List<ProfileRequestHints> hintsList = new ArrayList<>();
		List<String> domains = getProfileDomains().get(type);
		if (domains != null)
		{
			for (String domain : domains)
			{
				ProfileRequestHints hints = new ProfileRequestHints();
				hints.setMailDomain(domain);
				hintsList.add(hints);
			}
		}
		return hintsList;
	}

	@Override
	public ProfileRequestResult request(PTOPrincipal principal,
			ProfileRequestInfo requestInfo, String tokenRef)
	throws AccreditatorException, NoSuchMemberException
	{
		try
		{
			Member member = Util.findMember(em, principal);
			if (!acceptedDomain(requestInfo.getType(),
					requestInfo.getMailDomain()))
			{
				StringBuilder msg = new StringBuilder();
				msg.append("Invalid mail domain '");
				msg.append(requestInfo.getMailDomain()).append("'");
				throw new AccreditatorException(msg.toString());
			}
			String mailAddress = String.format("%s@%s",
					Util.normalizeMailAddress(requestInfo.getMailUsername()),
					requestInfo.getMailDomain());
			
			ProfileRequestResult result = new ProfileRequestResult();
			if (alreadyApproved(member, mailAddress))
			{
				// TODO result.setRequest(...);
			}
			else
			{
				RequestProfileMail req = new RequestProfileMail();
				req.setToken(Util.createToken());
				req.setTarget(member);
				req.setProfileType(ProfileType.SPAIN_JUDGE.ordinal());
				req.setMailAddress(mailAddress);
				em.persist(req);
				
				NotifierMessage message = new NotifierMessage();
				message.setTarget(mailAddress);
				message.setSubjectText("Acreditación para Prepara tus Opos");
				
				String tokenHex = DatatypeConverter.printHexBinary(
						req.getToken());
				StringBuilder text = new StringBuilder();
				text.append("Use this link to get accreditation:\n");
				text.append(tokenRef.replace("${token}", tokenHex));
				message.setBodyText(text.toString());
				
				mailNotifier.messageSend(message);
			}
			return result;
		}
		catch (NotifierException exception)
		{
			throw new AccreditatorException(exception);
		}
	}
	
	private boolean alreadyApproved(Member member, String mailAddr)
	throws AccreditatorException
	{
		try (PTORealmConnection conn = Util.getRealmConnection(
				realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			
			PTOPrincipal principal = new PTOPrincipal(member.getUID());
			PTOManagedPrincipal managed = rm.manage(principal);
			for (PTOCredential cred : managed.getCredentials())
				if (mailAddr.equals(cred.getUsername()))
					return true;
			return false;
		}
		catch (IOException | PTORealmManagerException |
				PTORealmConnectionException exception)
		{
			throw new AccreditatorException(exception);
		}
	}
	
	private static Map<ProfileType, List<String>> getProfileDomains()
	{
		if (profileDomains == null)
		{
			profileDomains = new HashMap<>();
			profileDomains.put(ProfileType.SPAIN_JUDGE, Arrays.asList(
				"poderjudicial.es",
				"gmail.com"
			));
			profileDomains.put(ProfileType.SPAIN_NOTARY, Arrays.asList(
				"gmail.com"
			));
		}
		return profileDomains;
	}
	
	private static boolean acceptedDomain(ProfileType type, String domain)
	{
		List<String> domains = getProfileDomains().get(type);
		return domains != null && domains.contains(domain);
	}
}

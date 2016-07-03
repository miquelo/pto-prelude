package net.preparatusopos.app.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.preparatusopos.app.core.domain.model.RequestCredentialMailAddress;
import net.preparatusopos.app.core.domain.model.RequestDirectProfilesToken;
import net.preparatusopos.app.core.domain.model.RequestProfile;
import net.preparatusopos.app.core.notification.Notifier;
import net.preparatusopos.app.core.notification.NotifierException;
import net.preparatusopos.app.core.notification.NotifierMessage;
import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.DirectProfilesRequest;
import net.preparatusopos.app.domain.MailCredentialRequest;
import net.preparatusopos.app.domain.MailDomainMatchProfileRequest;
import net.preparatusopos.app.domain.ProfileRequest;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.Token;

@Remote(
	Accreditator.class
)
@Stateless(
	mappedName="ejb/AccreditatorMailBean"
)
public class AccreditatorMailBean
extends AbstractAccreditator
{
	@EJB(
		lookup="ejb/MailNotifier"
	)
	private Notifier mailNotifier;
	
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;
	
	public AccreditatorMailBean()
	{
		mailNotifier = null;
		em = null;
	}

	@PermitAll
	@Override
	public boolean supports(DirectProfilesRequest request)
	throws AccreditatorException
	{
		CredentialRequest credReq = request.getCredentialRequest();
		if (!(credReq instanceof MailCredentialRequest))
			return false;
		
		MailCredentialRequest mailCredReq = (MailCredentialRequest) credReq;
		String mailAddr = mailCredReq.getMailAddress();
		for (ProfileRequest profReq : request.getValues())
		{
			if (!(profReq instanceof MailDomainMatchProfileRequest))
				return false;
			if (!mailMatch(mailAddr, (MailDomainMatchProfileRequest) profReq))
				return false;
			if (!mailSupported((MailDomainMatchProfileRequest) profReq))
				return false;
		}
		return true;
	}
	
	@PermitAll
	@Override
	public Token request(String tokenRef, DirectProfilesRequest request)
	throws AccreditatorException
	{
		try
		{
			MailCredentialRequest credReq = (MailCredentialRequest)
					request.getCredentialRequest();
			
			RequestDirectProfilesToken token =
					RequestDirectProfilesToken.build();
			
			RequestCredentialMailAddress reqCred =
					new RequestCredentialMailAddress();
			reqCred.setMailAddress(credReq.getMailAddress());
			reqCred.setPassword(credReq.getPassword());
			token.setCredential(reqCred);
			
			for (ProfileRequest profReq : request.getValues())
			{
				RequestProfile reqProf = new RequestProfile();
				reqProf.setType(profReq.getType());
				token.getProfiles().add(reqProf);
			}
			em.persist(token);
			
			NotifierMessage message = new NotifierMessage();
			message.setTarget(reqCred.getMailAddress());
			message.setSubjectText("Prepara tus Opos registration");
			
			StringBuilder text = new StringBuilder();
			text.append("Welcome to Prepara tus Opos. Use this link to ");
			text.append("activate your account:\n");
			text.append(tokenRef.replace("${token}", token.getHexValue()));
			message.setBodyText(text.toString());
			
			mailNotifier.messageSend(message);
			
			return Token.fromEmptyValue();
		}
		catch (NotifierException exception)
		{
			throw new AccreditatorException(exception);
		}
	}

	@Override
	protected void fetchProfileRequestHintsMailDomainMatch(
			List<ProfileRequestHints> hintsList, ProfileType type)
	throws AccreditatorException
	{
		for (String domain : getProfileMailDomains(type))
		{
			ProfileRequestHints hints = new ProfileRequestHints();
			hints.setMailDomain(domain);
			hintsList.add(hints);
		}
	}
	
	private static List<String> getProfileMailDomains(ProfileType type)
	{
		List<String> mailDomains = new ArrayList<>();
		if (ProfileType.Role.TRAINER.equals(type.getRole()))
		{
			if (ProfileType.Country.SPAIN.equals(type.getCountry()))
			{
				switch (type.getField())
				{
					case JUDICATURE:
					if (type.getSpecialization() == null)
						mailDomains.add("poderjudicial.es");
					break;
					
					default:
					// Nothing to be done
				}
			}
			mailDomains.add("gmail.com");
		}
		return mailDomains;
	}
	
	private static boolean mailMatch(String mailAddr,
			MailDomainMatchProfileRequest req)
	{
		return mailAddr.equals(String.format("%s@%s", req.getMailUsername(),
				req.getMailDomain()));
	}
	
	private static boolean mailSupported(MailDomainMatchProfileRequest req)
	{
		ProfileType type = req.getType();
		return ProfileType.Role.CANDIDATE.equals(type.getRole()) ||
				getProfileMailDomains(type).contains(req.getMailDomain());
	}
}

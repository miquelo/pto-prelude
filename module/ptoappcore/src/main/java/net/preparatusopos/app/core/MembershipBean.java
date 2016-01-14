package net.preparatusopos.app.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.model.Profile;
import net.preparatusopos.app.core.model.RequestCredentialMail;
import net.preparatusopos.app.core.model.meta.Member_;
import net.preparatusopos.app.core.model.meta.RequestMemberMail_;
import net.preparatusopos.app.core.util.Util;
import net.preparatusopos.app.domain.CredentialInfo;
import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.CredentialRequestInfo;
import net.preparatusopos.app.domain.CredentialRequestResult;
import net.preparatusopos.app.domain.CredentialType;
import net.preparatusopos.app.domain.ExternalUIDType;
import net.preparatusopos.app.domain.MemberAdded;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.NoSuchCredentialRequestException;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileInfo;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestInfo;
import net.preparatusopos.app.domain.ProfileRequestResult;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.notification.Notifier;
import net.preparatusopos.app.notification.NotifierException;
import net.preparatusopos.app.notification.NotifierMessage;
import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOCredentialDuplicatedException;
import net.preparatusopos.security.auth.PTOCredentialFactory;
import net.preparatusopos.security.auth.PTOManagedPrincipal;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.PTORealmBootstrap;
import net.preparatusopos.security.auth.PTORealmManager;
import net.preparatusopos.security.auth.PTORealmManagerException;
import net.preparatusopos.security.auth.PTORealmManagerFactory;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.external.ExternalUIDConnection;
import net.preparatusopos.security.external.ExternalUIDConnectionException;
import net.preparatusopos.security.external.ExternalUIDConnectionFactory;

@Remote(
	Membership.class
)
@Stateless(
	name="ejb/MembershipBean"
)
@DeclareRoles({
	"MEMBER"
})
public class MembershipBean
implements Membership
{
	private static final Charset PASSWORD_CHARSET = StandardCharsets.UTF_8;
	private static final String PASSWORD_DIGEST_ALG = "SHA-256";
	
	private static MessageDigest passwordDigest;
	
	@Resource(
		lookup="properties/PTOSettings"
	)
	private Properties settings;
	
	@Resource(
		lookup="extuid/PTOGoogleExternalID"
	)
	private ExternalUIDConnectionFactory extUIDGoogleFact;
	
	@Resource(
		lookup="jdbc/PTORealmDataSource"
	)
	private DataSource realmDataSource;
	
	@Resource(
		lookup="ejb/MailNotifier"
	)
	private Notifier mailNotifier;
	
	@Resource(
		lookup="ejb/AccreditatorAutomatic"
	)
	private Accreditator accredAutomatic;
	
	@Resource(
		lookup="ejb/AccreditatorHummanContact"
	)
	private Accreditator accredHumanContact;
	
	@Resource(
		lookup="ejb/AccreditatorMailDomainMatch"
	)
	private Accreditator accredMailDomainMatch;
	
	@Resource
	private EJBContext context;
	
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;
	
	static
	{
		passwordDigest = null;
	}
	
	public MembershipBean()
	{
		settings = null;
		extUIDGoogleFact = null;
		realmDataSource = null;
		mailNotifier = null;
		accredHumanContact = null;
		accredMailDomainMatch = null;
		context = null;
		em = null;
	}
	
	@Override
	public String normalizeMailAddress(String mailAddr)
	throws MembershipException
	{
		return Util.normalizeMailAddress(mailAddr);
	}
	
	@Override
	public String passwordHash(String password)
	throws MembershipException
	{
		return new String(passwordHash(password.toCharArray()),
				PASSWORD_CHARSET);
	}
	
	@Override
	public String fetchExternalUID(ExternalUIDType type,
			String authorizationCode)
	throws MembershipException
	{
		try (ExternalUIDConnection conn = getExternalUIDConnection(type))
		{
			return type.format(conn.getExternalUID(authorizationCode));
		}
		catch (ExternalUIDConnectionException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
		catch (IOException exception)
		{
			throw new MembershipException(exception);
		}
	}
	
	@Override
	public CredentialRequestResult requestCredential(
			CredentialRequestInfo requestInfo)
	throws MembershipException
	{
		CredentialType type = requestInfo.getType();
		switch (type)
		{
			case MAIL_ADDRESS:
			return requestCredentialMail(requestInfo);
			
			case EXTERNAL_UID:
			return requestCredentialExternalUID(requestInfo);
			
			default:
			return null;
		}
	}
	
	@Override
	public List<CredentialInfo> fetchCredentials()
	throws MembershipException
	{
		try
		{
			return fetchCredentials(getCallerPrincipal());
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}
	
	@Override
	public List<CredentialInfo> fetchCredentials(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			
			List<CredentialInfo> credInfoList = new ArrayList<>();
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			for (PTOCredential cred : managedPrincipal.getCredentials())
			{
				CredentialInfo credInfo = null;
				switch (cred.getType())
				{
					case PTOCredential.TYPE_PASSWORD:
						credInfo = new CredentialInfo(
								CredentialType.MAIL_ADDRESS);
						credInfo.setMailAddress(cred.getUsername());
						credInfoList.add(credInfo);
						break;
					case PTOCredential.TYPE_EXTERNAL:
						credInfo = new CredentialInfo(
								CredentialType.EXTERNAL_UID);
						ExternalUIDType type = ExternalUIDType.resolve(
								cred.getUsername());
						credInfo.setExternalUID(type.parse(cred.getUsername()));
						credInfo.setExternalUIDType(type);
						credInfoList.add(credInfo);
						break;
				}
			}
			return credInfoList;
		}
		catch (PTORealmManagerException | PTORealmConnectionException
				exception)
		{
			throw new MembershipException(exception.getMessage());
		}
		catch (IOException exception)
		{
			throw new EJBException(exception);
		}
	}
	
	@Override
	public CredentialRequestInfo addCredential(CredentialRequest credReq)
	throws MembershipException
	{
		try
		{
			return addCredential(getCallerPrincipal(), credReq);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}
	
	@Override
	public CredentialRequestInfo addCredential(PTOPrincipal principal,
			CredentialRequest credReq)
	throws MembershipException, NoSuchMemberException
	{
		return approveCredential(principal, credReq);
	}
	
	@Override
	public void removeCredential(int index)
	throws MembershipException
	{
		try
		{
			removeCredential(getCallerPrincipal(), index);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}
	
	@Override
	public void removeCredential(PTOPrincipal principal, int index)
	throws MembershipException, NoSuchMemberException
	{
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			managedPrincipal.getCredentials().remove(index);
		}
		catch (PTORealmManagerException | PTORealmConnectionException
				exception)
		{
			throw new MembershipException(exception.getMessage());
		}
		catch (IOException exception)
		{
			throw new EJBException(exception);
		}
	}
	
	@Override
	public MemberInfo fetchMemberInfo()
	throws MembershipException
	{
		try
		{
			return fetchMemberInfo(getCallerPrincipal());
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public MemberInfo fetchMemberInfo(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setName(member.getName());
		memberInfo.setSurname(member.getSurname());
		return memberInfo;
	}
	
	@Override
	public MemberAdded addMember(MemberInfo memberInfo,
			CredentialRequest credReq)
	throws MembershipException
	{
		try
		{
			Member member = new Member();
			member.setName(memberInfo.getName());
			member.setSurname(memberInfo.getSurname());
			em.persist(member);
			em.flush();
			
			PTOPrincipal principal = new PTOPrincipal(member.getUID());
			MemberAdded added = new MemberAdded();
			added.setPrincipal(principal);
			added.setRequestInfo(approveCredential(principal, credReq));
			return added;
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}
	
	@Override
	public void updateMember(MemberInfo memberInfo)
	throws MembershipException
	{
		try
		{
			updateMember(getCallerPrincipal(), memberInfo);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public void updateMember(PTOPrincipal principal, MemberInfo memberInfo)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		member.setName(memberInfo.getName());
		member.setSurname(memberInfo.getSurname());
	}
	
	@Override
	public void removeMember()
	throws MembershipException
	{
		try
		{
			removeMember(getCallerPrincipal());
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}
	
	@Override
	public void removeMember(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		checkMemberExist(principal);
		
		Member member = new Member();
		member.setUID(principal.getUserID());
		em.remove(member);
	}
	
	@Override
	public Map<ProfileRequestType, List<ProfileRequestHints>>
			fetchProfileRequestHints(ProfileType type)
	throws MembershipException
	{
		try
		{
			Map<ProfileRequestType, List<ProfileRequestHints>> hintsMap =
					new HashMap<>();
			for (Accreditator accreditator : accreditators())
			{
				ProfileRequestType implementedType =
						accreditator.getImplementedType();
				List<ProfileRequestHints> hintsList = hintsMap.get(
						implementedType);
				if (hintsList == null)
				{
					hintsList = new ArrayList<>();
					hintsMap.put(implementedType, hintsList);
				}
				hintsList.addAll(accreditator.requestHints(type));
			}
			return hintsMap;
		}
		catch (AccreditatorException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
	}
	
	@Override
	public ProfileType[] availableProfileTypes()
	throws MembershipException
	{
		try
		{
			return availableProfileTypes(getCallerPrincipal());
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public ProfileType[] availableProfileTypes(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		
		Set<ProfileType> givenProfileSet = new HashSet<>();
		for (Profile profile : member.getProfiles())
			givenProfileSet.add(getProfileType(profile));
		
		List<ProfileType> result = new ArrayList<>();
		for (ProfileType profileType : ProfileType.values())
			if (!givenProfileSet.contains(profileType))
				result.add(profileType);
		return result.toArray(new ProfileType[result.size()]);
	}
	
	@Override
	public Set<ProfileInfo> fetchProfiles()
	throws MembershipException
	{
		try
		{
			return fetchProfiles(getCallerPrincipal());
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public Set<ProfileInfo> fetchProfiles(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		Set<ProfileInfo> profileInfos = new HashSet<>();
		for (Profile profile : member.getProfiles())
		{
			ProfileInfo profileInfo = new ProfileInfo();
			profileInfo.setType(getProfileType(profile));
			profileInfos.add(profileInfo);
		}
		return profileInfos;
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ProfileRequestResult requestProfile(ProfileRequestInfo requestInfo)
	throws MembershipException
	{
		try
		{
			return requestProfile(getCallerPrincipal(), requestInfo);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public ProfileRequestResult requestProfile(PTOPrincipal principal,
			ProfileRequestInfo requestInfo)
	throws MembershipException, NoSuchMemberException
	{
		try
		{
			Accreditator accreditator = accreditatorLookup(
					requestInfo.getRequestType(), requestInfo.getType());
			return accreditator.request(principal, requestInfo);
		}
		catch (AccreditatorException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
	}
	
	@Override
	public void removeProfile(ProfileType profileType)
	throws MembershipException
	{
		try
		{
			removeProfile(getCallerPrincipal(), profileType);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@Override
	public void removeProfile(PTOPrincipal principal, ProfileType profileType)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Util.findMember(em, principal);
		member.removeProfile(getReverseProfileType(profileType));
	}
	
	private PTOPrincipal getCallerPrincipal()
	throws MembershipException
	{
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			
			Principal principal = context.getCallerPrincipal();
			return rm.resolve(principal.getName());
		}
		catch (IOException exception)
		{
			throw new EJBException(exception);
		}
		catch (PTORealmManagerException | PTORealmConnectionException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
	}
	
	private ProfileType getProfileType(Profile profile)
	{
		switch (String.valueOf(profile.getType()))
		{
			case Profile.TYPE_CANDIDATE:
			return ProfileType.CANDIDATE;
			
			case Profile.TYPE_ES_JUDGE:
			return ProfileType.SPAIN_JUDGE;
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Profile ").append(profile);
			msg.append(" has an unknown type");
			throw new IllegalStateException(msg.toString());
		}
	}
	
	private int getReverseProfileType(ProfileType profileType)
	{
		switch (profileType)
		{
			case CANDIDATE:
			return Integer.parseInt(Profile.TYPE_CANDIDATE);
			
			case SPAIN_JUDGE:
			return Integer.parseInt(Profile.TYPE_ES_JUDGE);
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Profile type ").append(profileType);
			msg.append(" is not implemented");
			throw new IllegalStateException(msg.toString());
		}
	}
	
	private void checkMemberExist(PTOPrincipal principal)
	throws NoSuchMemberException
	{
		long memberID = principal.getUserID();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Member> root = cq.from(Member.class);
		cq.select(cb.count(root));
		cq.where(cb.equal(root.get(Member_.UID), memberID));
		TypedQuery<Long> q = em.createQuery(cq);
		Long result = q.getSingleResult();
		
		if (result == null || result <= 0l)
			throw new NoSuchMemberException(memberID);
	}
	
	private boolean credentialRequestMailExist(String mailAddr)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<RequestCredentialMail> root = cq.from(RequestCredentialMail.class);
		cq.select(cb.count(root));
		cq.where(cb.equal(root.get(RequestMemberMail_.mailAddress), mailAddr));
		TypedQuery<Long> q = em.createQuery(cq);
		Long result = q.getSingleResult();
		return result != null && result > 0l;
	}
	
	private Iterable<Accreditator> accreditators()
	{
		return Arrays.asList(
			accredAutomatic,
			accredHumanContact,
			accredMailDomainMatch
		);
	}
	
	private Accreditator accreditatorLookup(ProfileRequestType requestType,
			ProfileType type)
	throws MembershipException
	{
		try
		{
			for (Accreditator accreditator : accreditators())
				if (requestType.equals(accreditator.getImplementedType()) &&
						!accreditator.requestHints(type).isEmpty())
					return accreditator;
			
			StringBuilder msg = new StringBuilder();
			msg.append("Unsupported request type ").append(requestType);
			msg.append(" for profile of type ").append(type);
			throw new MembershipException(msg.toString());
		}
		catch (AccreditatorException exception)
		{
			throw new MembershipException(exception.toString());
		}
	}
	
	private ExternalUIDConnection getExternalUIDConnection(
			ExternalUIDType externalUIDType)
	throws MembershipException
	{
		switch (externalUIDType)
		{
			case GOOGLE:
			return extUIDGoogleFact.getConnection();
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Unsupported external UID type ");
			msg.append(externalUIDType);
			throw new MembershipException(msg.toString());
		}
	}
	
	private CredentialRequestResult requestCredentialMail(
			CredentialRequestInfo requestInfo)
	throws MembershipException
	{
		try
		{
			String mailAddress = requestInfo.getMailAddress();
			String normMailAddress = normalizeMailAddress(mailAddress);
			if (credentialRequestMailExist(normMailAddress))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("Membership request was already done for mail ");
				sb.append("address ").append(normMailAddress);
				throw new MembershipException(sb.toString());
			}
			
			RequestCredentialMail req = new RequestCredentialMail();
			req.setMailAddress(normMailAddress);
			req.setPassword(passwordHash(
					requestInfo.getPassword().toCharArray()));
			req.setToken(Util.createToken());
			em.persist(req);
		
			NotifierMessage message = new NotifierMessage();
			message.setTarget(mailAddress);
			message.setSubjectText("Prepara tus Opos registration");
			
			String tokenRef = requestInfo.getTokenRef();
			String tokenHex = DatatypeConverter.printHexBinary(req.getToken());
			StringBuilder text = new StringBuilder();
			text.append("Welcome to Prepara tus Opos. Use this link to ");
			text.append("activate your account:\n");
			text.append(tokenRef.replace("${token}", tokenHex));
			message.setBodyText(text.toString());
			
			mailNotifier.messageSend(message);
			return new CredentialRequestResult();
		}
		catch (NotifierException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Could not send token message to ");
			msg.append(requestInfo.getMailAddress());
			
			Logger logger = Util.getLogger();
			logger.log(Level.SEVERE, msg.toString(), exception);
			
			throw new MembershipException(msg.toString());
		}
	}
	
	private CredentialRequestResult requestCredentialExternalUID(
			CredentialRequestInfo requestInfo)
	throws MembershipException
	{
		ExternalUIDType externalUIDType = requestInfo.getExternalUIDType();
		
		CredentialRequest req = new CredentialRequest(
				CredentialType.EXTERNAL_UID);
		req.setExternalUIDType(externalUIDType);
		req.setExternalUserID(fetchExternalUID(externalUIDType,
				requestInfo.getAuthorizationCode()));
		CredentialRequestResult result = new CredentialRequestResult();
		result.setRequest(req);
		return result;
	}
	
	private ExtractedCredential extractCredentialMail(CredentialRequest request)
	throws NoSuchCredentialRequestException
	{
		try
		{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<RequestCredentialMail> cq = cb.createQuery(
					RequestCredentialMail.class);
			Root<RequestCredentialMail> root = cq.from(
					RequestCredentialMail.class);
			cq.where(cb.equal(root.get(RequestMemberMail_.token),
					DatatypeConverter.parseHexBinary(request.getTokenHex())));
			TypedQuery<RequestCredentialMail> q = em.createQuery(cq);
			RequestCredentialMail req = q.getSingleResult();
			
			CredentialRequestInfo requestInfo = new CredentialRequestInfo(
					CredentialType.MAIL_ADDRESS);
			requestInfo.setMailAddress(req.getMailAddress());
			
			em.remove(req);
			
			char[] password = toCharArray(request.getPassword());
			if (password == null)
				password = toCharArray(req.getPassword());
			
			PTOCredentialFactory credentialFact =
					PTORealmBootstrap.createCredentialFactory();
			ExtractedCredential extracted = new ExtractedCredential();
			extracted.setRequestInfo(requestInfo);
			extracted.setCredential(credentialFact.newPassword(
					req.getMailAddress(), password));
			return extracted;
		}
		catch (NoResultException exception)
		{
			throw new NoSuchCredentialRequestException(request.getTokenHex());
		}
	}
	
	private ExtractedCredential extractCredentialExternalUID(
			CredentialRequest request)
	throws NoSuchCredentialRequestException
	{
		CredentialRequestInfo requestInfo = new CredentialRequestInfo(
				CredentialType.EXTERNAL_UID);
		requestInfo.setExternalUIDType(request.getExternalUIDType());
		
		PTOCredentialFactory credentialFact =
				PTORealmBootstrap.createCredentialFactory();
		ExtractedCredential extracted = new ExtractedCredential();
		extracted.setRequestInfo(requestInfo);
		extracted.setCredential(credentialFact.newExternal(
				request.getExternalUserID()));
		return extracted;
	}
	
	private CredentialRequestInfo approveCredential(PTOPrincipal principal,
			CredentialRequest credReq)
	throws MembershipException, NoSuchMemberException
	{
		checkMemberExist(principal);
		
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			
			ExtractedCredential extracted = null;
			switch (credReq.getType())
			{
				case MAIL_ADDRESS:
				extracted = extractCredentialMail(credReq);
				break;
				
				case EXTERNAL_UID:
				extracted = extractCredentialExternalUID(credReq);
				break;
			}
			
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			managedPrincipal.getCredentials().add(extracted.getCredential());
			return extracted.getRequestInfo();
		}
		catch (PTORealmManagerException | PTORealmConnectionException |
				PTOCredentialDuplicatedException |
				NoSuchCredentialRequestException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
		catch (IOException exception)
		{
			throw new EJBException(exception);
		}
	}
	
	private byte[] passwordHash(char[] password)
	{
		try
		{
			byte[] bytes = String.valueOf(password).getBytes(PASSWORD_CHARSET);
			return getPasswordDigest().digest(bytes);
		}
		catch (NoSuchAlgorithmException exception)
		{
			throw new EJBException(exception);
		}
	}
	
	private static MessageDigest getPasswordDigest()
	throws NoSuchAlgorithmException
	{
		if (passwordDigest == null)
			passwordDigest = MessageDigest.getInstance(PASSWORD_DIGEST_ALG);
		return passwordDigest;
	}
	
	private static char[] toCharArray(String str)
	{
		return str == null ? null : str.toCharArray();
	}
	
	private static char[] toCharArray(byte[] bytes)
	{
		return new String(bytes, PASSWORD_CHARSET).toCharArray();
	}
	
	private static IllegalStateException newNoSuchExistingMemberException(
			NoSuchMemberException exception)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Member with UID ").append(exception.getMemberID());
		msg.append(" should be an existing member");
		return new IllegalStateException(msg.toString());
	}
	
	private static class ExtractedCredential
	{
		private CredentialRequestInfo requestInfo;
		private PTOCredential credential;
		
		public ExtractedCredential()
		{
			requestInfo = null;
			credential = null;
		}

		public CredentialRequestInfo getRequestInfo()
		{
			return requestInfo;
		}

		public void setRequestInfo(CredentialRequestInfo requestInfo)
		{
			this.requestInfo = requestInfo;
		}

		public PTOCredential getCredential()
		{
			return credential;
		}

		public void setCredential(PTOCredential credential)
		{
			this.credential = credential;
		}
	}
}

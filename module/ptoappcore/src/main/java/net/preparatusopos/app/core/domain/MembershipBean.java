package net.preparatusopos.app.core.domain;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimeType;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import net.preparatusopos.app.core.domain.model.AuthorizationToken;
import net.preparatusopos.app.core.domain.model.ManagedFile;
import net.preparatusopos.app.core.domain.model.Member;
import net.preparatusopos.app.core.domain.model.Profile;
import net.preparatusopos.app.core.domain.model.RequestCredential;
import net.preparatusopos.app.core.domain.model.RequestCredentialMailAddress;
import net.preparatusopos.app.core.domain.model.RequestDirectProfilesToken;
import net.preparatusopos.app.core.domain.model.RequestProfile;
import net.preparatusopos.app.core.file.PrivateFileStorage;
import net.preparatusopos.app.core.util.Util;
import net.preparatusopos.app.domain.CredentialInfo;
import net.preparatusopos.app.domain.CredentialRequest;
import net.preparatusopos.app.domain.DirectProfilesInfo;
import net.preparatusopos.app.domain.DirectProfilesRequest;
import net.preparatusopos.app.domain.ExternalCredentialInfo;
import net.preparatusopos.app.domain.ExternalUID;
import net.preparatusopos.app.domain.ExternalUIDDomain;
import net.preparatusopos.app.domain.MailCredentialInfo;
import net.preparatusopos.app.domain.MailCredentialRequest;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.MemberRegistered;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileInfo;
import net.preparatusopos.app.domain.ProfileRequest;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.ReferencedMemberInfo;
import net.preparatusopos.app.domain.Token;
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
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;
import net.preparatusopos.tools.file.StoredFileSpecs;

@Remote(
	Membership.class
)
@Stateless(
	name="ejb/MembershipBean"
)
@DeclareRoles({
	"ADMIN",
	"MEMBER"
})
public class MembershipBean
implements Membership
{
	@Resource(
		lookup="extuid/PTOGoogleExternalID"
	)
	private ExternalUIDConnectionFactory extUIDGoogleFact;
	
	@Resource(
		lookup="jdbc/PTORealmDataSource"
	)
	private DataSource realmDataSource;
	
	@EJB(
		lookup="ejb/AccreditatorMailBean"
	)
	private Accreditator accreditatorMail;
	
	@EJB(
		lookup="ejb/PrivateFileStorageBean"
	)
	private PrivateFileStorage fileStorage;
	
	@Resource
	private EJBContext context;
	
	@PersistenceContext(
		unitName="PTOApplicationUnit"
	)
	private EntityManager em;
	
	public MembershipBean()
	{
		extUIDGoogleFact = null;
		realmDataSource = null;
		accreditatorMail = null;
		context = null;
		em = null;
	}
	
	@PermitAll
	@Override
	public Set<ProfileType.Specialization> availableSpecializations(
			ProfileType.Country country, ProfileType.Field field)
	throws MembershipException
	{
		Set<ProfileType.Specialization> specializations = new HashSet<>();
		switch (field)
		{
			case JUDICATURE:
			specializations.add(ProfileType.Specialization.COMMERCIAL);
			break;
			
			default:
			// No thing to be done
		}
		return specializations;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public String fetchExternalUID(ExternalUIDDomain domain, String
			authorizationCode)
	throws MembershipException
	{
		try (ExternalUIDConnection conn = getExternalUIDConnection(domain))
		{
			return domain.format(conn.getExternalUID(authorizationCode));
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
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public Set<CredentialInfo> fetchCredentials()
	throws MembershipException
	{
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTOPrincipal principal = getCallerPrincipal();
			
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			
			Set<CredentialInfo> credInfoList = new HashSet<>();
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			for (PTOCredential cred : managedPrincipal.getCredentials())
			{
				switch (cred.getType())
				{
					case PTOCredential.TYPE_PASSWORD:
						MailCredentialInfo mailCredentialInfo =
								new MailCredentialInfo();
						mailCredentialInfo.setMailAddress(cred.getUsername());
						credInfoList.add(mailCredentialInfo);
						break;
					case PTOCredential.TYPE_EXTERNAL:
						ExternalCredentialInfo externalCredentialInfo =
								new ExternalCredentialInfo();
						externalCredentialInfo.setExternalUID(
								ExternalUID.parse(cred.getUsername()));
						credInfoList.add(externalCredentialInfo);
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
	
	@PermitAll
	@Override
	public Map<ProfileRequestType, List<ProfileRequestHints>>
			fetchProfileRequestHints(ProfileType type)
	throws MembershipException
	{
		try
		{
			Map<ProfileRequestType, List<ProfileRequestHints>> hintsMap =
					new HashMap<ProfileRequestType,
					List<ProfileRequestHints>>();
			for (ProfileRequestType requestType : ProfileRequestType.values())
			{
				List<ProfileRequestHints> hintsList = new ArrayList<>();
				for (Accreditator accreditator : accreditators())
					hintsList.addAll(accreditator.fetchProfileRequestHints(type,
							requestType));
				hintsMap.put(requestType, hintsList);
			}
			return hintsMap;
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	@RolesAllowed({
		"MEMBER"
	})
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
	
	@RolesAllowed({
		"ADMIN"
	})
	@Override
	public Set<ProfileInfo> fetchProfiles(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Member.find(em, principal);
		Set<ProfileInfo> profiles = new HashSet<>();
		for (Profile profile : member.getProfiles())
		{
			ProfileInfo profileInfo = new ProfileInfo(profile.getType());
			profiles.add(profileInfo);
		}
		return profiles;
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public StoredFile storeMemberPhoto(long size, MimeType contentType)
	throws MembershipException, FileStorageException
	{
		try
		{
			return storeMemberPhoto(getCallerPrincipal(), size, contentType);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@RolesAllowed({
		"ADMIN"
	})
	@Override
	public StoredFile storeMemberPhoto(PTOPrincipal principal,
			long size, MimeType contentType)
	throws MembershipException, NoSuchMemberException, FileStorageException
	{
		Member member = Member.find(em, principal);
		ManagedFile photoFile = member.getPhotoFile();
		
		if (photoFile != null)
		{
			String providerName = fileStorage.getProviderName(
					photoFile.transport());
			fileStorage.remove(photoFile.getStoredFile(providerName));
		}
		
		StringBuilder name = new StringBuilder();
		name.append("member-photo");
		name.append("-").append(member.getRef());
		name.append("-").append(Util.createRef(20));
		
		photoFile = fileStorage.store(StoredFileSpecs.builder()
			.setName(name.toString())
			.setSize(size)
			.setContentType(contentType)
			.build()).restore();
		member.setPhotoFile(photoFile);
		
		String providerName = fileStorage.getProviderName(
				photoFile.transport());
		return photoFile.getStoredFile(providerName);
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public ReferencedMemberInfo fetchMemberInfo()
	throws MembershipException, FileStorageException
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
	
	@RolesAllowed({
		"ADMIN"
	})
	@Override
	public ReferencedMemberInfo fetchMemberInfo(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException, FileStorageException
	{
		Member member = Member.find(em, principal);
		return member.getMemberInfo(fileStorage);
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public void updateMemberInfo(MemberInfo memberInfo)
	throws MembershipException
	{
		try
		{
			updateMemberInfo(getCallerPrincipal(), memberInfo);
		}
		catch (NoSuchMemberException exception)
		{
			throw newNoSuchExistingMemberException(exception);
		}
	}

	@RolesAllowed({
		"ADMIN"
	})
	@Override
	public void updateMemberInfo(PTOPrincipal principal, MemberInfo memberInfo)
	throws MembershipException, NoSuchMemberException
	{
		Member member = Member.find(em, principal);
		member.setMemberInfo(memberInfo);
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public Token request(String tokenRef, CredentialRequest request)
	throws MembershipException
	{
		try
		{
			return accreditatorLookup(request).request(tokenRef, request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	@RolesAllowed({
		"MEMBER"
	})
	@Override
	public Token request(String tokenRef, ProfileRequest request)
	throws MembershipException
	{
		try
		{
			return accreditatorLookup(request).request(tokenRef, request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	@PermitAll
	@Override
	public Token request(String tokenRef, DirectProfilesRequest request)
	throws MembershipException
	{
		try
		{
			return accreditatorLookup(request).request(tokenRef, request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	@Override
	public CredentialRequest approve(Token token, CredentialInfo info)
	throws MembershipException
	{
		// TODO ...
		return null;
	}
	
	@Override
	public ProfileRequest approve(Token token, ProfileInfo info)
	throws MembershipException
	{
		// TODO ...
		return null;
	}
	
	@Override
	public MemberRegistered register(Token token, DirectProfilesInfo
			directProfilesInfo, MemberInfo memberInfo)
	throws MembershipException
	{
		RequestDirectProfilesToken authToken = AuthorizationToken.find(em,
				RequestDirectProfilesToken.class, token);
		
		Member member = Member.create();
		member.setName(memberInfo.getName());
		member.setSurname(memberInfo.getSurname());
		em.persist(member);
		em.flush();
		
		PTOPrincipal principal = new PTOPrincipal(member.getUID());
		MemberRegistered registered = new MemberRegistered();
		registered.setPrincipal(principal);
		registered.setCredentialRequest(addCredential(principal,
				authToken.getCredential(),
				directProfilesInfo.getCredentialInfo()));
		
		for (RequestProfile reqProf : authToken.getProfiles())
		{
			ProfileInfo profileInfo = findProfileInfo(reqProf.getType(),
					directProfilesInfo.getValues());
			if (profileInfo == null)
				profileInfo = new ProfileInfo(reqProf.getType());
			member.addProfile(reqProf, profileInfo);
		}
		
		em.remove(authToken);
		return registered;
	}
	
	@Override
	public void unregister()
	throws MembershipException
	{
		// TODO ...
	}
	
	@Override
	public void removeCredential(int index)
	throws MembershipException
	{
		// TODO ...
	}
	
	@Override
	public void removeMember(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException
	{
		// TODO ...
	}
	
	@Override
	public void removeProfile(ProfileType type)
	throws MembershipException
	{
		// TODO ...
	}
	
	@Override
	public void removeProfile(PTOPrincipal principal, ProfileType type)
	throws MembershipException, NoSuchMemberException
	{
		// TODO ...
	}
	
	private Iterable<Accreditator> accreditators()
	{
		return Arrays.asList(
			accreditatorMail
		);
	}
	
	private Accreditator accreditatorLookup(CredentialRequest request)
	throws MembershipException
	{
		try
		{
			for (Accreditator accreditator : accreditators())
				if (accreditator.supports(request))
					return accreditator;
			throw newUnsuppotedRequest(request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	private Accreditator accreditatorLookup(ProfileRequest request)
	throws MembershipException
	{
		try
		{
			for (Accreditator accreditator : accreditators())
				if (accreditator.supports(request))
					return accreditator;
			throw newUnsuppotedRequest(request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	private Accreditator accreditatorLookup(DirectProfilesRequest request)
	throws MembershipException
	{
		try
		{
			for (Accreditator accreditator : accreditators())
				if (accreditator.supports(request))
					return accreditator;
			throw newUnsuppotedRequest(request);
		}
		catch (AccreditatorException exception)
		{
			throw newMembershipException(exception);
		}
	}
	
	private ExternalUIDConnection getExternalUIDConnection(
			ExternalUIDDomain domain)
	throws MembershipException
	{
		switch (domain)
		{
			case GOOGLE:
			return extUIDGoogleFact.getConnection();
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Unsupported external UID domain ");
			msg.append(domain);
			throw new MembershipException(msg.toString());
		}
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
	
	private ProfileInfo findProfileInfo(ProfileType type, Set<ProfileInfo>
			profiles)
	{
		for (ProfileInfo info : profiles)
			if (type.equals(info.getType()))
				return info;
		return null;
	}
	
	private CredentialRequest addCredential(PTOPrincipal principal,
			RequestCredential reqCred, CredentialInfo credentialInfo)
	throws MembershipException
	{
		if (reqCred instanceof RequestCredentialMailAddress)
		{
			if (credentialInfo instanceof MailCredentialInfo)
				return addCredential(principal,
						(RequestCredentialMailAddress) reqCred,
						(MailCredentialInfo) credentialInfo);
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported credential addition with request of type ");
		msg.append(reqCred.getClass());
		msg.append(" and info of type ");
		msg.append(credentialInfo.getClass());
		throw new UnsupportedOperationException(msg.toString());
	}
	
	private MailCredentialRequest addCredential(PTOPrincipal principal,
			RequestCredentialMailAddress reqCred, MailCredentialInfo
			credentialInfo)
	throws MembershipException
	{
		try (PTORealmConnection conn =
				Util.getRealmConnection(realmDataSource))
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(conn);
			PTOCredentialFactory credFact =
					PTORealmBootstrap.createCredentialFactory();
			
			MailCredentialRequest req = new MailCredentialRequest();
			req.setMailAddress(reqCred.getMailAddress());
			req.setPassword(reqCred.getPassword());
			
			if (credentialInfo.getMailAddress() != null)
				req.setMailAddress(credentialInfo.getMailAddress());
			if (credentialInfo.getPassword() != null)
				req.setPassword(credentialInfo.getPassword());
			
			PTOManagedPrincipal mp = rm.manage(principal);
			mp.getCredentials().add(credFact.newPassword(req.getMailAddress(),
					new String(req.getPassword()).toCharArray()));
			return req;
		}
		catch (IOException exception)
		{
			throw new EJBException(exception);
		}
		catch (PTORealmManagerException | PTORealmConnectionException
				| PTOCredentialDuplicatedException exception)
		{
			throw new MembershipException(exception.getMessage());
		}
	}
	
	private static IllegalStateException newNoSuchExistingMemberException(
			NoSuchMemberException exception)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Member with UID ").append(exception.getMemberID());
		msg.append(" should be an existing member");
		return new IllegalStateException(msg.toString());
	}
	
	private static MembershipException newUnsuppotedRequest(Object request)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported request ").append(request);
		return new MembershipException(msg.toString());
	}
	
	private static MembershipException newMembershipException(Exception
			exception)
	{
		return new MembershipException(exception.getMessage());
	}
}

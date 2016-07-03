package net.preparatusopos.app.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimeType;

import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

/**
 * Membership contracts.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public interface Membership
{
	public Set<ProfileType.Specialization> availableSpecializations(
			ProfileType.Country country, ProfileType.Field field)
	throws MembershipException;
	
	public String fetchExternalUID(ExternalUIDDomain domain, String
			authorizationCode)
	throws MembershipException;
	
	public Set<CredentialInfo> fetchCredentials()
	throws MembershipException;
	
	public Map<ProfileRequestType, List<ProfileRequestHints>>
			fetchProfileRequestHints(ProfileType type)
	throws MembershipException;
	
	public Set<ProfileInfo> fetchProfiles()
	throws MembershipException;
	
	public Set<ProfileInfo> fetchProfiles(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	public StoredFile storeMemberPhoto(long size, MimeType contentType)
	throws MembershipException, FileStorageException;
	
	public StoredFile storeMemberPhoto(PTOPrincipal principal,
			long size, MimeType contentType)
	throws MembershipException, NoSuchMemberException, FileStorageException;
	
	public ReferencedMemberInfo fetchMemberInfo()
	throws MembershipException, FileStorageException;
	
	public ReferencedMemberInfo fetchMemberInfo(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException, FileStorageException;
	
	public void updateMemberInfo(MemberInfo memberInfo)
	throws MembershipException;
	
	public void updateMemberInfo(PTOPrincipal principal, MemberInfo memberInfo)
	throws MembershipException, NoSuchMemberException;
	
	public Token request(String tokenRef, CredentialRequest request)
	throws MembershipException;
	
	public Token request(String tokenRef, ProfileRequest request)
	throws MembershipException;
	
	public Token request(String tokenRef, DirectProfilesRequest request)
	throws MembershipException;
	
	public CredentialRequest approve(Token token, CredentialInfo info)
	throws MembershipException;
	
	public ProfileRequest approve(Token token, ProfileInfo info)
	throws MembershipException;
	
	public MemberRegistered register(Token token, DirectProfilesInfo
			directProfilesInfo, MemberInfo memberInfo)
	throws MembershipException;
	
	public void unregister()
	throws MembershipException;
	
	public void removeCredential(int index)
	throws MembershipException;
	
	public void removeMember(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	public void removeProfile(ProfileType type)
	throws MembershipException;
	
	public void removeProfile(PTOPrincipal principal, ProfileType type)
	throws MembershipException, NoSuchMemberException;
}

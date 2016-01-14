package net.preparatusopos.app.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.preparatusopos.security.auth.PTOPrincipal;

/**
 * Membership contracts.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public interface Membership
{
	/**
	 * Fetch a normalized mail address.
	 * 
	 * @param mailAddr
	 * 			Any valid mail address.
	 * 
	 * @return
	 * 			The normalized mail address.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred during normalization.
	 */
	public String normalizeMailAddress(String mailAddr)
	throws MembershipException;
	
	/**
	 * Fetch a hashed password.
	 * 
	 * @param password
	 * 			Password to be hashed.
	 * 
	 * @return
	 * 			The hashed password.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred hashing password.
	 */
	public String passwordHash(String password)
	throws MembershipException;
	
	/**
	 * Fetch an external user ID with the given authorization.
	 * 
	 * @param type
	 * 			Type of external user ID system.
	 * @param authorizationCode
	 * 			Authorization code for the external UID system.
	 * 
	 * @return
	 * 			The external user ID.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public String fetchExternalUID(ExternalUIDType type,
			String authorizationCode)
	throws MembershipException;
	
	/**
	 * Request for a credential.
	 * 
	 * <p>
	 * The resolved credential request is available immediately if it does not
	 * requires two phase validation.
	 * </p>
	 * 
	 * @param requestInfo
	 * 			Credential request info.
	 * 
	 * @return
	 * 			The result of request.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public CredentialRequestResult requestCredential(
			CredentialRequestInfo requestInfo)
	throws MembershipException;
	
	/**
	 * Fetch credentials info of the caller member.
	 * 
	 * @return
	 * 			The member credentials info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public List<CredentialInfo> fetchCredentials()
	throws MembershipException;
	
	/**
	 * Fetch credentials info of the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * 
	 * @return
	 * 			The member credentials info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public List<CredentialInfo> fetchCredentials(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Add a credential for the caller member according the given request.
	 * 
	 * @param credReq
	 * 			Credential request.
	 * 
	 * @return
	 * 			Info of the given credential request.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public CredentialRequestInfo addCredential(CredentialRequest credReq)
	throws MembershipException;
	
	/**
	 * Add a credential for the member with the given principal according the
	 * given request.
	 * 
	 * @param principal
	 * 			Member principal.
	 * @param credReq
	 * 			Credential request.
	 * 
	 * @return
	 * 			Info of the given credential request.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public CredentialRequestInfo addCredential(PTOPrincipal principal,
			CredentialRequest credReq)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Remove credential of the caller member at the specified index.
	 * 
	 * @param index
	 * 			Credential index.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public void removeCredential(int index)
	throws MembershipException;
	
	/**
	 * Remove credential of the member with the given principal at the
	 * specified index.
	 * 
	 * @param principal
	 * 			Member principal.
	 * @param index
	 * 			Credential index.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public void removeCredential(PTOPrincipal principal, int index)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Fetch caller member info.
	 * 
	 * @return
	 * 			The caller member info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public MemberInfo fetchMemberInfo()
	throws MembershipException;
	
	/**
	 * Fetch info of the member member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * 
	 * @return
	 * 			The caller member info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public MemberInfo fetchMemberInfo(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Add a new member by using the given credential request to generate the
	 * first credential.
	 * 
	 * @param memberInfo
	 * 			Member info.
	 * @param credReq
	 * 			First credential request.
	 * 
	 * @return
	 * 			New member info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public MemberAdded addMember(MemberInfo memberInfo,
			CredentialRequest credReq)
	throws MembershipException;
	
	/**
	 * Update caller member info.
	 * 
	 * @param memberInfo
	 * 			The new member info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public void updateMember(MemberInfo memberInfo)
	throws MembershipException;
	
	/**
	 * Update info of the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * @param memberInfo
	 * 			The new member info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public void updateMember(PTOPrincipal principal, MemberInfo memberInfo)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Remove the caller member.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public void removeMember()
	throws MembershipException;
	
	/**
	 * Remove the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public void removeMember(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Fetch hints for a profile request of the given type.
	 * 
	 * @param type
	 * 			Type of profile.
	 * 
	 * @return
	 * 			Request hints.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public Map<ProfileRequestType, List<ProfileRequestHints>>
			fetchProfileRequestHints(ProfileType type)
	throws MembershipException;
	
	/**
	 * Fetch available profile types for the caller member.
	 * 
	 * @return
	 * 			Array of available types.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public ProfileType[] availableProfileTypes()
	throws MembershipException;
	
	/**
	 * Fetch available profile types for the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * 
	 * @return
	 * 			Array of available types.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public ProfileType[] availableProfileTypes(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Fetch profiles info of the caller member.
	 * 
	 * @return
	 * 			The member profiles info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public Set<ProfileInfo> fetchProfiles()
	throws MembershipException;
	
	/**
	 * Fetch profiles info of the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * 
	 * @return
	 * 			The member profiles info.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public Set<ProfileInfo> fetchProfiles(PTOPrincipal principal)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Does a profile request for the caller member.
	 * 
	 * @param requestInfo
	 * 			Info of profile request.
	 * 
	 * @return
	 * 			The result of profile request operation.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public ProfileRequestResult requestProfile(ProfileRequestInfo requestInfo)
	throws MembershipException;
	
	/**
	 * Does a profile request for the member with the given principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * @param requestInfo
	 * 			Info of profile request.
	 * 
	 * @return
	 * 			The result of profile request operation.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public ProfileRequestResult requestProfile(PTOPrincipal principal,
			ProfileRequestInfo requestInfo)
	throws MembershipException, NoSuchMemberException;
	
	/**
	 * Remove profile of the specified type for the caller member.
	 * 
	 * @param profileType
	 * 			Profile type.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 */
	public void removeProfile(ProfileType profileType)
	throws MembershipException;
	
	/**
	 * Remove profile of the specified type for the member with the given
	 * principal.
	 * 
	 * @param principal
	 * 			Member principal.
	 * @param profileType
	 * 			Profile type.
	 * 
	 * @throws MembershipException
	 * 			If some error has been occurred.
	 * @throws NoSuchMemberException
	 * 			If member does not exist.
	 */
	public void removeProfile(PTOPrincipal principal, ProfileType profileType)
	throws MembershipException, NoSuchMemberException;
}

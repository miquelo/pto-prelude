package net.preparatusopos.security.auth;

import java.util.Set;

public interface PTOManagedPrincipal
{
	public Set<String> getGroups()
	throws PTORealmManagerException;
	
	public PTOCredentialList getCredentials()
	throws PTORealmManagerException;
}

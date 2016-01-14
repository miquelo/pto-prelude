package net.preparatusopos.security.auth;

public interface PTORealmManager
{
	public PTOPrincipal resolve(String name)
	throws PTORealmManagerException;
	
	public PTOManagedPrincipal manage(PTOPrincipal principal)
	throws PTORealmManagerException;
	
	public PTOPrincipal authenticate(PTOCredential credential)
	throws PTORealmManagerException;
}

package net.preparatusopos.security.auth.spi;

import java.io.Closeable;
import java.util.List;
import java.util.Set;

import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOCredentialDuplicatedException;
import net.preparatusopos.security.auth.PTOPrincipal;

public interface PTORealmConnection
extends Closeable
{
	public PTOPrincipal resolve(String name)
	throws PTORealmConnectionException;
	
	public Set<String> getGroups(PTOPrincipal principal)
	throws PTORealmConnectionException;
	
	public List<PTOCredential> getCredentials(PTOPrincipal principal)
	throws PTORealmConnectionException;
	
	public void addCredential(PTOPrincipal principal, PTOCredential credential)
	throws PTORealmConnectionException, PTOCredentialDuplicatedException;
	
	public void removeCredential(PTOPrincipal principal, int index)
	throws PTORealmConnectionException;
	
	public PTOPrincipal matching(PTOCredential credential)
	throws PTORealmConnectionException;
}

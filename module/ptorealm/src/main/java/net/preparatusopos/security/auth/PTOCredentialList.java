package net.preparatusopos.security.auth;

public interface PTOCredentialList
extends Iterable<PTOCredential>
{
	public void add(PTOCredential credential)
	throws PTORealmManagerException, PTOCredentialDuplicatedException;
	
	public PTOCredential remove(int index)
	throws PTORealmManagerException;
}

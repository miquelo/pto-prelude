package net.preparatusopos.security.auth;

import net.preparatusopos.security.auth.spi.PTORealmConnection;

public interface PTORealmManagerFactory
{
	public PTORealmManager getManager(PTORealmConnection connection)
	throws PTORealmManagerException;
}

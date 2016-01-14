package net.preparatusopos.security.auth.spi;

public interface PTORealmConnectionFactory
{
	public PTORealmConnection getConnection()
	throws PTORealmConnectionException;
}

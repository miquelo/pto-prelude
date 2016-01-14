package net.preparatusopos.resource.security.external.google;

import net.preparatusopos.security.external.ExternalUIDConnection;
import net.preparatusopos.security.external.ExternalUIDConnectionException;
import net.preparatusopos.util.resource.AbstractConnection;

public class GoogleExternalUIDConnection
extends AbstractConnection<GoogleExternalUIDManagedConnection>
implements ExternalUIDConnection
{
	public GoogleExternalUIDConnection()
	{
	}
	
	@Override
	public String getExternalUID(String authorizationCode)
	throws ExternalUIDConnectionException
	{
		return mc.getExternalUID(authorizationCode);
	}
}
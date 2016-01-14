package net.preparatusopos.security.external;

import java.io.Closeable;

public interface ExternalUIDConnection
extends Closeable
{
	public String getExternalUID(String authorizationCode)
	throws ExternalUIDConnectionException;
}

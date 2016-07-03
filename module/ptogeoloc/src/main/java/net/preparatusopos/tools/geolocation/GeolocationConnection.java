package net.preparatusopos.tools.geolocation;

import java.io.IOException;

public interface GeolocationConnection
extends AutoCloseable
{
	@Override
	public void close()
	throws IOException;
}

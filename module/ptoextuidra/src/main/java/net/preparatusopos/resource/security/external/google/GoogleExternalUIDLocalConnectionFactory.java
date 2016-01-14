package net.preparatusopos.resource.security.external.google;

import net.preparatusopos.util.resource.AbstractConnectionFactory;

public class GoogleExternalUIDLocalConnectionFactory
extends AbstractConnectionFactory<GoogleExternalUIDConnection>
implements GoogleExternalUIDConnectionFactory
{
	private static final long serialVersionUID = 1L;
	
	public GoogleExternalUIDLocalConnectionFactory()
	{
	}
	
	@Override
	protected void configure(GoogleExternalUIDConnection connection)
	{
	}
}
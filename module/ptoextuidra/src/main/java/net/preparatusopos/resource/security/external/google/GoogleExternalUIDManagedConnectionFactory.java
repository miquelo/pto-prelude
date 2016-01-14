package net.preparatusopos.resource.security.external.google;

import java.io.IOException;

import javax.resource.ResourceException;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;

import net.preparatusopos.security.external.ExternalUIDConnection;
import net.preparatusopos.util.resource.AbstractConnectionFactory;
import net.preparatusopos.util.resource.AbstractManagedConnectionFactory;

@ConnectionDefinition(
	connectionFactory=GoogleExternalUIDConnectionFactory.class,
	connectionFactoryImpl=GoogleExternalUIDLocalConnectionFactory.class,
	connection=ExternalUIDConnection.class,
	connectionImpl=GoogleExternalUIDConnection.class
)
public class GoogleExternalUIDManagedConnectionFactory
extends AbstractManagedConnectionFactory
{
	private static final long serialVersionUID = 1L;
	
	private String clientID;
	private String clientSecret;
	
	public GoogleExternalUIDManagedConnectionFactory()
	{
		clientID = null;
		clientSecret = null;
	}
	
	@ConfigProperty(
		type=String.class
	)
	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}

	@ConfigProperty(
		type=String.class,
		confidential=true
	)
	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnectionFactory
	 */

	@Override
	public ManagedConnection createManagedConnection(Subject subject,
			ConnectionRequestInfo cxRequestInfo)
	throws ResourceException
	{
		try
		{
			GoogleExternalUIDManagedConnection mc =
					new GoogleExternalUIDManagedConnection();
			mc.setClientID(clientID);
			mc.setClientSecret(clientSecret);
			mc.connect();
			return mc;
		}
		catch (IOException exception)
		{
			throw new ResourceException(exception);
		}
	}

	/*
	 * Implements: net.preparatusopos.util.resource.AbstractConnectionFactory
	 */
	
	@Override
	protected AbstractConnectionFactory<?> createLocalConnectionFactory()
	{
		return new GoogleExternalUIDLocalConnectionFactory();
	}
}
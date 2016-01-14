package net.preparatusopos.resource.security.external.google;

import java.io.IOException;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.security.auth.Subject;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import net.preparatusopos.security.external.ExternalUIDConnectionException;
import net.preparatusopos.util.resource.AbstractManagedConnection;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleExternalUIDManagedConnection
extends AbstractManagedConnection<GoogleExternalUIDConnection>
implements XAResource
{
	private static final String EIS_PRODUCT_NAME = "GoogleExternalUID";
	private static final String EIS_PRODUCT_VERSION = "0.1";
	private static final int MAX_CONNECTIONS = 20;
	
	private static final String TOKEN_URI =
			"https://www.googleapis.com/oauth2/v4/token";
	
	private HttpTransport transport;
	private JacksonFactory jacksonFact;
	
	private String clientID;
	private String clientSecret;
	
	public GoogleExternalUIDManagedConnection()
	{
		transport = null;
		jacksonFact = JacksonFactory.getDefaultInstance();
		
		clientID = null;
		clientSecret = null;
	}

	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}

	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}
	
	public void connect()
	throws IOException
	{
		transport = new NetHttpTransport();
	}
	
	public String getExternalUID(String authorizationCode)
	throws ExternalUIDConnectionException
	{
		try
		{
			GoogleAuthorizationCodeTokenRequest tokenRequest =
					new GoogleAuthorizationCodeTokenRequest(transport,
					jacksonFact, TOKEN_URI, clientID, clientSecret,
					authorizationCode, "postmessage");
			GoogleTokenResponse tokenResponse = tokenRequest.execute();
			
			GoogleIdToken idToken = tokenResponse.parseIdToken();
			GoogleIdToken.Payload payload = idToken.getPayload();
			return payload.getSubject();
		}
		catch (IOException exception)
		{
			throw new ExternalUIDConnectionException(exception);
		}
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnectionMetaData
	 */
	
	@Override
	public String getEISProductName()
	throws ResourceException
	{
		return EIS_PRODUCT_NAME;
	}

	@Override
	public String getEISProductVersion()
	throws ResourceException
	{
		return EIS_PRODUCT_VERSION;
	}

	@Override
	public int getMaxConnections()
	throws ResourceException
	{
		return MAX_CONNECTIONS;
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnection
	 */
	
	@Override
	public LocalTransaction getLocalTransaction()
	throws ResourceException
	{
		throw new ResourceException("LocalTransaction is not supported");
	}
	
	@Override
	public XAResource getXAResource()
	throws ResourceException
	{
		return this;
	}
	
	@Override
	public void destroy()
	throws ResourceException
	{
		try
		{
			transport.shutdown();
		}
		catch (IOException exception)
		{
			throw new ResourceException(exception);
		}
	}
	
	/*
	 * Implements: net.preparatusopos.resource.security.external.
	 * AbstractManagedConnection
	 */
	
	@Override
	public int matchScore(Subject subject, ConnectionRequestInfo cxRequestInfo)
	{
		return -1;
	}

	/*
	 * Implements: javax.transaction.xa.XAResource
	 */
	
	@Override
	public int getTransactionTimeout()
	throws XAException
	{
		return 0;
	}
	
	@Override
	public boolean setTransactionTimeout(int seconds)
	throws XAException
	{
		return false;
	}
	
	@Override
	public void start(Xid xid, int flags)
	throws XAException
	{
	}
	
	@Override
	public void end(Xid xid, int flags)
	throws XAException
	{
	}
	
	@Override
	public int prepare(Xid xid)
	throws XAException
	{
		return XA_RDONLY;
	}
	
	@Override
	public void forget(Xid xid)
	throws XAException
	{
	}
	
	@Override
	public void commit(Xid xid, boolean onePhase)
	throws XAException
	{
	}
	
	@Override
	public void rollback(Xid xid)
	throws XAException
	{
	}

	@Override
	public Xid[] recover(int flag)
	throws XAException
	{
		return null;
	}
	
	@Override
	public boolean isSameRM(XAResource xares)
	throws XAException
	{
		return xares instanceof GoogleExternalUIDManagedConnection;
	}
}

package net.preparatusopos.util.resource;

import java.io.PrintWriter;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

public abstract class AbstractManagedConnectionFactory
implements ManagedConnectionFactory
{
	private static final long serialVersionUID = 1L;
	
	private PrintWriter logWriter;
	
	protected AbstractManagedConnectionFactory()
	{
		logWriter = null;
	}
	
	@Override
	public PrintWriter getLogWriter()
	throws ResourceException
	{
		return logWriter;
	}
	
	@Override
	public void setLogWriter(PrintWriter out)
	throws ResourceException
	{
		logWriter = out;
	}
	
	@Override
	public Object createConnectionFactory(ConnectionManager cxManager)
	throws ResourceException
	{
		AbstractConnectionFactory<?> fact = createLocalConnectionFactory();
		fact.init(this, cxManager);
		return fact;
	}
	
	@Override
	public Object createConnectionFactory()
	throws ResourceException
	{
		throw new ResourceException("ConnectionManager is not provided");
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	public ManagedConnection matchManagedConnections(Set connectionSet,
			Subject subject, ConnectionRequestInfo cxRequestInfo)
	throws ResourceException
	{
		ManagedConnection candidate = null;
		int minScore = Integer.MAX_VALUE;
		for (Object obj : connectionSet)
		{
			if (obj instanceof AbstractManagedConnection)
			{
				AbstractManagedConnection conn =
						(AbstractManagedConnection) obj;
				int score = conn.matchScore(subject, cxRequestInfo);
				if (score < 0)
					return conn;
				if (score < minScore)
				{
					candidate = conn;
					minScore = score;
				}
			}
		}
		return candidate;
	}
	
	protected abstract AbstractConnectionFactory<?>
			createLocalConnectionFactory();
}

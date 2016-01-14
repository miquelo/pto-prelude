package net.preparatusopos.util.resource;

import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;

public abstract class AbstractManagedConnection<
		T extends AbstractConnection<?>>
implements ManagedConnection, ManagedConnectionMetaData
{
	private PrintWriter logWriter;
	private List<AbstractConnection<?>> connList;
	private List<ConnectionEventListener> listenerList;
	
	protected AbstractManagedConnection()
	{
		logWriter = null;
		connList = new LinkedList<>();
		listenerList = new ArrayList<>();
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnection
	 */
	
	@Override
	public ManagedConnectionMetaData getMetaData()
	throws ResourceException
	{
		return this;
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
	public Object getConnection(Subject subject,
			ConnectionRequestInfo cxRequestInfo)
	throws ResourceException
	{
		try
		{
			Class<T> connClass = getConnectionClass();
			T conn = connClass.newInstance();
			conn.associate(this);
			return conn;
		}
		catch (IllegalAccessException | InstantiationException exception)
		{
			throw new ResourceException(exception);
		}
	}

	@Override
	public void associateConnection(Object connection)
	throws ResourceException
	{
		if (connection instanceof AbstractConnection)
		{
			AbstractConnection<?> conn = (AbstractConnection<?>) connection;
			conn.associate(this);
		}
		StringBuilder msg = new StringBuilder();
		msg.append("Incompatible connection type ");
		msg.append(connection.getClass());
		throw new ResourceException(msg.toString());
	}
	
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener)
	{
		synchronized (listenerList)
		{
			listenerList.add(listener);
		}
	}

	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener)
	{
		synchronized (listenerList)
		{
			listenerList.remove(listener);
		}
	}
	
	@Override
	public void cleanup()
	throws ResourceException
	{
		for (AbstractConnection<?> conn : connList)
			conn.invalidate();
	}
	
	/*
	 * Implements: javax.resource.spi.ManagedConnectionMetaData
	 */
	
	@Override
	public String getUserName()
	throws ResourceException
	{
		return null;
	}
	
	public void associate(AbstractConnection<?> conn)
	{
		connList.add(conn);
	}
	
	public void release(AbstractConnection<?> conn)
	{
		connList.remove(conn);
	}
	
	public void fireConnectionClosed(AbstractConnection<?> conn)
	{
		int eid = ConnectionEvent.CONNECTION_CLOSED;
		ConnectionEvent event = new ConnectionEvent(this, eid);
		event.setConnectionHandle(conn);
		
		synchronized (listenerList)
		{
			for (ConnectionEventListener listener : listenerList)
				listener.connectionClosed(event);	
		}
	}
	
	public abstract int matchScore(Subject subject,
			ConnectionRequestInfo cxRequestInfo);
	
	@SuppressWarnings("unchecked")
	private Class<T> getConnectionClass()
	{
		ParameterizedType type = (ParameterizedType)
				getClass().getGenericSuperclass();
		return (Class<T>) type.getActualTypeArguments()[0];
	}
}

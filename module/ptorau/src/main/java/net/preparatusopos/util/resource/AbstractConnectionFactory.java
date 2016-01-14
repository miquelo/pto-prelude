package net.preparatusopos.util.resource;

import java.io.Serializable;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

public abstract class AbstractConnectionFactory<
		T extends AbstractConnection<?>>
implements Serializable, Referenceable
{
	private static final long serialVersionUID = 1L;
	
	private ManagedConnectionFactory mcf;
	private ConnectionManager cxManager;
	private Reference reference;
	
	protected AbstractConnectionFactory()
	{
		mcf = null;
		cxManager = null;
		reference = null;
	}
	
	public void init(ManagedConnectionFactory mcf, ConnectionManager cxManager)
	{
		this.mcf = mcf;
		this.cxManager = cxManager;
	}
	
	@SuppressWarnings("unchecked")
	public T getConnection()
	{
		try
		{
			T conn = (T) cxManager.allocateConnection(mcf, null);
			configure(conn);
			return conn;
		}
		catch (ResourceException exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	/*
	 * Implements: javax.naming.Reference
	 */
	
	@Override
	public Reference getReference()
	throws NamingException
	{
		return reference;
	}
	
	public void setReference(Reference reference)
	{
		this.reference = reference;
	}
	
	protected abstract void configure(T connection);
}

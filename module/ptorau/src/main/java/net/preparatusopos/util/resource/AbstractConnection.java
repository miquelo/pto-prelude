package net.preparatusopos.util.resource;

import java.io.IOException;

public class AbstractConnection<T extends AbstractManagedConnection<?>>
{
	protected T mc;
	
	protected AbstractConnection()
	{
		mc = null;
	}
	
	@SuppressWarnings("unchecked")
	public void associate(AbstractManagedConnection<?> mc)
	{
		if (this.mc != null)
			this.mc.release(this);
		this.mc = (T) mc;
		this.mc.associate(this);
	}
	
	public void invalidate()
	{
		mc = null;
	}
	
	public void close()
	throws IOException
	{
		mc.fireConnectionClosed(this);
	}
}
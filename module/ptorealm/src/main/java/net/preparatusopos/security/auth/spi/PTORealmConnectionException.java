package net.preparatusopos.security.auth.spi;

public class PTORealmConnectionException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PTORealmConnectionException(String message)
	{
		super(message);
	}
	
	public PTORealmConnectionException(Throwable cause)
	{
		super(cause);
	}
	
	public PTORealmConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

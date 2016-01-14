package net.preparatusopos.security.auth;

public class PTORealmManagerException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public PTORealmManagerException(String message)
	{
		super(message);
	}
	
	public PTORealmManagerException(Throwable cause)
	{
		super(cause);
	}
	
	public PTORealmManagerException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

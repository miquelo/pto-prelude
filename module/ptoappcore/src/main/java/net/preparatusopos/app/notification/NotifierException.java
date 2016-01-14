package net.preparatusopos.app.notification;

public class NotifierException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public NotifierException(String message)
	{
		super(message);
	}
	
	public NotifierException(Throwable cause)
	{
		super(cause);
	}
	
	public NotifierException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

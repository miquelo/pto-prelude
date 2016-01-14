package net.preparatusopos.app.core;

public class AccreditatorException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public AccreditatorException(String message)
	{
		super(message);
	}
	
	public AccreditatorException(Throwable cause)
	{
		super(cause);
	}
	
	public AccreditatorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

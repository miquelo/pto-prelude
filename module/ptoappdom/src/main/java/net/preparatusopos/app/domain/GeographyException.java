package net.preparatusopos.app.domain;

public class GeographyException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public GeographyException(String message)
	{
		super(message);
	}
	
	public GeographyException(Throwable cause)
	{
		super(cause);
	}
	
	public GeographyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

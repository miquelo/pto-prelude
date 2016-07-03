package net.preparatusopos.tools.geolocation;

public class GeolocationException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public GeolocationException(String message)
	{
		super(message);
	}
	
	public GeolocationException(Throwable cause)
	{
		super(cause);
	}
	
	public GeolocationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

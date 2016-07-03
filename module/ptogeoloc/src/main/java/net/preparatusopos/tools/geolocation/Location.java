package net.preparatusopos.tools.geolocation;

import java.io.Serializable;

public class Location
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private double latitude;
	private double longitude;
	
	public Location()
	{
		latitude = 0.;
		longitude = 0.;
	}
	
	@Override
	public int hashCode()
	{
		return (int) Math.floor(latitude);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Location)
		{
			Location location = (Location) obj;
			return latitude == location.latitude
					&& longitude == location.longitude;
		}
		return false;
	}
}

package net.preparatusopos.app.domain;

import java.beans.Transient;
import java.io.Serializable;

public class RegionRef
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CountryRef countryRef;
	private String name;
	
	public RegionRef()
	{
		this(null, null);
	}
	
	public RegionRef(CountryRef countryRef, String name)
	{
		this.countryRef = countryRef;
		this.name = name;
	}

	@Transient
	public String getCountryCode()
	{
		return countryRef == null ? null : countryRef.getCode();
	}

	public CountryRef getCountryRef()
	{
		return countryRef;
	}

	public void setCountryRef(CountryRef countryRef)
	{
		this.countryRef = countryRef;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public RegionRef regionRef(String name)
	{
		return new RegionRef(countryRef, name);
	}
	
	public RouteRef routeRef(String name)
	{
		return new RouteRef(countryRef, name);
	}

	@Override
	public int hashCode()
	{
		return countryRef == null ? 0 : countryRef.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RegionRef)
		{
			RegionRef ref = (RegionRef) obj;
			return countryRef != null && countryRef.equals(ref.countryRef)
					&& name != null && name.equals(ref.name);
		}
		return false;
	}
}

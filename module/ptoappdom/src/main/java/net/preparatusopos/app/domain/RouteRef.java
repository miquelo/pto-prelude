package net.preparatusopos.app.domain;

import java.io.Serializable;

public class RouteRef
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CountryRef countryRef;
	private String name;
	
	public RouteRef()
	{
		this(null, null);
	}
	
	public RouteRef(CountryRef countryRef, String name)
	{
		this.countryRef = countryRef;
		this.name = name;
	}

	public String getCountryCode()
	{
		return countryRef.getCode();
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

	@Override
	public int hashCode()
	{
		return countryRef == null ? 0 : countryRef.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof RouteRef)
		{
			RouteRef ref = (RouteRef) obj;
			return countryRef != null && countryRef.equals(ref.countryRef)
					&& name != null && name.equals(ref.name);
		}
		return false;
	}
}

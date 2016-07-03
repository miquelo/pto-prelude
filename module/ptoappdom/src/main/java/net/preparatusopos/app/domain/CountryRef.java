package net.preparatusopos.app.domain;

import java.io.Serializable;

public class CountryRef
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	public CountryRef()
	{
		this(null);
	}
	
	public CountryRef(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
	
	public RegionRef regionRef(String name)
	{
		return new RegionRef(this, name);
	}
	
	@Override
	public int hashCode()
	{
		return code == null ? 0 : code.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof CountryRef)
		{
			CountryRef ref = (CountryRef) obj;
			return code != null && code.equals(ref.code);
		}
		return false;
	}
}

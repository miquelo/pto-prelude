package net.preparatusopos.app.domain;

public class Country
implements Referenced<CountryRef>
{
	private static final long serialVersionUID = 1L;
	
	private CountryRef ref;
	private String displayName;
	
	public Country()
	{
		this(null, null);
	}
	
	public Country(CountryRef ref, String displayName)
	{
		this.ref = ref;
		this.displayName = displayName;
	}

	@Override
	public CountryRef getRef()
	{
		return ref;
	}

	public void setRef(CountryRef ref)
	{
		this.ref = ref;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public Region region(String name, String displayName)
	{
		return new Region(ref.regionRef(name), displayName);
	}
	
	@Override
	public int hashCode()
	{
		return ref == null ? 0 : ref.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Country)
		{
			Country country = (Country) obj;
			return ref != null && ref.equals(country.ref);
		}
		return false;
	}
}

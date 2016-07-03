package net.preparatusopos.app.domain;

public class Region
implements Referenced<RegionRef>
{
	private static final long serialVersionUID = 1L;
	
	private RegionRef ref;
	private String displayName;
	
	public Region()
	{
		this(null, null);
	}
	
	public Region(RegionRef ref, String displayName)
	{
		this.ref = ref;
		this.displayName = displayName;
	}

	@Override
	public RegionRef getRef()
	{
		return ref;
	}

	public void setRef(RegionRef ref)
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
	
	public Route route(String name, String displayName)
	{
		return new Route(ref.routeRef(name), displayName);
	}

	@Override
	public int hashCode()
	{
		return ref == null ? 0 : ref.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Region)
		{
			Region region = (Region) obj;
			return ref != null && ref.equals(region.ref);
		}
		return false;
	}
}

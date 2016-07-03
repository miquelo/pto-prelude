package net.preparatusopos.app.domain;

public class Route
implements Referenced<RouteRef>
{
	private static final long serialVersionUID = 1L;
	
	private RouteRef ref;
	private String displayName;
	
	public Route()
	{
		this(null, null);
	}
	
	public Route(RouteRef ref, String displayName)
	{
		this.ref = ref;
		this.displayName = displayName;
	}

	@Override
	public RouteRef getRef()
	{
		return ref;
	}

	public void setRef(RouteRef ref)
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

	@Override
	public int hashCode()
	{
		return ref == null ? 0 : ref.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Route)
		{
			Route route = (Route) obj;
			return ref != null && ref.equals(route.ref);
		}
		return false;
	}
}

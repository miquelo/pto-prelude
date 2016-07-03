package net.preparatusopos.app.core.geography.model;

import java.util.Set;

import net.preparatusopos.app.domain.GeographyException;

public interface NodeManager
{
	public Set<CountryNode> countries()
	throws GeographyException;
	
	public Set<RegionNode> regions(String countryCode)
	throws GeographyException;
	
	public Set<RouteNode> routes(String countryCode)
	throws GeographyException;
}

package net.preparatusopos.app.domain;

import java.util.Locale;
import java.util.Set;

public interface Geography
{
	public Set<Country> fetch(Locale locale)
	throws GeographyException;

	public Set<Region> fetch(CountryRef countryRef, RegionType type,
			Locale locale)
	throws GeographyException;
	
	public Set<Region> fetch(CountryRef countryRef, RegionType type,
			String zipCode, Locale locale)
	throws GeographyException;
	
	public Set<Region> fetch(RegionRef regionRef, RegionType type,
			Locale locale)
	throws GeographyException;
	
	public Set<Region> fetch(RegionRef regionRef, RegionType type,
			String zipCode, Locale locale)
	throws GeographyException;
	
	public Set<Route> fetch(RegionRef regionRef, Locale locale)
	throws GeographyException;
	
	public Set<Route> fetch(RegionRef regionRef, String zipCode, Locale locale)
	throws GeographyException;
	
	public Country find(CountryRef countryRef, Locale locale)
	throws GeographyException;
	
	public Region find(RegionRef regionRef, Locale locale)
	throws GeographyException;
	
	public Route find(RouteRef routeRef, Locale locale)
	throws GeographyException;
	
	public RegionRef parent(RegionRef regionRef, RegionType type)
	throws GeographyException;
	
	public RegionRef parent(RouteRef routeRef, RegionType type)
			throws GeographyException;
	
	public InputMatches<Country> match(String input, Locale locale)
	throws GeographyException;
	
	public InputMatches<Region> match(CountryRef countryRef, RegionType type,
			String input, Locale locale)
	throws GeographyException;
	
	public InputMatches<Region> match(RegionRef regionRef, RegionType type,
			String input, Locale locale)
	throws GeographyException;
	
	public InputMatches<Route> match(RegionRef regionRef, String input,
			Locale locale)
	throws GeographyException;
}

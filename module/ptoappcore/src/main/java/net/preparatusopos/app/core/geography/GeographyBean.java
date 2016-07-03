package net.preparatusopos.app.core.geography;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Singleton;

import net.preparatusopos.app.core.geography.model.CountryConstraint;
import net.preparatusopos.app.core.geography.model.CountryNode;
import net.preparatusopos.app.core.geography.model.RegionConstraint;
import net.preparatusopos.app.core.geography.model.RegionNode;
import net.preparatusopos.app.core.geography.model.RouteConstraint;
import net.preparatusopos.app.core.geography.model.RouteNode;
import net.preparatusopos.app.core.geography.model.World;
import net.preparatusopos.app.core.matching.Matcher;
import net.preparatusopos.app.domain.Country;
import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.Geography;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.InputMatches;
import net.preparatusopos.app.domain.Region;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RegionType;
import net.preparatusopos.app.domain.Route;
import net.preparatusopos.app.domain.RouteRef;

@Remote(
	Geography.class
)
@Singleton(
	name="ejb/GeographyBean"
)
@DeclareRoles({
	"ADMIN",
	"MEMBER"
})
public class GeographyBean
implements Geography
{
	private World world;
	private Matcher matcher;
	
	public GeographyBean()
	{
		world = World.internal();
		matcher = Matcher.simple();
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Country> fetch(Locale locale)
	throws GeographyException
	{
		Set<Country> result = new HashSet<>();
		CountryConstraint constraint = new CountryConstraint();
		for (CountryNode node : world.countries(constraint))
			result.add(node.build(locale));
		return result;
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Region> fetch(CountryRef countryRef, RegionType type,
			Locale locale)
	throws GeographyException
	{
		Set<Region> result = new HashSet<>();
		CountryNode country = world.country(countryRef.getCode());
		RegionConstraint constraint = new RegionConstraint();
		for (RegionNode node : country.regions(type, constraint))
			result.add(node.build(locale));
		return result;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Region> fetch(CountryRef countryRef, RegionType type,
			String zipCode, Locale locale)
	throws GeographyException
	{
		Set<Region> result = new HashSet<>();
		CountryNode country = world.country(countryRef.getCode());
		RegionConstraint constraint = new RegionConstraint(zipCode);
		for (RegionNode node : country.regions(type, constraint))
			result.add(node.build(locale));
		return result;
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Region> fetch(RegionRef regionRef, RegionType type,
			Locale locale)
	throws GeographyException
	{
		Set<Region> result = new HashSet<>();
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RegionConstraint constraint = new RegionConstraint();
		for (RegionNode node : region.regions(type, constraint))
			result.add(node.build(locale));
		return result;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Region> fetch(RegionRef regionRef, RegionType type,
			String zipCode, Locale locale)
	throws GeographyException
	{
		Set<Region> result = new HashSet<>();
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RegionConstraint constraint = new RegionConstraint(zipCode);
		for (RegionNode node : region.regions(type, constraint))
			result.add(node.build(locale));
		return result;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Route> fetch(RegionRef regionRef, Locale locale)
	throws GeographyException
	{
		Set<Route> result = new HashSet<>();
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RouteConstraint constraint = new RouteConstraint();
		for (RouteNode node : region.routes(constraint))
			result.add(node.build(locale));
		return result;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Set<Route> fetch(RegionRef regionRef, String zipCode, Locale locale)
	throws GeographyException
	{
		Set<Route> result = new HashSet<>();
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RouteConstraint constraint = new RouteConstraint(zipCode);
		for (RouteNode node : region.routes(constraint))
			result.add(node.build(locale));
		return result;
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Country find(CountryRef countryRef, Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(countryRef.getCode());
		return country.build(locale);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Region find(RegionRef regionRef, Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		return region.build(locale);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public Route find(RouteRef routeRef, Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(routeRef.getCountryCode());
		RouteNode route = country.route(routeRef.getName());
		return route.build(locale);
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	public RegionRef parent(RegionRef regionRef, RegionType type)
	throws GeographyException
	{
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		return region.parent(type).getRef();
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	public RegionRef parent(RouteRef routeRef, RegionType type)
	throws GeographyException
	{
		CountryNode country = world.country(routeRef.getCountryCode());
		RouteNode route = country.route(routeRef.getName());
		return route.parent(type).getRef();
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public InputMatches<Country> match(String input, Locale locale)
	throws GeographyException
	{
		CountryConstraint constraint = new CountryConstraint();
		return matcher.match(world.countries(constraint), locale, input);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public InputMatches<Region> match(CountryRef countryRef, RegionType type,
			String input, Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(countryRef.getCode());
		RegionConstraint constraint = new RegionConstraint();
		return matcher.match(country.regions(type, constraint), locale, input);
	}

	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public InputMatches<Region> match(RegionRef regionRef, RegionType type,
			String input, Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RegionConstraint constraint = new RegionConstraint();
		return matcher.match(region.regions(type, constraint), locale, input);
	}
	
	@RolesAllowed({
		"ADMIN",
		"MEMBER"
	})
	@Override
	public InputMatches<Route> match(RegionRef regionRef, String input,
			Locale locale)
	throws GeographyException
	{
		CountryNode country = world.country(regionRef.getCountryCode());
		RegionNode region = country.region(regionRef.getName());
		RouteConstraint constraint = new RouteConstraint();
		return matcher.match(region.routes(constraint), locale, input);
	}
}

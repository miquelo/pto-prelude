package net.preparatusopos.app.core.geography.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.preparatusopos.app.core.matching.MatchingSource;
import net.preparatusopos.app.domain.Country;
import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RegionType;
import net.preparatusopos.app.domain.RouteRef;

public class CountryNode
implements MatchingSource<Locale, Country>
{
	private Stub stub;
	
	private CountryNode(Stub stub)
	{
		this.stub = stub;
	}
	
	public CountryRef getRef()
	{
		return stub.getRef();
	}
	
	@Override
	public String getMatchingText(Locale locale)
	{
		return stub.getMatchingText(locale);
	}
	
	public boolean hasRef(CountryRef countryRef)
	{
		return countryRef.equals(getRef());
	}
	
	@Override
	public Country build(Locale locale)
	{
		return stub.build(locale);
	}
	
	public Set<RegionNode> regions(RegionType type, RegionConstraint constraint)
	throws GeographyException
	{
		return stub.regions(type, constraint);
	}
	
	public RegionNode region(String name)
	throws GeographyException
	{
		return stub.region(name);
	}
	
	public RouteNode route(String name)
	throws GeographyException
	{
		return stub.route(name);
	}
	
	@Override
	public int hashCode()
	{
		return stub.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CountryNode)
		{
			CountryNode node = (CountryNode) obj;
			return stub.equals(node.stub);
		}
		return false;
	}
	
	public static CountryNode empty()
	{
		return new CountryNode(new EmptyStub());
	}
	
	public static CountryNode create(NodeManager manager, CountryRef ref,
			LocalizedValue displayName)
	{
		return new CountryNode(new DefaultStub(manager, ref, displayName));
	}
	
	private interface Stub
	extends MatchingSource<Locale, Country>
	{
		public CountryRef getRef();
		
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		throws GeographyException;
		
		public RegionNode region(String name)
		throws GeographyException;
		
		public RouteNode route(String name)
		throws GeographyException;
	}
	
	private static class EmptyStub
	implements Stub
	{
		public EmptyStub()
		{
		}
		
		@Override
		public CountryRef getRef()
		{
			return null;
		}
		
		@Override
		public String getMatchingText(Locale locale)
		{
			throw new IllegalStateException("Empty country");
		}

		@Override
		public Country build(Locale locale)
		{
			return null;
		}
		
		public RegionNode region(String name)
		{
			return RegionNode.empty();
		}
		
		public RouteNode route(String name)
		throws GeographyException
		{
			return RouteNode.empty();
		}
		
		@Override
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		{
			return Collections.emptySet();
		}
		
		@Override
		public int hashCode()
		{
			return 0;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return false;
		}
	}
	
	private static class DefaultStub
	implements Stub
	{
		private NodeManager manager;
		private CountryRef ref;
		private LocalizedValue displayName;
		
		public DefaultStub(NodeManager manager, CountryRef ref,
				LocalizedValue displayName)
		{
			this.manager = manager;
			this.ref = ref;
			this.displayName = displayName;
		}
		
		@Override
		public CountryRef getRef()
		{
			return ref;
		}
		
		@Override
		public String getMatchingText(Locale locale)
		{
			return displayName.getText(locale);
		}

		@Override
		public Country build(Locale locale)
		{
			String displayNameText = displayName.getText(locale);
			return new Country(ref, displayNameText);
		}
		
		public RegionNode region(String name)
		throws GeographyException
		{
			for (RegionNode node : manager.regions(ref.getCode()))
				if (node.hasRef(new RegionRef(ref, name)))
					return node;
			return RegionNode.empty();
		}
		
		public RouteNode route(String name)
		throws GeographyException
		{
			for (RouteNode node : manager.routes(ref.getCode()))
				if (node.hasRef(new RouteRef(ref, name)))
					return node;
			return RouteNode.empty();
		}
		
		@Override
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		throws GeographyException
		{
			Set<RegionNode> result = new HashSet<>();
			for (RegionNode node : manager.regions(ref.getCode()))
				if (node.hasType(type) && constraint.accept(node))
					result.add(node);
			return result;
		}
		
		@Override
		public int hashCode()
		{
			return ref.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof DefaultStub)
			{
				DefaultStub stub = (DefaultStub) obj;
				return ref.equals(stub.ref);
			}
			return false;
		}
	}
}

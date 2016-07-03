package net.preparatusopos.app.core.geography.model;

import java.util.Locale;
import java.util.Set;

import net.preparatusopos.app.core.matching.MatchingSource;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RegionType;
import net.preparatusopos.app.domain.Route;
import net.preparatusopos.app.domain.RouteRef;

public class RouteNode
implements MatchingSource<Locale, Route>
{
	private Stub stub;
	
	private RouteNode(Stub stub)
	{
		this.stub = stub;
	}
	
	public RouteRef getRef()
	{
		return stub.getRef();
	}
	
	@Override
	public String getMatchingText(Locale locale)
	{
		return stub.getMatchingText(locale);
	}
	
	public boolean isInsideOf(RegionRef regionRef)
	{
		return stub.isInsideOf(regionRef);
	}
	
	public boolean hasRef(RouteRef ref)
	{
		return ref.equals(getRef());
	}
	
	public RegionNode parent(RegionType type)
	throws GeographyException
	{
		return stub.parent(type);
	}
	
	public boolean hasZipCode(String zipCode)
	{
		return stub.hasZipCode(zipCode);
	}
	
	@Override
	public Route build(Locale locale)
	{
		return stub.build(locale);
	}
	
	@Override
	public int hashCode()
	{
		return stub.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof RouteNode)
		{
			RouteNode node = (RouteNode) obj;
			return stub.equals(node.stub);
		}
		return false;
	}
	
	public static RouteNode empty()
	{
		return new RouteNode(new EmptyStub());
	}
	
	public static RouteNode create(NodeManager manager, RouteRef ref,
			Set<RegionRef> inside, Set<String> zipCodes,
			LocalizedValue displayName)
	{
		return new RouteNode(new DefaultStub(manager, ref, inside, zipCodes,
				displayName));
	}
	
	private interface Stub
	extends MatchingSource<Locale, Route>
	{
		public RouteRef getRef();
		
		public boolean isInsideOf(RegionRef ref);
		
		public boolean hasZipCode(String zipCode);
		
		public RegionNode parent(RegionType type)
		throws GeographyException;
	}
	
	private static class EmptyStub
	implements Stub
	{
		public EmptyStub()
		{
		}
		
		@Override
		public RouteRef getRef()
		{
			return null;
		}
		
		@Override
		public String getMatchingText(Locale locale)
		{
			throw new IllegalStateException("Empty route");
		}
		
		@Override
		public boolean isInsideOf(RegionRef regionRef)
		{
			return false;
		}
		
		@Override
		public boolean hasZipCode(String zipCode)
		{
			return false;
		}
		
		public RegionNode parent(RegionType type)
		throws GeographyException
		{
			return RegionNode.empty();
		}
		
		@Override
		public Route build(Locale locale)
		{
			return null;
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
		private RouteRef ref;
		private Set<RegionRef> inside;
		private Set<String> zipCodes;
		private LocalizedValue displayName;
		
		public DefaultStub(NodeManager manager, RouteRef ref,
				Set<RegionRef> inside, Set<String> zipCodes,
				LocalizedValue displayName)
		{
			this.ref = ref;
			this.inside = inside;
			this.zipCodes = zipCodes;
			this.displayName = displayName;
		}
		
		@Override
		public RouteRef getRef()
		{
			return ref;
		}
		
		@Override
		public String getMatchingText(Locale locale)
		{
			return displayName.getText(locale);
		}
		
		@Override
		public boolean isInsideOf(RegionRef regionRef)
		{
			return inside.contains(regionRef);
		}
		
		@Override
		public boolean hasZipCode(String zipCode)
		{
			return zipCodes.contains(zipCode);
		}
		
		@Override
		public RegionNode parent(RegionType type)
		throws GeographyException
		{
			for (RegionNode node : manager.regions(ref.getCountryCode()))
				if (node.hasType(type) && node.isParentOf(inside))
					return node;
			return RegionNode.empty();
		}
		
		@Override
		public Route build(Locale locale)
		{
			String displayNameText = displayName.getText(locale);
			return new Route(ref, displayNameText);
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

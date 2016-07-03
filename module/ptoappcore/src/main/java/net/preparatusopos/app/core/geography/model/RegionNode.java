package net.preparatusopos.app.core.geography.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.preparatusopos.app.core.matching.MatchingSource;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.Region;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RegionType;

public class RegionNode
implements MatchingSource<Locale, Region>
{
	private Stub stub;
	
	private RegionNode(Stub stub)
	{
		this.stub = stub;
	}
	
	public RegionRef getRef()
	{
		return stub.getRef();
	}
	
	@Override
	public String getMatchingText(Locale locale)
	{
		return stub.getMatchingText(locale);
	}
	
	public boolean isParentOf(Set<RegionRef> inside)
	{
		return inside.contains(getRef());
	}
	
	public boolean isInsideOf(RegionRef regionRef)
	{
		return stub.isInsideOf(regionRef);
	}
	
	public boolean hasRef(RegionRef ref)
	{
		return ref.equals(getRef());
	}
	
	public boolean hasType(RegionType type)
	{
		return stub.hasType(type);
	}
	
	public boolean hasZipCode(String zipCode)
	throws GeographyException
	{
		return stub.hasZipCode(zipCode);
	}
	
	public RegionNode parent(RegionType type)
	throws GeographyException
	{
		return stub.parent(type);
	}
	
	@Override
	public Region build(Locale locale)
	{
		return stub.build(locale);
	}
	
	public Set<RegionNode> regions(RegionType type, RegionConstraint constraint)
	throws GeographyException
	{
		return stub.regions(type, constraint);
	}
	
	public Set<RouteNode> routes(RouteConstraint constraint)
	throws GeographyException
	{
		return stub.routes(constraint);
	}
	
	@Override
	public int hashCode()
	{
		return stub.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof RegionNode)
		{
			RegionNode node = (RegionNode) obj;
			return stub.equals(node.stub);
		}
		return false;
	}
	
	public static RegionNode empty()
	{
		return new RegionNode(new EmptyStub());
	}
	
	public static RegionNode create(NodeManager manager, RegionRef ref,
			RegionType type, Set<RegionRef> inside, Set<String> zipCodes,
			LocalizedValue displayName)
	{
		return new RegionNode(new DefaultStub(manager, ref, type, inside,
				zipCodes, displayName));
	}
	
	private interface Stub
	extends MatchingSource<Locale, Region>
	{
		public RegionRef getRef();
		
		public boolean isInsideOf(RegionRef ref);
		
		public boolean hasType(RegionType type);
		
		public boolean hasZipCode(String zipCode)
		throws GeographyException;
		
		public RegionNode parent(RegionType type)
		throws GeographyException;
		
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		throws GeographyException;
		
		public Set<RouteNode> routes(RouteConstraint constraint)
		throws GeographyException;
	}
	
	private static class EmptyStub
	implements Stub
	{
		public EmptyStub()
		{
		}
		
		@Override
		public RegionRef getRef()
		{
			return null;
		}
		
		@Override
		public String getMatchingText(Locale locale)
		{
			throw new IllegalStateException("Empty region");
		}
		
		@Override
		public boolean isInsideOf(RegionRef regionRef)
		{
			return false;
		}
		
		@Override
		public boolean hasType(RegionType type)
		{
			return false;
		}
		
		@Override
		public boolean hasZipCode(String zipCode)
		{
			return false;
		}
		
		@Override
		public RegionNode parent(RegionType type)
		{
			return RegionNode.empty();
		}
		
		@Override
		public Region build(Locale locale)
		{
			return null;
		}
		
		@Override
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		throws GeographyException
		{
			return Collections.emptySet();
		}

		@Override
		public Set<RouteNode> routes(RouteConstraint constraint)
		throws GeographyException
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
		private RegionRef ref;
		private RegionType type;
		private Set<RegionRef> inside;
		private Set<String> zipCodes;
		private LocalizedValue displayName;
		
		public DefaultStub(NodeManager manager, RegionRef ref, RegionType type,
				Set<RegionRef> inside, Set<String> zipCodes,
				LocalizedValue displayName)
		{
			this.manager = manager;
			this.ref = ref;
			this.type = type;
			this.inside = inside;
			this.zipCodes = zipCodes;
			this.displayName = displayName;
		}
		
		@Override
		public RegionRef getRef()
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
		public boolean hasType(RegionType type)
		{
			return type.equals(this.type);
		}
		
		@Override
		public boolean hasZipCode(String zipCode)
		throws GeographyException
		{
			if (zipCodes.contains(zipCode))
				return true;
			for (RegionNode node : manager.regions(ref.getCountryCode()))
				if (node.isInsideOf(ref) && node.hasZipCode(zipCode))
				{
					zipCodes.add(zipCode);
					return true;
				}
			for (RouteNode node : manager.routes(ref.getCountryCode()))
				if (node.isInsideOf(ref) && node.hasZipCode(zipCode))
				{
					zipCodes.add(zipCode);
					return true;
				}
			return false;
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
		public Region build(Locale locale)
		{
			String displayNameText = displayName.getText(locale);
			return new Region(ref, displayNameText);
		}
		
		@Override
		public Set<RegionNode> regions(RegionType type,
				RegionConstraint constraint)
		throws GeographyException
		{
			Set<RegionNode> result = new HashSet<>();
			for (RegionNode node : manager.regions(ref.getCountryCode()))
				if (node.hasType(type) && node.isInsideOf(ref)
						&& constraint.accept(node))
					result.add(node);
			return result;
		}

		@Override
		public Set<RouteNode> routes(RouteConstraint constraint)
		throws GeographyException
		{
			Set<RouteNode> result = new HashSet<>();
			for (RouteNode node : manager.routes(ref.getCountryCode()))
				if (node.isInsideOf(ref) && constraint.accept(node))
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

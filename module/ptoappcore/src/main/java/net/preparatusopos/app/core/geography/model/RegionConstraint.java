package net.preparatusopos.app.core.geography.model;

import net.preparatusopos.app.domain.GeographyException;

public class RegionConstraint
{
	private Constraint constraint;
	
	public RegionConstraint()
	{
		constraint = new EmptyConstraint();
	}
	
	public RegionConstraint(String zipCode)
	{
		constraint = new ZipCodeConstraint(zipCode);
	}
	
	public boolean accept(RegionNode node)
	throws GeographyException
	{
		return constraint.accept(node);
	}
	
	private interface Constraint
	{
		public boolean accept(RegionNode node)
		throws GeographyException;
	}
	
	private static class EmptyConstraint
	implements Constraint
	{
		public EmptyConstraint()
		{
		}

		@Override
		public boolean accept(RegionNode node)
		{
			return true;
		}
	}
	
	private static class ZipCodeConstraint
	implements Constraint
	{
		private String zipCode;
		
		public ZipCodeConstraint(String zipCode)
		{
			this.zipCode = zipCode;
		}

		@Override
		public boolean accept(RegionNode node)
		throws GeographyException
		{
			return node.hasZipCode(zipCode);
		}
	}
}

package net.preparatusopos.app.core.geography.model;

public class RouteConstraint
{
	private Constraint constraint;
	
	public RouteConstraint()
	{
		constraint = new EmptyConstraint();
	}
	
	public RouteConstraint(String zipCode)
	{
		constraint = new ZipCodeConstraint(zipCode);
	}
	
	public boolean accept(RouteNode node)
	{
		return constraint.accept(node);
	}
	
	private interface Constraint
	{
		public boolean accept(RouteNode node);
	}
	
	private static class EmptyConstraint
	implements Constraint
	{
		public EmptyConstraint()
		{
		}

		@Override
		public boolean accept(RouteNode node)
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
		public boolean accept(RouteNode node)
		{
			return node.hasZipCode(zipCode);
		}
	}
}

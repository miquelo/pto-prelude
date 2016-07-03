package net.preparatusopos.app.core.geography.model;

import java.util.HashSet;
import java.util.Set;

import net.preparatusopos.app.core.geography.model.internal.InternalNodeManager;
import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.GeographyException;

public class World
{
	private NodeManager manager;
	
	public World(NodeManager manager)
	{
		this.manager = manager;
	}
	
	public Set<CountryNode> countries(CountryConstraint constraint)
	throws GeographyException
	{
		Set<CountryNode> result = new HashSet<>();
		for (CountryNode node : manager.countries())
			if (constraint.accept(node))
				result.add(node);
		return result;
	}
	
	public CountryNode country(String code)
	throws GeographyException
	{
		for (CountryNode node : manager.countries())
			if (node.hasRef(new CountryRef(code)))
				return node;
		return CountryNode.empty();
	}
	
	public static World internal()
	{
		return new World(new InternalNodeManager());
	}
}

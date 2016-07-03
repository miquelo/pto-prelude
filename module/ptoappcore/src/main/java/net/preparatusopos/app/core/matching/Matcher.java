package net.preparatusopos.app.core.matching;

import java.io.Serializable;
import java.util.Set;

import net.preparatusopos.app.core.matching.algorithms.SimpleMatcherAlgorithm;
import net.preparatusopos.app.domain.InputMatch;
import net.preparatusopos.app.domain.InputMatches;

public class Matcher
{
	private MatcherAlgorithm algorithm;
	
	private Matcher(MatcherAlgorithm algorithm)
	{
		this.algorithm = algorithm;
	}
	
	public <C, V extends Serializable> InputMatches<V>
			match(Set<? extends MatchingSource<C, V>> sourceSet, C context,
			String input)
	{
		InputMatches<V> result = new InputMatches<>();
		for (MatchingSource<C, V> source : sourceSet)
		{
			String matchingText = source.getMatchingText(context);
			V value = source.build(context);
			String[] parts = algorithm.match(matchingText);
			result.addValue(new InputMatch<V>(value, parts));
		}
		return result;
	}
	
	public static Matcher simple()
	{
		return new Matcher(new SimpleMatcherAlgorithm());
	}
}

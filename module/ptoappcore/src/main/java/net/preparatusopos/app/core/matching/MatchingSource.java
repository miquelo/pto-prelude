package net.preparatusopos.app.core.matching;

import java.io.Serializable;

public interface MatchingSource<C, V extends Serializable>
{
	public String getMatchingText(C context);
	
	public V build(C context);
}

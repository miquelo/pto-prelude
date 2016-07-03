package net.preparatusopos.app.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputMatches<T extends Serializable>
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<InputMatch<T>> values;
	
	public InputMatches()
	{
		values = new ArrayList<>();
	}

	public List<InputMatch<T>> getValues()
	{
		return values;
	}

	public void setValues(List<InputMatch<T>> values)
	{
		this.values = values;
	}
	
	public void addValue(InputMatch<T> value)
	{
		values.add(value);
	}
}

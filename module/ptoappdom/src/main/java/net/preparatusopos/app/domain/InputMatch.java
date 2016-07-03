package net.preparatusopos.app.domain;

import java.io.Serializable;

public class InputMatch<T extends Serializable>
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private T value;
	private String[] parts;
	
	public InputMatch()
	{
		this(null, new String[0]);
	}
	
	public InputMatch(T value, String[] parts)
	{
		this.value = value;
		this.parts = parts;
	}
	
	public int getScore()
	{
		int score = 0;
		int i = 0;
		for (String part : parts)
			if (i % 2 == 1)
				score += part.length();
		return score;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public String[] getParts()
	{
		return parts;
	}

	public void setParts(String[] parts)
	{
		this.parts = parts;
	}
}

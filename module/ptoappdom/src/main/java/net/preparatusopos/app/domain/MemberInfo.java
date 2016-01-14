package net.preparatusopos.app.domain;

import java.io.Serializable;

public class MemberInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String surname;
	
	public MemberInfo()
	{
		name = null;
		surname = null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}
}

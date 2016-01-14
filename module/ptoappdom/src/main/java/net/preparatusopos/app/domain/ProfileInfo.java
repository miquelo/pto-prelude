package net.preparatusopos.app.domain;

import java.io.Serializable;

public class ProfileInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ProfileType type;
	
	public ProfileInfo()
	{
		type = null;
	}

	public ProfileType getType()
	{
		return type;
	}

	public void setType(ProfileType type)
	{
		this.type = type;
	}
}

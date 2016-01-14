package net.preparatusopos.app.domain;

import java.io.Serializable;

public class ProfileRequestResult
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ProfileInfo info;
	
	public ProfileRequestResult()
	{
		info = null;
	}

	public ProfileInfo getInfo()
	{
		return info;
	}

	public void setInfo(ProfileInfo info)
	{
		this.info = info;
	}
}

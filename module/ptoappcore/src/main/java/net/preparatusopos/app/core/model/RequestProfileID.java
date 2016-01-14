package net.preparatusopos.app.core.model;

import java.io.Serializable;

public class RequestProfileID
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long target;
	private int profileType;
	
	public RequestProfileID()
	{
		this(0l, 0);
	}
	
	public RequestProfileID(long target, int profileType)
	{
		this.target = target;
		this.profileType = profileType;
	}
	
	public long getTarget()
	{
		return target;
	}

	public void setTarget(long target)
	{
		this.target = target;
	}

	public int getProfileType()
	{
		return profileType;
	}

	public void setProfileType(int profileType)
	{
		this.profileType = profileType;
	}

	@Override
	public int hashCode()
	{
		return (int) (target % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RequestProfileID)
		{
			RequestProfileID id = (RequestProfileID) obj;
			return target == id.target && profileType == id.profileType;
		}
		return false;
	}
}

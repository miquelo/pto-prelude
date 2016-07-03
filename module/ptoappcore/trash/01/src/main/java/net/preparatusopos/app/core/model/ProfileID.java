package net.preparatusopos.app.core.model;

import java.io.Serializable;

public class ProfileID
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long ownerUID;
	private int type;
	
	public ProfileID()
	{
		this(0l, 0);
	}
	
	public ProfileID(long ownerUID, int type)
	{
		this.ownerUID = ownerUID;
		this.type = type;
	}

	public long getOwnerUID()
	{
		return ownerUID;
	}

	public void setOwnerUID(long ownerUID)
	{
		this.ownerUID = ownerUID;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		return (int) (ownerUID % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ProfileID)
		{
			ProfileID id = (ProfileID) obj;
			return ownerUID == id.ownerUID && type == id.type;
		}
		return false;
	}
}
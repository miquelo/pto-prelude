package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * Profile info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class ProfileInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ProfileType type;
	
	@ConstructorProperties({
		"type"
	})
	public ProfileInfo(ProfileType type)
	{
		this.type = type;
	}

	public ProfileType getType()
	{
		return type;
	}
	
	@Override
	public int hashCode()
	{
		return type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ProfileInfo)
		{
			ProfileInfo info = (ProfileInfo) obj;
			return type.equals(info.type);
		}
		return false;
	}
}

package net.preparatusopos.app.domain;

import java.io.Serializable;

/**
 * Profile request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public abstract class ProfileRequest
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ProfileType type;
	
	protected ProfileRequest(ProfileType type)
	{
		this.type = type;
	}
	
	public abstract ProfileRequestType getRequestType();
	
	public ProfileType getType()
	{
		return type;
	}
	
	public ProfileRequest withoutSpecialization()
	{
		return clone(type.withoutSpecialization());
	}
	
	@Override
	public int hashCode()
	{
		return type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ProfileRequest)
		{
			ProfileRequest request = (ProfileRequest) obj;
			return type.equals(request.type);
		}
		return false;
	}
	
	protected abstract ProfileRequest clone(ProfileType profileType);
}

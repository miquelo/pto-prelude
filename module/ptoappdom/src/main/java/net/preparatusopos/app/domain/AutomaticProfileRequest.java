package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;

/**
 * Automatic profile request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class AutomaticProfileRequest
extends ProfileRequest
{
	private static final long serialVersionUID = 1L;

	@ConstructorProperties({
		"type"
	})
	public AutomaticProfileRequest(ProfileType type)
	{
		super(type);
	}

	@Override
	public ProfileRequestType getRequestType()
	{
		return ProfileRequestType.AUTOMATIC;
	}

	@Override
	protected ProfileRequest clone(ProfileType profileType)
	{
		return new AutomaticProfileRequest(profileType);
	}
}

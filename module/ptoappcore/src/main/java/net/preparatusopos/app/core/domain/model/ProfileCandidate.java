package net.preparatusopos.app.core.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILECAND"
)
@DiscriminatorValue(Profile.ROLE_CANDIDATE)
public class ProfileCandidate
extends Profile
{
	public ProfileCandidate()
	{
	}

	@Override
	protected String getRoleAsString()
	{
		return Profile.ROLE_CANDIDATE;
	}

	@Override
	protected Profile cloneRole()
	{
		return new ProfileCandidate();
	}
}

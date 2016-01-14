package net.preparatusopos.app.core.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILECAND"
)
@DiscriminatorValue(Profile.TYPE_CANDIDATE)
public class ProfileCandidate
extends Profile
{
	public ProfileCandidate()
	{
	}

	@Override
	protected String getTypeAsString()
	{
		return Profile.TYPE_CANDIDATE;
	}
}
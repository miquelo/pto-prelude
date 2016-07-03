package net.preparatusopos.app.core.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(Profile.TYPE_ES_JUDGE)
public class ProfileTrainerESJudge
extends ProfileTrainer
{
	public ProfileTrainerESJudge()
	{
	}

	@Override
	protected String getTypeAsString()
	{
		return Profile.TYPE_ES_JUDGE;
	}
}
package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.ProfileTrainer;

@StaticMetamodel(ProfileTrainer.class)
public class ProfileTrainer_
extends Profile_
{
	public static volatile ListAttribute<ProfileTrainer, String> locationRefs;
}
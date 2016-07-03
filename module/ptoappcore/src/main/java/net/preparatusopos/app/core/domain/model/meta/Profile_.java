package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.Member;
import net.preparatusopos.app.core.domain.model.Profile;

@StaticMetamodel(Profile.class)
public class Profile_
extends ProfileSpecs_
{
	public static volatile SingularAttribute<Profile, Long> ref;
	public static volatile SingularAttribute<Profile, Member> owner;
}
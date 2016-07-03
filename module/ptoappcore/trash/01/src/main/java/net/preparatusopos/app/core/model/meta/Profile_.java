package net.preparatusopos.app.core.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.model.Profile;

@StaticMetamodel(Profile.class)
public class Profile_
{
	public static volatile SingularAttribute<Profile, Member> owner;
	public static volatile SingularAttribute<Profile, Integer> type;
}
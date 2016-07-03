package net.preparatusopos.app.core.model.meta;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.model.Profile;

@StaticMetamodel(Member.class)
public class Member_
{
	public static volatile SingularAttribute<Member, Long> UID;
	public static volatile SingularAttribute<Member, Date> creation;
	public static volatile SingularAttribute<Member, String> name;
	public static volatile SingularAttribute<Member, String> surname;
	public static volatile SetAttribute<Member, Profile> profiles;
}
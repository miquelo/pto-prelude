package net.preparatusopos.app.core.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.Member;
import net.preparatusopos.app.core.model.RequestProfile;

@StaticMetamodel(RequestProfile.class)
public class RequestProfile_
extends Request_
{
	public static volatile SingularAttribute<RequestProfile, Member> target;
	public static volatile SingularAttribute<RequestProfile, Integer>
			profileType;
}
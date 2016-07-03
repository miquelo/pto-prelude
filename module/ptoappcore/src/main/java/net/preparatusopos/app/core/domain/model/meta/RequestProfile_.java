package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.RequestProfile;
import net.preparatusopos.app.domain.ProfileType;

@StaticMetamodel(RequestProfile.class)
public class RequestProfile_
extends ProfileSpecs_
{
	public static volatile SingularAttribute<RequestProfile, Long> ref;
	public static volatile SingularAttribute<RequestProfile, ProfileType> type;
}
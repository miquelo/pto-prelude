package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.Member;
import net.preparatusopos.app.core.domain.model.RequestProfile;
import net.preparatusopos.app.core.domain.model.RequestProfileToken;

@StaticMetamodel(RequestProfileToken.class)
public class RequestProfileToken_
extends AuthorizationToken_
{
	public static volatile SingularAttribute<RequestProfileToken, Member>
			owner;
	public static volatile SingularAttribute<RequestProfileToken,
			RequestProfile> request;
}
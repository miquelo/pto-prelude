package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.AuthorizationToken;

@StaticMetamodel(AuthorizationToken.class)
public class AuthorizationToken_
{
	public static volatile SingularAttribute<AuthorizationToken, Long> ref;
	public static volatile SingularAttribute<AuthorizationToken, Integer> type;
	public static volatile SingularAttribute<AuthorizationToken, byte[]> value;
}

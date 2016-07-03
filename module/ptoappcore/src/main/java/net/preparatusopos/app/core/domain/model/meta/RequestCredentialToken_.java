package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.Member;
import net.preparatusopos.app.core.domain.model.RequestCredential;
import net.preparatusopos.app.core.domain.model.RequestCredentialToken;

@StaticMetamodel(RequestCredentialToken.class)
public class RequestCredentialToken_
extends AuthorizationToken_
{
	public static volatile SingularAttribute<RequestCredentialToken, Member>
			owner;
	public static volatile SingularAttribute<RequestCredentialToken,
			RequestCredential> request;
}
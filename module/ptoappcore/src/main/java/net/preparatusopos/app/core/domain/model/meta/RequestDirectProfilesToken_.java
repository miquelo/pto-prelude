package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.RequestCredential;
import net.preparatusopos.app.core.domain.model.RequestDirectProfilesToken;
import net.preparatusopos.app.core.domain.model.RequestProfile;

@StaticMetamodel(RequestDirectProfilesToken.class)
public class RequestDirectProfilesToken_
extends AuthorizationToken_
{
	public static volatile SingularAttribute<RequestDirectProfilesToken,
			RequestCredential> credential;
	public static volatile SetAttribute<RequestDirectProfilesToken,
			RequestProfile> profiles;
}
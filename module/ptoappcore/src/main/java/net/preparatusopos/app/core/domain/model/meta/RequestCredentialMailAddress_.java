package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.RequestCredentialMailAddress;

@StaticMetamodel(RequestCredentialMailAddress.class)
public class RequestCredentialMailAddress_
extends RequestCredential_
{
	public static volatile SingularAttribute<RequestCredentialMailAddress,
			String> mailAddress;
	public static volatile SingularAttribute<RequestCredentialMailAddress,
			byte[]> password;
}
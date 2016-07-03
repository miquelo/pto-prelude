package net.preparatusopos.app.core.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.RequestCredentialMail;

@StaticMetamodel(RequestCredentialMail.class)
public class RequestMemberMail_
extends Request_
{
	public static volatile SingularAttribute<RequestCredentialMail, String>
			mailAddress;
}
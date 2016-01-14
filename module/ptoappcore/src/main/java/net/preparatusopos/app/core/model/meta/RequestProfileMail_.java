package net.preparatusopos.app.core.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.RequestProfileMail;

@StaticMetamodel(RequestProfileMail.class)
public class RequestProfileMail_
extends RequestProfile_
{
	public static volatile SingularAttribute<RequestProfileMail, String>
			mailAddress;
}
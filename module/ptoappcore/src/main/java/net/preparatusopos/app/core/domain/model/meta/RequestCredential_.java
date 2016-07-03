package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.RequestCredential;

@StaticMetamodel(RequestCredential.class)
public class RequestCredential_
{
	public static volatile SingularAttribute<RequestCredential, Long> ref;
	public static volatile SingularAttribute<RequestCredential, Integer> type;
}
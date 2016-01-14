package net.preparatusopos.app.core.model.meta;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.model.Request;

@StaticMetamodel(Request.class)
public class Request_
{
	public static volatile SingularAttribute<Request, byte[]> token;
	public static volatile SingularAttribute<Request, Date> creation;
}
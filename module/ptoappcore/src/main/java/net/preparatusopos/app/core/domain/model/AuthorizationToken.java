package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.DatatypeConverter;

import net.preparatusopos.app.core.domain.model.meta.AuthorizationToken_;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.Token;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_AUTHTOKEN"
)
@Inheritance(
	strategy=InheritanceType.JOINED
)
@DiscriminatorColumn(
	name="CL_TYPE",
	discriminatorType=DiscriminatorType.INTEGER
)
public abstract class AuthorizationToken
{
	public static final String TYPE_CREDENTIAL = "1";
	public static final String TYPE_PROFILE = "2";
	public static final String TYPE_DIRECT_PROFILES = "3";
	
	private long ref;
	private byte[] value;
	
	public AuthorizationToken()
	{
		ref = -1l;
		value = null;
	}

	@Id
	@GeneratedValue(
		strategy=GenerationType.IDENTITY
	)
	@Column(
		name="CL_REF"
	)
	public long getRef()
	{
		return ref;
	}
	
	public void setRef(long ref)
	{
		this.ref = ref;
	}
	
	@Column(
		name="CL_TYPE"
	)
	public int getType()
	{
		return Integer.parseInt(getTypeAsString());
	}
	
	public void setType(int type)
	{
		// XXX Throw an exception?
	}

	@Column(
		name="CL_VALUE"
	)
	public byte[] getValue()
	{
		return value;
	}

	public void setValue(byte[] value)
	{
		this.value = value;
	}
	
	@Transient
	public String getHexValue()
	{
		return DatatypeConverter.printHexBinary(getValue());
	}

	@Override
	public int hashCode()
	{
		return (int) (ref % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof AuthorizationToken)
		{
			AuthorizationToken token = (AuthorizationToken) obj;
			return ref == token.ref;
		}
		return false;
	}
	
	public static <T extends AuthorizationToken> T find(EntityManager em,
			Class<T> type, Token token)
	throws MembershipException
	{
		try
		{
			if (token.isEmpty())
				throw new MembershipException("Token is empty");
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(type);
			Root<T> root = cq.from(type);
			cq.select(root);
			cq.where(cb.equal(root.get(AuthorizationToken_.value),
					token.getValue()));
			TypedQuery<T> q = em.createQuery(cq);
			return q.getSingleResult();
		}
		catch (NoResultException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Token ").append(token).append(" was not found");
			throw new MembershipException(msg.toString());
		}
	}
	
	protected abstract String getTypeAsString();
}
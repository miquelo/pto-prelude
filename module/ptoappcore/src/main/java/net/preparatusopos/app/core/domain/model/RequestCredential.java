package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_REQCRED"
)
@Inheritance(
	strategy=InheritanceType.SINGLE_TABLE
)
@DiscriminatorColumn(
	name="CL_TYPE",
	discriminatorType=DiscriminatorType.INTEGER
)
public abstract class RequestCredential
{
	public static final String TYPE_MAIL_ADDRESS = "1";
	public static final String TYPE_EXTERNAL_UID = "2";
	
	private long ref;
	
	public RequestCredential()
	{
		ref = 0l;
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
		// TODO Throw an exception?
	}

	@Override
	public int hashCode()
	{
		return (int) (ref % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RequestCredential)
		{
			RequestCredential reqCred = (RequestCredential) obj;
			return ref == reqCred.ref;
		}
		return false;
	}
	
	protected abstract String getTypeAsString();
}
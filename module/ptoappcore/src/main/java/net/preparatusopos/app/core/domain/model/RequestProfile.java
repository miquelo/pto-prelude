package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.preparatusopos.app.domain.ProfileType;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_REQPROF"
)
public class RequestProfile
extends ProfileSpecs
{
	private long ref;
	private ProfileType.Role role;
	
	public RequestProfile()
	{
		ref = 0l;
		role = null;
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
	
	public void setType(ProfileType type)
	{
		setRole(type.getRole());
		setCountry(type.getCountry());
		setField(type.getField());
		setSpecialization(type.getSpecialization());
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(
		name="CL_ROLE"
	)
	@Override
	public ProfileType.Role getRole()
	{
		return role;
	}

	@Override
	public void setRole(ProfileType.Role role)
	{
		this.role = role;
	}

	@Override
	public int hashCode()
	{
		return (int) (ref % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RequestProfile)
		{
			RequestProfile reqProf = (RequestProfile) obj;
			return ref != 0l && ref == reqProf.ref;
		}
		return false;
	}
}
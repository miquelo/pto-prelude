package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.preparatusopos.app.domain.ProfileType;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILE"
)
@Inheritance(
	strategy=InheritanceType.JOINED
)
@DiscriminatorColumn(
	name="CL_ROLE",
	discriminatorType=DiscriminatorType.INTEGER
)
public abstract class Profile
extends ProfileSpecs
{
	public static final String ROLE_CANDIDATE = "0";
	public static final String ROLE_TRAINER = "1";
	
	private long ref;
	private Member owner;
	
	protected Profile()
	{
		ref = 0l;
		owner = null;
	}
	
	@Id
	@GeneratedValue(
		strategy=GenerationType.IDENTITY
	)
	@Column(
		name="CL_REF"
	)
	public Long getRef()
	{
		return ref;
	}
	
	public void setRef(Long ref)
	{
		this.ref = ref;
	}
	
	@ManyToOne
	@JoinColumn(
		name="CL_OWNERUID",
		nullable=false
	)
	public Member getOwner()
	{
		return owner;
	}
	
	public void setOwner(Member owner)
	{
		this.owner = owner;
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(
		name="CL_ROLE"
	)
	@Override
	public ProfileType.Role getRole()
	{
		int ordinal = Integer.parseInt(getRoleAsString());
		return ProfileType.Role.values()[ordinal];
	}
	
	@Override
	public void setRole(ProfileType.Role role)
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
		if (obj != null && obj instanceof Profile)
		{
			Profile profile = (Profile) obj;
			return ref != 0l && ref != profile.ref || super.equals(obj);
		}
		return false;
	}
	
	protected abstract String getRoleAsString();
	
	protected abstract Profile cloneRole();
}
package net.preparatusopos.app.core.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILE"
)
@Inheritance(
	strategy=InheritanceType.JOINED
)
@DiscriminatorColumn(
	name="CL_TYPE",
	discriminatorType=DiscriminatorType.INTEGER
)
@IdClass(ProfileID.class)
public abstract class Profile
{
	public static final String TYPE_CANDIDATE = "1";
	public static final String TYPE_ES_JUDGE = "101";
	
	private Member owner;
	
	protected Profile()
	{
		owner = null;
	}

	@Id
	@Column(
		name="CL_OWNERUID",
		nullable=false,
		insertable=false,
		updatable=false
	)
	public long getOwnerUID()
	{
		return owner == null ? 0l : owner.getUID();
	}
	
	public void setOwnerUID(long ownerUID)
	{
		if (owner != null)
			owner.setUID(ownerUID);
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
	
	@Id
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
		return owner == null ? 0 : owner.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Profile)
		{
			Profile profile = (Profile) obj;
			return owner != null && owner.equals(profile.owner) &&
					getType() == profile.getType();
		}
		return false;
	}
	
	protected abstract String getTypeAsString();
}
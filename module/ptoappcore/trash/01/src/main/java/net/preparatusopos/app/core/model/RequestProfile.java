package net.preparatusopos.app.core.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
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
	name="TB_REQPROFILE"
)
@Inheritance(
	strategy=InheritanceType.SINGLE_TABLE
)
@DiscriminatorColumn(
	name="CL_TYPE"
)
@IdClass(RequestProfileID.class)
public abstract class RequestProfile
extends Request
{
	public static final String TYPE_MAIL = "1";
	
	private Member target;
	private int profileType;
	
	public RequestProfile()
	{
		target = null;
		profileType = -1;
	}

	@Id
	@ManyToOne
	@JoinColumn(
		name="CL_TARGETUID",
		referencedColumnName="CL_UID"
	)
	public Member getTarget()
	{
		return target;
	}

	public void setTarget(Member target)
	{
		this.target = target;
	}
	
	@Id
	@Column(
		name="CL_PROFTYPE"
	)
	public int getProfileType()
	{
		return profileType;
	}

	public void setProfileType(int profileType)
	{
		this.profileType = profileType;
	}

	@Override
	public int hashCode()
	{
		return target == null ? 0 : target.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RequestProfile)
		{
			RequestProfile profile = (RequestProfile) obj;
			return target != null && target.equals(profile.target) &&
					profileType == profile.profileType;
		}
		return false;
	}
}

package net.preparatusopos.app.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.preparatusopos.app.core.AccreditatorException;
import net.preparatusopos.app.domain.ProfileInfo;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_MEMBER"
)
public class Member
{
	private long uid;
	private String name;
	private String surname;
	private Set<Profile> profiles;
	
	public Member()
	{
		uid = -1;
		name = null;
		surname = null;
		profiles = new HashSet<>();
	}

	@Id
	@GeneratedValue(
		strategy=GenerationType.IDENTITY
	)
	@Column(
		name="CL_UID"
	)
	public long getUID()
	{
		return uid;
	}
	
	public void setUID(long uid)
	{
		this.uid = uid;
	}

	@Column(
		name="CL_NAME"
	)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(
		name="CL_SURNAME"
	)
	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	@OneToMany(
		mappedBy="owner",
		cascade=CascadeType.ALL,
		fetch=FetchType.LAZY,
		orphanRemoval=true
	)
	public Set<Profile> getProfiles()
	{
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles)
	{
		this.profiles = profiles;
	}
	
	public void addProfile(ProfileInfo profileInfo)
	throws AccreditatorException
	{
		getProfiles().add(newProfile(profileInfo));
	}
	
	public boolean removeProfile(int type)
	{
		boolean removed = false;
		Set<Profile> newProfiles = new HashSet<>();
		for (Profile profile : getProfiles())
			if (profile.getType() == type)
				removed = true;
			else
				newProfiles.add(profile);
		setProfiles(newProfiles);
		return removed;
	}

	@Override
	public int hashCode()
	{
		return (int) (uid % 200l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Member)
		{
			Member member = (Member) obj;
			return uid == member.uid;
		}
		return false;
	}
	
	private Profile newProfile(ProfileInfo profileInfo)
	throws AccreditatorException
	{
		switch (profileInfo.getType())
		{
			case CANDIDATE:
			return newProfileCandidate(profileInfo);
			
			case SPAIN_JUDGE:
			return newProfileTrainerESJudge(profileInfo);
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Unsupported profile type '");
			msg.append(profileInfo.getType()).append("'");
			throw new AccreditatorException(msg.toString());
		}
	}
	
	private Profile newProfileCandidate(ProfileInfo profileInfo)
	{
		ProfileCandidate profile = new ProfileCandidate();
		profile.setOwner(this);
		return profile;
	}
	
	private Profile newProfileTrainerESJudge(ProfileInfo profileInfo)
	{
		ProfileTrainerESJudge profile = new ProfileTrainerESJudge();
		profile.setOwner(this);
		return profile;
	}
}
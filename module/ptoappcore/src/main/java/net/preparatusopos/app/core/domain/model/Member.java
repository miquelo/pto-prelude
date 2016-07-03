package net.preparatusopos.app.core.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.preparatusopos.app.core.file.PrivateFileStorage;
import net.preparatusopos.app.core.util.Util;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.NoSuchMemberException;
import net.preparatusopos.app.domain.ProfileInfo;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.ReferencedMemberInfo;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_MEMBER"
)
public class Member
{
	private long uid;
	private String ref;
	private ManagedFile photoFile;
	private String name;
	private String surname;
	private Set<Profile> profiles;
	
	public Member()
	{
		uid = 0l;
		ref = null;
		photoFile = null;
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
		name="CL_REF"
	)
	public String getRef()
	{
		return ref;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}

	@JoinColumn(
		name="CL_PHOTOFILE"
	)
	@ManyToOne(
		cascade=CascadeType.ALL,
		fetch=FetchType.LAZY
	)
	public ManagedFile getPhotoFile()
	{
		return photoFile;
	}

	public void setPhotoFile(ManagedFile photoFile)
	{
		this.photoFile = photoFile;
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
	
	public ReferencedMemberInfo getMemberInfo(PrivateFileStorage fileStorage)
	throws FileStorageException
	{
		StoredFile storedFile = null;
		if (photoFile != null)
		{
			String providerName = fileStorage.getProviderName(
					photoFile.transport());
			storedFile = photoFile.getStoredFile(providerName);
		}
		ReferencedMemberInfo memberInfo = new ReferencedMemberInfo(ref,
				storedFile);
		memberInfo.setName(name);
		memberInfo.setSurname(surname);
		return memberInfo;
	}
	
	public void setMemberInfo(MemberInfo memberInfo)
	{
		name = memberInfo.getName();
		surname = memberInfo.getSurname();
	}
	
	public void addProfile(RequestProfile request, ProfileInfo profileInfo)
	throws MembershipException
	{
		getProfiles().add(newProfile(request, profileInfo));
	}
	
	public boolean removeProfile(ProfileType type)
	{
		boolean removed = false;
		Set<Profile> newProfiles = new HashSet<>();
		for (Profile profile : getProfiles())
			if (type.equals(profile.getType()))
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
	
	public static Member find(EntityManager em, PTOPrincipal principal)
	throws NoSuchMemberException
	{
		Member member = em.find(Member.class, principal.getUserID());
		if (member == null)
			throw new NoSuchMemberException(principal.getUserID());
		return member;
	}
	
	public static Member create()
	{
		Member member = new Member();
		member.setRef(Util.createRef(20));
		return member;
	}
	
	private Profile newProfile(RequestProfile reqProf, ProfileInfo info)
	throws MembershipException
	{
		ProfileType type = info.getType();
		if (type == null)
			type = reqProf.getType();
		else if (!type.equals(reqProf.getType()))
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Profile type mismatch: ");
			msg.append(type).append(" != ").append(reqProf.getType());
			throw new MembershipException(msg.toString());
		}
		
		Profile profile = null;
		switch (type.getRole())
		{
			case CANDIDATE:
			profile = new ProfileCandidate();
			break;
			
			case TRAINER:
			profile = new ProfileTrainer();
			break;
			
			default:
			StringBuilder msg = new StringBuilder();
			msg.append("Unsupported profile role '");
			msg.append(reqProf.getRole()).append("'");
			throw new MembershipException(msg.toString());
		}
		profile.setOwner(this);
		profile.setCountry(type.getCountry());
		profile.setField(type.getField());
		profile.setSpecialization(type.getSpecialization());
		return profile;
	}
}
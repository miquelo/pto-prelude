package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import net.preparatusopos.app.domain.ProfileType;

@MappedSuperclass
public abstract class ProfileSpecs
{
	public static final String ROLE_CANDIDATE = "0";
	public static final String ROLE_TRAINER = "1";
	
	private ProfileType.Country country;
	private ProfileType.Field field;
	private ProfileType.Specialization specialization;
	
	protected ProfileSpecs()
	{
		country = null;
		field = null;
		specialization = null;
	}
	
	@Transient
	public ProfileType getType()
	{
		return new ProfileType(getRole(), getCountry(), getField(),
				getSpecialization());
	}
	
	public abstract ProfileType.Role getRole();
	
	public abstract void setRole(ProfileType.Role role);
	
	@Enumerated(EnumType.ORDINAL)
	@Column(
		name="CL_COUNTRY"
	)
	public ProfileType.Country getCountry()
	{
		return country;
	}

	public void setCountry(ProfileType.Country country)
	{
		this.country = country;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(
		name="CL_FIELD"
	)
	public ProfileType.Field getField()
	{
		return field;
	}

	public void setField(ProfileType.Field field)
	{
		this.field = field;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(
		name="CL_SPECIAL"
	)
	public ProfileType.Specialization getSpecialization()
	{
		return specialization;
	}

	public void setSpecialization(ProfileType.Specialization specialization)
	{
		this.specialization = specialization;
	}
	
	@Override
	public int hashCode()
	{
		return getRole() == null ? 0 : getRole().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ProfileSpecs)
		{
			ProfileSpecs specs = (Profile) obj;
			return getRole() != null && getRole().equals(specs.getRole()) &&
					country != null && country.equals(specs.country) &&
					field != null && field.equals(specs.field) && (
					specialization == null && specs.specialization == null ||
					specialization != null && specialization.equals(
					specs.specialization));
		}
		return false;
	}
}
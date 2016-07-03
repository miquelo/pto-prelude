package net.preparatusopos.app.domain;

import java.io.Serializable;

public class ProfileType
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Role role;
	private Country country;
	private Field field;
	private Specialization specialization;
	
	public ProfileType()
	{
		this(null, null, null, null);
	}
	
	public ProfileType(Role role, Country country, Field field, Specialization
			specialization)
	{
		this.role = role;
		this.country = country;
		this.field = field;
		this.specialization = specialization;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}

	public Country getCountry()
	{
		return country;
	}

	public void setCountry(Country country)
	{
		this.country = country;
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public Specialization getSpecialization()
	{
		return specialization;
	}

	public void setSpecialization(Specialization specialization)
	{
		this.specialization = specialization;
	}
	
	public ProfileType withoutSpecialization()
	{
		return new ProfileType(role, country, field, null);
	}
	
	@Override
	public int hashCode()
	{
		return country == null ? 0 : country.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ProfileType)
		{
			ProfileType type = (ProfileType) obj;
			if (role == null || !role.equals(type.role))
				return false;
			if (country == null || !country.equals(type.country))
				return false;
			if (field == null || !field.equals(type.field))
				return false;
			return specialization == null
					? type.specialization == null
					: specialization.equals(type.specialization);
		}
		return false;
	}

	public static enum Role
	{
		CANDIDATE,
		TRAINER
	}
	
	public static enum Country
	{
		SPAIN("es");
		
		private String code;
		
		private Country(String code)
		{
			this.code = code;
		}
		
		public String getCode()
		{
			return code;
		}
	}
	
	public static enum Field
	{
		JUDICATURE,
		NOTARIAL
	}
	
	public static enum Specialization
	{
		COMMERCIAL
	}
}

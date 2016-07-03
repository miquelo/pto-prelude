package net.preparatusopos.app.ma.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import net.preparatusopos.app.domain.DirectProfilesRequest;
import net.preparatusopos.app.domain.MailCredentialRequest;
import net.preparatusopos.app.domain.MailDomainMatchProfileRequest;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.ProfileRequestHints;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.domain.ProfileType.Role;
import net.preparatusopos.app.ma.web.RegisterBean.ProfileCountryField;
import net.preparatusopos.app.ma.web.util.Util;

public class RegisterStateBegin
extends RegisterState
{
	private static final String STATE_NAME = "begin";
	
	private static final String TOKEN_REF = "/register.html?t=${token}";
	
	private ProfileType profileType;
	private String mailAddress;
	private String password;
	private List<String> mailDomains;
	private String mailUsername;
	private String mailDomain;
	
	public RegisterStateBegin(RegisterBean owner)
	{
		super(owner);
		profileType = new ProfileType();
		profileType.setRole(Role.CANDIDATE);
		profileType.setCountry(null);
		profileType.setField(null);
		mailAddress = null;
		password = null;
		mailDomains = null;
		mailUsername = null;
		mailDomain = null;
	}

	@Override
	public String getStateName()
	{
		return STATE_NAME;
	}
	
	@Override
	public ProfileType.Specialization[] getProfileSpecializations()
	{
		try
		{
			// TODO Cache needed!
			if (profileType.getCountry() == null
					|| profileType.getField() == null)
				return new ProfileType.Specialization[0];
			
			Set<ProfileType.Specialization> specializations =
					membership.availableSpecializations(
					profileType.getCountry(), profileType.getField());
			return specializations.toArray(
					new ProfileType.Specialization[specializations.size()]);
		}
		catch (MembershipException exception)
		{
			throw new FacesException(exception);
		}
	}
	
	@Override
	public ProfileType.Role getProfileRole()
	{
		return profileType.getRole();
	}

	@Override
	public void setProfileRole(ProfileType.Role profileRole)
	{
		profileType.setRole(profileRole);
	}
	
	@Override
	public ProfileCountryField getProfileCountryField()
	{
		ProfileType.Country country = profileType.getCountry();
		ProfileType.Field field = profileType.getField();
		if (country == null || field == null)
			return null;
		return new ProfileCountryField(country, field);
	}
	
	@Override
	public void setProfileCountryField(ProfileCountryField profileCountryField)
	{
		if (profileCountryField == null)
		{
			profileType.setCountry(null);
			profileType.setField(null);
		}
		else
		{
			profileType.setCountry(profileCountryField.getCountry());
			profileType.setField(profileCountryField.getField());
		}
	}
	
	@Override
	public ProfileType.Specialization getProfileSpecialization()
	{
		return profileType.getSpecialization();
	}
	
	@Override
	public void setProfileSpecialization(ProfileType.Specialization
			profileSpecialization)
	{
		profileType.setSpecialization(profileSpecialization);
	}
	
	@Override
	public String getMailAddress()
	{
		return mailAddress;
	}

	@Override
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}
	
	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@Override
	public List<String> getMailDomains()
	{
		try
		{
			if (mailDomains == null)
			{
				mailDomains = new ArrayList<>();
				
				Map<ProfileRequestType, List<ProfileRequestHints>>
				hintsMap = membership.fetchProfileRequestHints(profileType);
				// Only mail domain match for now
				List<ProfileRequestHints> hintsList = hintsMap.get(
						ProfileRequestType.MAIL_DOMAIN_MATCH);
				
				if (hintsList != null)
					for (ProfileRequestHints hints : hintsList)
						mailDomains.add(hints.getMailDomain());
			}
			return mailDomains;
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}

	@Override
	public void setMailDomains(List<String> mailDomains)
	{
		this.mailDomains = mailDomains;
	}
	
	@Override
	public String getMailUsername()
	{
		return mailUsername;
	}

	@Override
	public void setMailUsername(String mailUsername)
	{
		this.mailUsername = mailUsername;
	}

	@Override
	public String getMailDomain()
	{
		if (mailDomain != null)
			return mailDomain;
		if (!getMailDomains().isEmpty())
			return getMailDomains().get(0);
		return null;
	}

	@Override
	public void setMailDomain(String mailDomain)
	{
		this.mailDomain = mailDomain;
	}

	@Override
	public void init()
	{
	}
	
	@Override
	public void perform(AjaxBehaviorEvent event)
	{
		try
		{
			DirectProfilesRequest request = new DirectProfilesRequest();
			
			MailCredentialRequest credReq = new MailCredentialRequest();
			credReq.setPassword(getPassword() == null ? null
					: Util.passwordHash(getPassword().toCharArray()));
			request.setCredentialRequest(credReq);
			
			MailDomainMatchProfileRequest profReq =
					new MailDomainMatchProfileRequest(profileType);
			if (getProfileRole() == ProfileType.Role.CANDIDATE)
			{
				credReq.setMailAddress(getMailAddress());
				
				String[] parts = getMailAddress().split("@");
				profReq.setMailUsername(parts[0]);
				profReq.setMailDomain(parts[1]);
			}
			else
			{
				credReq.setMailAddress(String.format("%s@%s", getMailUsername(),
						getMailDomain()));
				
				profReq.setMailUsername(getMailUsername());
				profReq.setMailDomain(getMailDomain());
			}
			request.getValues().add(profReq.withoutSpecialization());
			request.getValues().add(profReq);
			
			membership.request(Util.getViewURI(TOKEN_REF), request);
			RegisterState newState = new RegisterStateRequested(owner);
			newState.setMailAddress(credReq.getMailAddress());
			changeState(newState);
		}
		catch (MembershipException exception)
		{
			// TODO Report error
			throw new AbortProcessingException(exception);
		}
	}
	
	@Override
	public void profileRoleChange(ValueChangeEvent event)
	{
		setMailDomains(null);
		setMailDomain(null);
		
		getProfileCountryFieldInput().resetValue();
		getProfileSpecializationInput().resetValue();
		getMailAddressInput().resetValue();
		getMailUsernameInput().resetValue();
		getMailDomainInput().resetValue();
		getPasswordInput().resetValue();
	}
	
	public void profileCountryFieldChange(ValueChangeEvent event)
	{
		setMailDomains(null);
		setMailDomain(null);
		
		getProfileSpecializationInput().resetValue();
		getMailAddressInput().resetValue();
		getMailUsernameInput().resetValue();
		getMailDomainInput().resetValue();
		getPasswordInput().resetValue();
	}
}

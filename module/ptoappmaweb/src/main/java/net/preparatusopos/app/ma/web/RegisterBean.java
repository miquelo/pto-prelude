package net.preparatusopos.app.ma.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;

import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.ma.web.util.Util;
import net.preparatusopos.tools.file.FileStorageException;

@ManagedBean(
	name="registerBean"
)
@ViewScoped
public class RegisterBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String TOKEN_HEX_PARAMETER_NAME = "t";
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private transient Membership membership;
	
	@ManagedProperty("#{userBean}")
	private UserBean userBean;

	private String tokenHex;
	private RegisterState state;
	private HtmlSelectOneMenu profileCountryFieldInput;
	private HtmlSelectOneMenu profileSpecializationInput;
	private HtmlInputText mailAddressInput;
	private HtmlInputSecret passwordInput;
	private HtmlInputText mailUsernameInput;
	private HtmlSelectOneMenu mailDomainInput;
	
	public RegisterBean()
	{
		membership = null;
		userBean = null;
		tokenHex = null;
		state = null;
		profileCountryFieldInput = null;
		profileSpecializationInput = null;
		mailAddressInput = null;
		passwordInput = null;
		mailUsernameInput = null;
		mailDomainInput = null;
	}

	public void setUserBean(UserBean userBean)
	{
		this.userBean = userBean;
	}

	public String getTokenHex()
	{
		return tokenHex;
	}

	public void setTokenHex(String tokenHex)
	{
		this.tokenHex = tokenHex;
	}

	public String getStateName()
	{
		return state.getStateName();
	}
	
	public ProfileCountryField[] getProfileCountryFields()
	{
		return new ProfileCountryField[] {
			new ProfileCountryField(ProfileType.Country.SPAIN,
					ProfileType.Field.JUDICATURE),
			new ProfileCountryField(ProfileType.Country.SPAIN,
					ProfileType.Field.NOTARIAL)
		};
	}
	
	public ProfileType.Specialization[] getProfileSpecializations()
	{
		return state.getProfileSpecializations();
	}
	
	public ProfileType.Role getProfileRole()
	{
		return state.getProfileRole();
	}
	
	public void setProfileRole(ProfileType.Role profileRole)
	{
		state.setProfileRole(profileRole);
	}
	
	public ProfileCountryField getProfileCountryField()
	{
		return state.getProfileCountryField();
	}
	
	public void setProfileCountryField(ProfileCountryField profileCountryField)
	{
		state.setProfileCountryField(profileCountryField);
	}
	
	public ProfileType.Specialization getProfileSpecialization()
	{
		return state.getProfileSpecialization();
	}
	
	public void setProfileSpecialization(ProfileType.Specialization
			profileSpecialization)
	{
		state.setProfileSpecialization(profileSpecialization);
	}

	public String getMailAddress()
	{
		return state.getMailAddress();
	}

	public void setMailAddress(String mailAddress)
	{
		state.setMailAddress(mailAddress);
	}

	public String getPassword()
	{
		return state.getPassword();
	}

	public void setPassword(String password)
	{
		state.setPassword(password);
	}
	
	public List<String> getMailDomains()
	{
		return state.getMailDomains();
	}

	public void setMailDomains(List<String> mailDomains)
	{
		state.setMailDomains(mailDomains);
	}

	public String getMailUsername()
	{
		return state.getMailUsername();
	}

	public void setMailUsername(String mailUsername)
	{
		state.setMailUsername(mailUsername);
	}

	public String getMailDomain()
	{
		return state.getMailDomain();
	}

	public void setMailDomain(String mailDomain)
	{
		state.setMailDomain(mailDomain);
	}
	
	public HtmlSelectOneMenu getProfileCountryFieldInput()
	{
		return profileCountryFieldInput;
	}

	public void setProfileCountryFieldInput(HtmlSelectOneMenu
			profileCountryFieldInput)
	{
		this.profileCountryFieldInput = profileCountryFieldInput;
	}

	public HtmlSelectOneMenu getProfileSpecializationInput()
	{
		return profileSpecializationInput;
	}

	public void setProfileSpecializationInput(HtmlSelectOneMenu
			profileSpecializationInput)
	{
		this.profileSpecializationInput = profileSpecializationInput;
	}

	public HtmlInputText getMailAddressInput()
	{
		return mailAddressInput;
	}

	public void setMailAddressInput(HtmlInputText mailAddressInput)
	{
		this.mailAddressInput = mailAddressInput;
	}

	public HtmlInputSecret getPasswordInput()
	{
		return passwordInput;
	}

	public void setPasswordInput(HtmlInputSecret passwordInput)
	{
		this.passwordInput = passwordInput;
	}

	public HtmlInputText getMailUsernameInput()
	{
		return mailUsernameInput;
	}

	public void setMailUsernameInput(HtmlInputText mailUsernameInput)
	{
		this.mailUsernameInput = mailUsernameInput;
	}

	public HtmlSelectOneMenu getMailDomainInput()
	{
		return mailDomainInput;
	}

	public void setMailDomainInput(HtmlSelectOneMenu mailDomainInput)
	{
		this.mailDomainInput = mailDomainInput;
	}
	
	@PostConstruct
	public void init()
	{
		setTokenHex(Util.getRequestParameter(TOKEN_HEX_PARAMETER_NAME));
		if (getTokenHex() == null)
			changeState(new RegisterStateBegin(this));
		else
			changeState(new RegisterStateVerified(this));
	}
	
	public void login(ComponentSystemEvent event)
	{
		try
		{
			state.login(userBean);
		}
		catch (ServletException | IOException | MembershipException |
				FileStorageException exception)
		{
			// TODO Report error
			throw new FacesException(exception);
		}
	}

	public void perform(AjaxBehaviorEvent event)
	{
		state.perform(event);
	}

	public void profileRoleChange(ValueChangeEvent event)
	{
		state.profileRoleChange(event);
	}
	
	public void profileCountryFieldChange(ValueChangeEvent event)
	{
		state.profileCountryFieldChange(event);
	}
	
	public void changeState(RegisterState state)
	{
		state.setMembership(membership);
		state.init();
		this.state = state;
	}
	
	public static class ProfileCountryField
	implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private ProfileType.Country country;
		private ProfileType.Field field;
		
		public ProfileCountryField()
		{
			country = null;
			field = null;
		}
		
		public ProfileCountryField(ProfileType.Country country,
				ProfileType.Field field)
		{
			this.country = country;
			this.field = field;
		}

		public ProfileType.Country getCountry()
		{
			return country;
		}

		public void setCountry(ProfileType.Country country)
		{
			this.country = country;
		}

		public ProfileType.Field getField()
		{
			return field;
		}

		public void setField(ProfileType.Field field)
		{
			this.field = field;
		}
		
		@Override
		public int hashCode()
		{
			return country == null ? 0 : country.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj != null && obj instanceof ProfileCountryField)
			{
				ProfileCountryField pcf = (ProfileCountryField) obj;
				return country != null && country.equals(pcf.country) &&
						field != null && field.equals(pcf.field);
			}
			return false;
		}
		
		@Override
		public String toString()
		{
			return String.format("%s_%s", country, field);
		}
	}
}

package net.preparatusopos.app.ma.web;

import java.io.IOException;
import java.util.List;

import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;

import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.ma.web.RegisterBean.ProfileCountryField;
import net.preparatusopos.tools.file.FileStorageException;

public abstract class RegisterState
{
	protected RegisterBean owner;
	protected Membership membership;
	
	protected RegisterState(RegisterBean owner)
	{
		this.owner = owner;
	}
	
	public void setMembership(Membership membership)
	{
		this.membership = membership;
	}
	
	public abstract String getStateName();
	
	public ProfileType.Specialization[] getProfileSpecializations()
	{
		throw new IllegalStateException();
	}
	
	public ProfileType.Role getProfileRole()
	{
		throw new IllegalStateException();
	}
	
	public void setProfileRole(ProfileType.Role profileRole)
	{
		throw new IllegalStateException();
	}
	
	public ProfileCountryField getProfileCountryField()
	{
		throw new IllegalStateException();
	}
	
	public void setProfileCountryField(ProfileCountryField profileCountryField)
	{
		throw new IllegalStateException();
	}
	
	public ProfileType.Specialization getProfileSpecialization()
	{
		throw new IllegalStateException();
	}
	
	public void setProfileSpecialization(ProfileType.Specialization
			profileSpecialization)
	{
		throw new IllegalStateException();
	}

	public String getMailAddress()
	{
		throw new IllegalStateException();
	}

	public void setMailAddress(String mailAddress)
	{
		throw new IllegalStateException();
	}

	public String getPassword()
	{
		throw new IllegalStateException();
	}

	public void setPassword(String password)
	{
		throw new IllegalStateException();
	}
	
	public List<String> getMailDomains()
	{
		throw new IllegalStateException();
	}

	public void setMailDomains(List<String> mailDomains)
	{
		throw new IllegalStateException();
	}

	public String getMailUsername()
	{
		throw new IllegalStateException();
	}

	public void setMailUsername(String mailUsername)
	{
		throw new IllegalStateException();
	}

	public String getMailDomain()
	{
		throw new IllegalStateException();
	}

	public void setMailDomain(String mailDomain)
	{
		throw new IllegalStateException();
	}
	
	public abstract void init();
	
	public void login(UserBean userBean)
	throws ServletException, IOException, MembershipException,
			FileStorageException
	{
		// Nothing to be done
	}

	public void perform(AjaxBehaviorEvent event)
	{
		throw new IllegalStateException();
	}
	
	public void profileRoleChange(ValueChangeEvent event)
	{
		throw new IllegalStateException();
	}
	
	public void profileCountryFieldChange(ValueChangeEvent event)
	{
		throw new IllegalStateException();
	}
	
	protected String getTokenHex()
	{
		return owner.getTokenHex();
	}
	
	protected HtmlSelectOneMenu getProfileCountryFieldInput()
	{
		return owner.getProfileCountryFieldInput();
	}

	protected HtmlSelectOneMenu getProfileSpecializationInput()
	{
		return owner.getProfileSpecializationInput();
	}

	protected HtmlInputText getMailAddressInput()
	{
		return owner.getMailAddressInput();
	}

	protected HtmlInputSecret getPasswordInput()
	{
		return owner.getPasswordInput();
	}

	protected HtmlInputText getMailUsernameInput()
	{
		return owner.getMailUsernameInput();
	}

	protected HtmlSelectOneMenu getMailDomainInput()
	{
		return owner.getMailDomainInput();
	}
	
	protected void changeState(RegisterState state)
	{
		owner.changeState(state);
	}
}
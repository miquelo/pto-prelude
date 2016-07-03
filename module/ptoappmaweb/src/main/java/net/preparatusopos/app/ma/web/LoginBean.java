package net.preparatusopos.app.ma.web;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletException;

import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.ma.web.util.Util;
import net.preparatusopos.tools.file.FileStorageException;

@ManagedBean(
	name="loginBean"
)
@ViewScoped
public class LoginBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final String STATUS_PARAM_NAME = "s";
	
	@ManagedProperty("#{userBean}")
	private UserBean userBean;
	
	private String status;
	private String mailAddress;
	private String password;
	
	public LoginBean()
	{
		userBean = null;
		status = null;
		mailAddress = null;
		password = null;
	}

	public void setUserBean(UserBean userBean)
	{
		this.userBean = userBean;
	}
	
	public String getStatus()
	{
		return status;
	}

	public String getMailAddress()
	{
		return mailAddress;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@PostConstruct
	public void init()
	{
		status = Util.getRequestParameter(STATUS_PARAM_NAME);
	}
	
	public void loginCheck(ComponentSystemEvent event)
	{
		try
		{
			if (userBean.isLogged())
				Util.redirectToHome();
		}
		catch (IOException exception)
		{
			// TODO Report error
			throw new FacesException(exception);
		}
	}

	public Object perform()
	{
		try
		{
			userBean.login(getMailAddress(), getPassword());
			return "login-success";
		}
		catch (ServletException | MembershipException
				| FileStorageException exception)
		{
			return "login-failure";
		}
	}
}

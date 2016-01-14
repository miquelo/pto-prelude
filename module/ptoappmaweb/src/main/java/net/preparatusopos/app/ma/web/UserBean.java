package net.preparatusopos.app.ma.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import net.preparatusopos.app.ma.web.util.Util;

@ManagedBean(
	name="userBean"
)
@SessionScoped
public class UserBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean logged;
	private String name;
	
	public UserBean()
	{
		logged = false;
		name = null;
	}

	public boolean isLogged()
	{
		return logged;
	}

	public String getName()
	{
		return name;
	}
	
	@PostConstruct
	public void init()
	{
		logged = Util.isLogged();
		name = "Irene";
	}
}

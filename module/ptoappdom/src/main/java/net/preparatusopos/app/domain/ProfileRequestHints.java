package net.preparatusopos.app.domain;

import java.io.Serializable;

public class ProfileRequestHints
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String mailDomain;
	
	public ProfileRequestHints()
	{
		mailDomain = null;
	}

	public String getMailDomain()
	{
		return mailDomain;
	}

	public void setMailDomain(String mailDomain)
	{
		this.mailDomain = mailDomain;
	}
}

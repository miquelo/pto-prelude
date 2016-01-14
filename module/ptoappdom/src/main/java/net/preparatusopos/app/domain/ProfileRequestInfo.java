package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class ProfileRequestInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ProfileType type;
	private ProfileRequestType requestType;
	private String mailUsername;
	private String mailDomain;
	private String tokenRef;
	
	@ConstructorProperties({
		"type"
	})
	public ProfileRequestInfo(ProfileType type)
	{
		this.type = type;
		requestType = null;
		mailUsername = null;
		mailDomain = null;
		tokenRef = null;
	}

	public ProfileType getType()
	{
		return type;
	}

	public ProfileRequestType getRequestType()
	{
		return requestType;
	}

	public void setRequestType(ProfileRequestType requestType)
	{
		this.requestType = requestType;
	}

	public String getMailUsername()
	{
		return mailUsername;
	}

	public void setMailUsername(String mailUsername)
	{
		this.mailUsername = mailUsername;
	}

	public String getMailDomain()
	{
		return mailDomain;
	}

	public void setMailDomain(String mailDomain)
	{
		this.mailDomain = mailDomain;
	}

	public String getTokenRef()
	{
		return tokenRef;
	}

	public void setTokenRef(String tokenRef)
	{
		this.tokenRef = tokenRef;
	}
}

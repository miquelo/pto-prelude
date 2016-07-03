package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;

/**
 * Mail domain match profile request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MailDomainMatchProfileRequest
extends ProfileRequest
{
	private static final long serialVersionUID = 1L;
	
	private String mailDomain;
	private String mailUsername;
	
	@ConstructorProperties({
		"type"
	})
	public MailDomainMatchProfileRequest(ProfileType type)
	{
		super(type);
		mailDomain = null;
		mailUsername = null;
	}
	
	@Override
	public ProfileRequestType getRequestType()
	{
		return ProfileRequestType.MAIL_DOMAIN_MATCH;
	}

	public String getMailDomain()
	{
		return mailDomain;
	}

	public void setMailDomain(String mailDomain)
	{
		this.mailDomain = mailDomain;
	}

	public String getMailUsername()
	{
		return mailUsername;
	}

	public void setMailUsername(String mailUsername)
	{
		this.mailUsername = mailUsername;
	}

	@Override
	protected ProfileRequest clone(ProfileType profileType)
	{
		MailDomainMatchProfileRequest request =
				new MailDomainMatchProfileRequest(profileType);
		request.setMailUsername(getMailUsername());
		request.setMailDomain(getMailDomain());
		return request;
	}
}

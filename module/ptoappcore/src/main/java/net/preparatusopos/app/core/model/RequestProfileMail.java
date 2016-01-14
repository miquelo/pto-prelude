package net.preparatusopos.app.core.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(RequestProfile.TYPE_MAIL)
public class RequestProfileMail
extends RequestProfile
{
	private String mailAddress;
	
	public RequestProfileMail()
	{
		mailAddress = null;
	}
	
	@Column(
		name="CL_MAIL"
	)
	public String getMailAddress()
	{
		return mailAddress;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}
}

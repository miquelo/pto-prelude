package net.preparatusopos.app.core.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(RequestCredential.TYPE_MAIL_ADDRESS)
public class RequestCredentialMailAddress
extends RequestCredential
{
	private String mailAddress;
	private byte[] password;
	
	public RequestCredentialMailAddress()
	{
		mailAddress = null;
		password = null;
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

	@Column(
		name="CL_PASSWORD"
	)
	public byte[] getPassword()
	{
		return password;
	}

	public void setPassword(byte[] password)
	{
		this.password = password;
	}

	protected String getTypeAsString()
	{
		return TYPE_MAIL_ADDRESS;
	}
}
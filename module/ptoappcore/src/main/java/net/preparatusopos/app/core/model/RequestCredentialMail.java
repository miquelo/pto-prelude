package net.preparatusopos.app.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_REQCREDMAIL"
)
public class RequestCredentialMail
extends Request
{
	private String mailAddress;
	private byte[] password;
	
	public RequestCredentialMail()
	{
		mailAddress = null;
		password = null;
	}

	@Id
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

	@Override
	public int hashCode()
	{
		return mailAddress.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RequestCredentialMail)
		{
			RequestCredentialMail req = (RequestCredentialMail) obj;
			return mailAddress.equals(req.mailAddress);
		}
		return false;
	}
}
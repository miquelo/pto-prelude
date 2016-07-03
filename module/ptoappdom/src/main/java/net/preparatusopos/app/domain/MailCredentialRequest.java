package net.preparatusopos.app.domain;

/**
 * Mail credential request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MailCredentialRequest
implements CredentialRequest
{
	private static final long serialVersionUID = 1L;
	
	private String mailAddress;
	private byte[] password;
	
	public MailCredentialRequest()
	{
		mailAddress = null;
		password = null;
	}
	
	@Override
	public CredentialType getType()
	{
		return CredentialType.MAIL_ADDRESS;
	}
	
	@Override
	public String getUsername()
	{
		return getMailAddress();
	}

	public String getMailAddress()
	{
		return mailAddress;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	@Override
	public byte[] getPassword()
	{
		return password;
	}

	public void setPassword(byte[] password)
	{
		this.password = password;
	}
}

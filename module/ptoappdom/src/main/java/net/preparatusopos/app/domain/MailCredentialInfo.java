package net.preparatusopos.app.domain;

/**
 * Mail credential info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MailCredentialInfo
implements CredentialInfo
{
	private static final long serialVersionUID = 1L;
	
	private String mailAddress;
	private byte[] password;
	
	public MailCredentialInfo()
	{
		mailAddress = null;
		password = null;
	}
	
	@Override
	public CredentialType getType()
	{
		return CredentialType.MAIL_ADDRESS;
	}
	
	public String getMailAddress()
	{
		return mailAddress;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	public byte[] getPassword()
	{
		return password;
	}

	public void setPassword(byte[] password)
	{
		this.password = password;
	}
}

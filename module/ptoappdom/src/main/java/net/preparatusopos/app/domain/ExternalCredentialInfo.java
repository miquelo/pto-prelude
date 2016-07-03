package net.preparatusopos.app.domain;

/**
 * External credential info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class ExternalCredentialInfo
implements CredentialInfo
{
	private static final long serialVersionUID = 1L;
	
	private ExternalUID externalUID;
	private String authorizationCode;
	
	public ExternalCredentialInfo()
	{
		externalUID = null;
		authorizationCode = null;
	}
	
	@Override
	public CredentialType getType()
	{
		return CredentialType.EXTERNAL_UID;
	}

	public ExternalUID getExternalUID()
	{
		return externalUID;
	}

	public void setExternalUID(ExternalUID externalUID)
	{
		this.externalUID = externalUID;
	}

	public String getAuthorizationCode()
	{
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode)
	{
		this.authorizationCode = authorizationCode;
	}
}

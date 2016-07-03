package net.preparatusopos.app.domain;

/**
 * External credential request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class ExternalCredentialRequest
implements CredentialRequest
{
	private static final long serialVersionUID = 1L;
	
	private ExternalUID externalUID;
	private String authorizationCode;
	
	public ExternalCredentialRequest()
	{
		externalUID = null;
		authorizationCode = null;
	}
	
	@Override
	public CredentialType getType()
	{
		return CredentialType.EXTERNAL_UID;
	}
	
	@Override
	public String getUsername()
	{
		return externalUID == null ? null : externalUID.toString();
	}

	@Override
	public byte[] getPassword()
	{
		return null;
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

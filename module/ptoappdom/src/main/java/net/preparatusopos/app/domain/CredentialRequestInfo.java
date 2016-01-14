package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * Flat credential request info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class CredentialRequestInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialType type;
	private ExternalUIDType externalUIDType;
	private String tokenRef;
	private String mailAddress;
	private String password;
	private String authorizationCode;
	
	/**
	 * Constructs a credential request info of the given type.
	 * 
	 * @param type
	 * 			Type of credential request info.
	 */
	@ConstructorProperties({
		"type"
	})
	public CredentialRequestInfo(CredentialType type)
	{
		this.type = type;
		externalUIDType = null;
		tokenRef = null;
		mailAddress = null;
		password = null;
		authorizationCode = null;
	}

	/**
	 * Type of credential request info.
	 */
	public CredentialType getType()
	{
		return type;
	}

	/**
	 * Type of external UID service.
	 */
	public ExternalUIDType getExternalUIDType()
	{
		return externalUIDType;
	}

	/**
	 * Set the type of external UID service.
	 * 
	 * @param externalUIDType
	 * 			The new type of external UID service.
	 */
	public void setExternalUIDType(ExternalUIDType externalUIDType)
	{
		this.externalUIDType = externalUIDType;
	}

	/**
	 * Token reference.
	 */
	public String getTokenRef()
	{
		return tokenRef;
	}

	/**
	 * Set the token reference.
	 * 
	 * @param tokenRef
	 * 			The new token reference.
	 */
	public void setTokenRef(String tokenRef)
	{
		this.tokenRef = tokenRef;
	}

	/**
	 * Subject mail address.
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * Set the subject mail address.
	 * 
	 * @param mailAddress
	 * 			The new subject mail address.
	 */
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	/**
	 * Password that may be used by credential.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set the password that may be used by credential.
	 * 
	 * @param password
	 * 			The new used password.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Authorization code for external UID service.
	 */
	public String getAuthorizationCode()
	{
		return authorizationCode;
	}

	/**
	 * Set the authorization code for external UID service.
	 * 
	 * @param authorizationCode
	 * 			The new authorization code.
	 */
	public void setAuthorizationCode(String authorizationCode)
	{
		this.authorizationCode = authorizationCode;
	}
}

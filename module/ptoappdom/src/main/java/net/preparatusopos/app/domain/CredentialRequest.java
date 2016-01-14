package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * Resolved credential request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class CredentialRequest
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialType type;
	private ExternalUIDType externalUIDType;
	private String tokenHex;
	private String password;
	private String externalUserID;
	
	/**
	 * Creates a resolved credential request of the given type.
	 * 
	 * @param type
	 * 			Type of credential.
	 */
	@ConstructorProperties({
		"type"
	})
	public CredentialRequest(CredentialType type)
	{
		this.type = type;
		externalUIDType = null;
		tokenHex = null;
		password = null;
		externalUserID = null;
	}

	/**
	 * Type of credential.
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
	 * Hexadecimal token.
	 */
	public String getTokenHex()
	{
		return tokenHex;
	}

	/**
	 * Set the hexadecimal token.
	 * 
	 * @param tokenHex
	 * 			The new hexadecimal token.
	 */
	public void setTokenHex(String tokenHex)
	{
		this.tokenHex = tokenHex;
	}

	/**
	 * Given password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set the given password.
	 * 
	 * @param password
	 * 			The new given password.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * External user ID.
	 */
	public String getExternalUserID()
	{
		return externalUserID;
	}

	/**
	 * Set the external user ID.
	 * 
	 * @param externalUserID
	 * 			The new external user ID.
	 */
	public void setExternalUserID(String externalUserID)
	{
		this.externalUserID = externalUserID;
	}
}

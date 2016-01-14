package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class CredentialInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialType type;
	private String mailAddress;
	private String externalUID;
	private ExternalUIDType externalUIDType;
	
	/**
	 * Constructs credential info of the given type.
	 * 
	 * @param type
	 * 			The type of credential.
	 */
	@ConstructorProperties({
		"type"
	})
	public CredentialInfo(CredentialType type)
	{
		this.type = type;
		mailAddress = null;
		externalUID = null;
		externalUIDType = null;
	}

	/**
	 * Type of credential.
	 */
	public CredentialType getType()
	{
		return type;
	}

	/**
	 * Mail address if type is {@link CredentialType#MAIL_ADDRESS}.
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * Set the mail address.
	 * 
	 * @param mailAddress
	 * 			The new mail address.
	 */
	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	/**
	 * External UID if type is {@link CredentialType#EXTERNAL_UID}.
	 */
	public String getExternalUID()
	{
		return externalUID;
	}

	/**
	 * Set the external UID.
	 * 
	 * @param externalUID
	 * 			The new external UID.
	 */
	public void setExternalUID(String externalUID)
	{
		this.externalUID = externalUID;
	}

	/**
	 * External UID type if type is {@link CredentialType#EXTERNAL_UID}.
	 */
	public ExternalUIDType getExternalUIDType()
	{
		return externalUIDType;
	}

	/**
	 * 
	 * @param externalUIDType
	 */
	public void setExternalUIDType(ExternalUIDType externalUIDType)
	{
		this.externalUIDType = externalUIDType;
	}
}

package net.preparatusopos.app.core.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(RequestCredential.TYPE_EXTERNAL_UID)
public class RequestCredentialExternalUID
extends RequestCredential
{
	public RequestCredentialExternalUID()
	{
	}
	
	protected String getTypeAsString()
	{
		return TYPE_EXTERNAL_UID;
	}
}
package net.preparatusopos.app.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Direct profiles request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class DirectProfilesRequest
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialRequest credentialRequest;
	private Set<ProfileRequest> values;
	
	public DirectProfilesRequest()
	{
		credentialRequest = null;
		values = new HashSet<>();
	}

	public CredentialRequest getCredentialRequest()
	{
		return credentialRequest;
	}

	public void setCredentialRequest(CredentialRequest credentialRequest)
	{
		this.credentialRequest = credentialRequest;
	}

	public Set<ProfileRequest> getValues()
	{
		return values;
	}

	public void setValues(Set<ProfileRequest> values)
	{
		this.values = values;
	}
}

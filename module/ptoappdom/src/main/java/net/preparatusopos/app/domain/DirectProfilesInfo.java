package net.preparatusopos.app.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Direct profiles info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class DirectProfilesInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CredentialInfo credentialInfo;
	private Set<ProfileInfo> values;
	
	public DirectProfilesInfo()
	{
		credentialInfo = null;
		values = new HashSet<>();
	}

	public CredentialInfo getCredentialInfo()
	{
		return credentialInfo;
	}

	public void setCredentialInfo(CredentialInfo credentialInfo)
	{
		this.credentialInfo = credentialInfo;
	}

	public Set<ProfileInfo> getValues()
	{
		return values;
	}

	public void setValues(Set<ProfileInfo> values)
	{
		this.values = values;
	}
}

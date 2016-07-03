package net.preparatusopos.app.domain;

import java.io.Serializable;

import net.preparatusopos.security.auth.PTOPrincipal;

/**
 * Result of registering a new member.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MemberRegistered
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private PTOPrincipal principal;
	private CredentialRequest credentialRequest;
	
	public MemberRegistered()
	{
		principal = null;
		credentialRequest = null;
	}

	public PTOPrincipal getPrincipal()
	{
		return principal;
	}

	public void setPrincipal(PTOPrincipal principal)
	{
		this.principal = principal;
	}

	public CredentialRequest getCredentialRequest()
	{
		return credentialRequest;
	}

	public void setCredentialRequest(CredentialRequest credentialRequest)
	{
		this.credentialRequest = credentialRequest;
	}
}

package net.preparatusopos.app.domain;

import java.io.Serializable;

import net.preparatusopos.security.auth.PTOPrincipal;

/**
 * Info for new member feedback.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MemberAdded
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private PTOPrincipal principal;
	private CredentialRequestInfo requestInfo;
	
	/**
	 * Empty constructor.
	 */
	public MemberAdded()
	{
		principal = null;
		requestInfo = null;
	}

	/**
	 * Member principal.
	 */
	public PTOPrincipal getPrincipal()
	{
		return principal;
	}

	/**
	 * Set the member principal.
	 * 
	 * @param principal
	 * 			The new member principal.
	 */
	public void setPrincipal(PTOPrincipal principal)
	{
		this.principal = principal;
	}

	/**
	 * Credential request info.
	 */
	public CredentialRequestInfo getRequestInfo()
	{
		return requestInfo;
	}

	/**
	 * Set the credential request info.
	 * 
	 * @param requestInfo
	 * 			The new credential request info.
	 */
	public void setRequestInfo(CredentialRequestInfo requestInfo)
	{
		this.requestInfo = requestInfo;
	}
}

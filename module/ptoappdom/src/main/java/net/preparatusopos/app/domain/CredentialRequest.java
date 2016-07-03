package net.preparatusopos.app.domain;

import java.io.Serializable;

/**
 * Credential request.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public interface CredentialRequest
extends Serializable
{
	public CredentialType getType();
	
	public String getUsername();
	
	public byte[] getPassword();
}

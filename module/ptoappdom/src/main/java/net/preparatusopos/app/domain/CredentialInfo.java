package net.preparatusopos.app.domain;

import java.io.Serializable;

/**
 * Credential info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public interface CredentialInfo
extends Serializable
{
	public CredentialType getType();
}

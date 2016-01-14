package net.preparatusopos.security.auth;

import java.security.cert.Certificate;

public interface PTOCredentialFactory
{
	public PTOCredential newPassword(String username, char[] password);
	
	public PTOCredential newCertificate(Certificate certificate);
	
	public PTOCredential newCertificate(String principalName);
	
	public PTOCredential newExternal(String userID);
}

package net.preparatusopos.security.auth.login;

import java.security.cert.X509Certificate;

import javax.security.auth.login.LoginException;

import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOCredentialFactory;
import net.preparatusopos.security.auth.PTORealmBootstrap;

import com.sun.appserv.security.AppservCertificateLoginModule;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.Realm;

public class PTOCertificateLoginModule
extends AppservCertificateLoginModule
implements PTOLoginModule
{
	public static final String OPTION_REALM = "realm";
	
	private PTOLoginModuleAutheticator autheticator;
	
	public PTOCertificateLoginModule()
	{
		autheticator = new PTOLoginModuleAutheticator(this);
	}

	@Override
	public Realm getInvolvedRealm()
	{
		try
		{
			Object realmName = _options.get(OPTION_REALM);
			if (realmName != null)
				return Realm.getInstance(realmName.toString());
			
			StringBuilder msg = new StringBuilder();
			msg.append("Option '").append(OPTION_REALM).append("' must be ");
			msg.append("specified");
			throw new IllegalStateException(msg.toString());
		}
		catch (NoSuchRealmException exception)
		{
			throw new IllegalStateException(exception);
		}
	}

	@Override
	public void commitGroupNames(String[] groupNames)
	{
		commitUserAuthentication(groupNames);
	}
	
	@Override
	protected void authenticateUser()
	throws LoginException
	{
		X509Certificate[] certs = getCerts();
		if (certs.length < 1)
			throw new IllegalStateException("There is not any certificate");
		PTOCredentialFactory credentialFact =
				PTORealmBootstrap.createCredentialFactory();
		PTOCredential cred = credentialFact.newCertificate(certs[0]);
		autheticator.authenticateSubject(getSubject(), cred);
	}
}

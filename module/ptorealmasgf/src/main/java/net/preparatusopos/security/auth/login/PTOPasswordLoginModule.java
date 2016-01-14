package net.preparatusopos.security.auth.login;

import javax.security.auth.login.LoginException;

import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOCredentialFactory;
import net.preparatusopos.security.auth.PTORealmBootstrap;

import com.sun.appserv.security.AppservPasswordLoginModule;
import com.sun.enterprise.security.auth.realm.Realm;

public class PTOPasswordLoginModule
extends AppservPasswordLoginModule
implements PTOLoginModule
{
	private PTOLoginModuleAutheticator autheticator;
	
	public PTOPasswordLoginModule()
	{
		autheticator = new PTOLoginModuleAutheticator(this);
	}
	
	@Override
	public Realm getInvolvedRealm()
	{
		return _currentRealm;
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
		PTOCredentialFactory credentialFact =
				PTORealmBootstrap.createCredentialFactory();
		PTOCredential cred = credentialFact.newPassword(_username, _passwd);
		autheticator.authenticateSubject(_subject, cred);
	}
}

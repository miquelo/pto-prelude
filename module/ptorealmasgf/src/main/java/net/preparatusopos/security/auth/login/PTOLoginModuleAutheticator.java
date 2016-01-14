package net.preparatusopos.security.auth.login;

import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOManagedPrincipal;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.PTORealmBootstrap;
import net.preparatusopos.security.auth.PTORealmManager;
import net.preparatusopos.security.auth.PTORealmManagerException;
import net.preparatusopos.security.auth.PTORealmManagerFactory;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.spi.PTORealmConnectionFactory;

import com.sun.enterprise.security.auth.realm.Realm;

public class PTOLoginModuleAutheticator
{
	private PTOLoginModule loginModule;
	
	public PTOLoginModuleAutheticator(PTOLoginModule loginModule)
	{
		this.loginModule = loginModule;
	}
	
	public void authenticateSubject(Subject subj, PTOCredential credential)
	throws LoginException
	{
		try
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(getConnection());
			PTOPrincipal principal = rm.authenticate(credential);
			if (principal == null)
			{
				StringBuilder msg = new StringBuilder();
				msg.append("Credential ").append(credential);
				msg.append(" does not correspont to any principal");
				throw new LoginException(msg.toString());
			}
			subj.getPrincipals().add(principal);
			
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			Set<String> groups = managedPrincipal.getGroups();
			String[] groupNames = new String[groups.size()];
			int i = 0;
			for (String groupName : groups)
				groupNames[i++] = groupName;
			loginModule.commitGroupNames(groupNames);
		}
		catch (PTORealmManagerException | PTORealmConnectionException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	private PTORealmConnection getConnection()
	throws PTORealmConnectionException
	{
		Realm realm = loginModule.getInvolvedRealm();
		if (realm instanceof PTORealmConnectionFactory)
		{
			PTORealmConnectionFactory fact = (PTORealmConnectionFactory) realm;
			return fact.getConnection();
		}
		StringBuilder msg = new StringBuilder();
		msg.append("Realm does not implement ");
		msg.append(PTORealmConnectionFactory.class);
		throw new IllegalStateException(msg.toString());
	}
}

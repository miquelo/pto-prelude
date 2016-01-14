package net.preparatusopos.security.auth.login;

import com.sun.enterprise.security.auth.realm.Realm;

public interface PTOLoginModule
{
	public Realm getInvolvedRealm();
	
	public void commitGroupNames(String[] groupNames);
}

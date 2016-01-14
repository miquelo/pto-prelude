package net.preparatusopos.test.security.auth.database;

import junit.framework.TestCase;

public class PTOSQLRealmManagerTest
extends TestCase
{
	// private PTODBRealmEmbeddedDataBase dataBase;
	
	public PTOSQLRealmManagerTest()
	{
		// dataBase = new PTODBRealmEmbeddedDataBase(new File(".db"));
	}
	
	@Override
	public void setUp()
	throws Exception
	{
		// dataBase.init();
	}
	
	@Override
	public void tearDown()
	throws Exception
	{
		// dataBase.destroy();
	}
	
	public void testAuthenticatePassword()
	throws Exception
	{
		// PTOPrincipal principal = dataBase.createUser();
		// 
		// try (PTORealmManager rm = dataBase.getManager())
		// {
		// 	PTOPasswordCredential cred = new PTOPasswordCredential();
		// 	cred.setUsername("user1");
		// 	cred.setPassword("12345678".toCharArray());
		// 	
		// 	PTOManagedPrincipal managedPrincipal = rm.manage(principal);
		// 	managedPrincipal.getCredentials().add(cred);
		// 	
		// 	assertEquals(principal, rm.authenticate(cred));
		// }
		// finally
		// {
		// 	dataBase.removeUser(principal);
		// }
	}
}

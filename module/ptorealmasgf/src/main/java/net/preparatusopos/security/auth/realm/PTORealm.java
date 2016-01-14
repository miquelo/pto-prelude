package net.preparatusopos.security.auth.realm;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import net.preparatusopos.security.auth.PTOManagedPrincipal;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.PTORealmBootstrap;
import net.preparatusopos.security.auth.PTORealmManager;
import net.preparatusopos.security.auth.PTORealmManagerException;
import net.preparatusopos.security.auth.PTORealmManagerFactory;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.spi.PTORealmConnectionFactory;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;

public abstract class PTORealm
extends AppservRealm
implements PTORealmConnectionFactory
{
	public static final String AUTH_TYPE = "ptoRealm";
	
	private PTORealmConnection connection;
	
	protected PTORealm()
	{
		connection = null;
	}
	
	@Override
	public String getAuthType()
	{
		return AUTH_TYPE;
	}
	
	@Override
	public Enumeration<?> getGroupNames(String username)
	throws InvalidOperationException, NoSuchUserException
	{
		try
		{
			PTORealmManagerFactory fact =
					PTORealmBootstrap.createManagerFactory();
			PTORealmManager rm = fact.getManager(getConnection());
			long uid = Long.parseLong(username);
			PTOPrincipal principal = new PTOPrincipal(uid);
			PTOManagedPrincipal managedPrincipal = rm.manage(principal);
			
			Vector<Object> groupNames = new Vector<>();
			for (String groupName : managedPrincipal.getGroups())
				groupNames.add(groupName);
			return groupNames.elements();
		}
		catch (NumberFormatException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid user identifier: ").append(username);
			throw new NoSuchUserException(msg.toString());
		}
		catch (PTORealmManagerException | PTORealmConnectionException exception)
		{
			throw new InvalidOperationException(exception.toString());
		}
	}
	
	@Override
	public PTORealmConnection getConnection()
	throws PTORealmConnectionException
	{
		if (connection == null)
			connection = newConnection();
		return connection;
	}
	
	@Override
	protected void init(Properties props)
	throws BadRealmException, NoSuchRealmException
	{
		String jaasContext = props.getProperty(JAAS_CONTEXT_PARAM);
		if (jaasContext != null)
			setProperty(JAAS_CONTEXT_PARAM, jaasContext);
		configure(new PTORealmProperties(props));
	}
	
	protected abstract void configure(PTORealmProperties props)
	throws BadRealmException;
	
	protected abstract PTORealmConnection newConnection()
	throws PTORealmConnectionException;
}

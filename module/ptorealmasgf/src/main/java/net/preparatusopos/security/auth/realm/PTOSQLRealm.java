package net.preparatusopos.security.auth.realm;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.sql.PTOSQLRealmConnection;
import net.preparatusopos.security.auth.sql.PTOSQLRealmSettings;

import com.sun.enterprise.security.auth.realm.BadRealmException;

public class PTOSQLRealm
extends PTORealm
{
	private static final String DATASOURCE_PROP_NAME = "dataSource";
	private static final String MANAGER_SETTINGS_PREFIX = "dataBase";
	
	private String dsJNDI;
	private PTOSQLRealmSettings settings;
	
	public PTOSQLRealm()
	{
		dsJNDI = null;
		settings = new PTOSQLRealmSettings();
	}
	
	@Override
	protected void configure(PTORealmProperties props)
	throws BadRealmException
	{
		dsJNDI = props.getRequiredProperty(DATASOURCE_PROP_NAME);
		props.store(settings, MANAGER_SETTINGS_PREFIX);
	}
	
	@Override
	protected PTORealmConnection newConnection()
	throws PTORealmConnectionException
	{
		try
		{
			DataSource ds = InitialContext.doLookup(dsJNDI);
			return new PTOSQLRealmConnection(settings, ds.getConnection());
		}
		catch (NamingException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Data source is not available");
			throw new PTORealmConnectionException(msg.toString(), exception);
		}
		catch (SQLException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Data base connection could not be established");
			throw new PTORealmConnectionException(msg.toString(), exception);
		}
	}
}

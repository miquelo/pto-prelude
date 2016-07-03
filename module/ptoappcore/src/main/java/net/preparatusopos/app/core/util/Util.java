package net.preparatusopos.app.core.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.sql.DataSource;

import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.sql.PTOSQLRealmConnection;
import net.preparatusopos.security.auth.sql.PTOSQLRealmSettings;

public class Util
{
	private static final String SCHEMA = "PTOAPP";
	private static final String VW_MEMBERGROUP = "VW_MEMBERGROUP";
	private static final String TB_MEMBERCRED = "TB_MEMBERCRED";
	
	private static Random random;
	
	private Util()
	{
	}
	
	public static String createRef(int size) {
		if (random == null)
			random = new Random();
		StringBuilder ref = new StringBuilder();
		while (ref.length() < size)
			ref.append(Integer.toHexString(random.nextInt(0x10)));
		return ref.toString();
	}
	
	public static PTORealmConnection getRealmConnection(
			DataSource realmDataSource)
	throws PTORealmConnectionException
	{
		try
		{
			PTOSQLRealmSettings settings = new PTOSQLRealmSettings();
			settings.setSchema(SCHEMA);
			settings.setGroupViewName(VW_MEMBERGROUP);
			settings.setCredTableName(TB_MEMBERCRED);
			Connection connection = realmDataSource.getConnection();
			return new PTOSQLRealmConnection(settings, connection);
		}
		catch (SQLException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Data base connection could not be established");
			throw new PTORealmConnectionException(msg.toString(), exception);
		}
	}
	
	public static String normalizeMailAddress(String mailAddr)
	{
		String[] parts = mailAddr.split("@");
		if (parts.length < 2)
		{
			StringBuilder msg = new StringBuilder();
			msg.append(mailAddr).append(" is not a valid mail address");
			throw new IllegalArgumentException(msg.toString());
		}
		String username = parts[0].replace(".", "");
		return String.format("%s@%s", username, parts[1]);
	}
}

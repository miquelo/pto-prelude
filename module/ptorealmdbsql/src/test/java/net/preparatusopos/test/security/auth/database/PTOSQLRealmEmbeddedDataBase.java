package net.preparatusopos.test.security.auth.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.spi.PTORealmConnectionFactory;
import net.preparatusopos.security.auth.sql.PTOSQLRealmConnection;
import net.preparatusopos.security.auth.sql.PTOSQLRealmSettings;

import org.apache.derby.jdbc.EmbeddedDataSource;

public class PTOSQLRealmEmbeddedDataBase
implements PTORealmConnectionFactory
{
	private static final String USER_VIEW_NAME = "TB_USER";
	
	private File dataBaseDir;
	private PTOSQLRealmSettings settings;
	private EmbeddedDataSource dataSource;
	
	public PTOSQLRealmEmbeddedDataBase(File dataBaseDir)
	{
		this.dataBaseDir = dataBaseDir;
		settings = new PTOSQLRealmSettings();
		dataSource = null;
	}
	
	@Override
	public PTORealmConnection getConnection()
	throws PTORealmConnectionException
	{
		try
		{
			if (dataSource == null)
			{
				StringBuilder msg = new StringBuilder();
				msg.append("Data base was not initialized");
				throw new PTORealmConnectionException(msg.toString());
			}
			Connection connection = dataSource.getConnection();
			return new PTOSQLRealmConnection(settings, connection);
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}
	
	public void init()
	throws PTORealmConnectionException
	{
		dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName(dataBaseDir.getAbsolutePath());
		dataSource.setCreateDatabase("create");
		createSchema(dataSource);
	}
	
	public void destroy()
	{
		dataSource.setShutdownDatabase("shutdown");
		deleteFile(dataBaseDir);
	}
	
	public PTOPrincipal createUser()
	{
		return new PTOPrincipal(0);
	}
	
	public void removeUser(PTOPrincipal principal)
	{
	}
	
	private static void deleteFile(File file)
	{
		if (file.isDirectory())
			for (File child : file.listFiles())
				deleteFile(child);
		file.delete();
	}
	
	private static void createSchema(DataSource dataSource)
	throws PTORealmConnectionException
	{
		try (Connection conn = dataSource.getConnection())
		{
			StringBuilder sql = null;
			
			try (Statement st = conn.createStatement())
			{
				sql = new StringBuilder();
				sql.append("CREATE TABLE ").append(USER_VIEW_NAME);
				sql.append(" (\n");
				sql.append("\tCL_UID BIGINT,\n");
				sql.append("\tPRIMARY KEY (CL_UID)\n");
				sql.append(")");
				st.execute(sql.toString());
			}
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}
}

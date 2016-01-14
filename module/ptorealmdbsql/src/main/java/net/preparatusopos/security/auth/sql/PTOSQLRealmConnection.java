package net.preparatusopos.security.auth.sql;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.preparatusopos.security.auth.PTOCredential;
import net.preparatusopos.security.auth.PTOCredentialDuplicatedException;
import net.preparatusopos.security.auth.PTOCredentialFactory;
import net.preparatusopos.security.auth.PTOPrincipal;
import net.preparatusopos.security.auth.PTORealmBootstrap;
import net.preparatusopos.security.auth.spi.PTORealmConnection;
import net.preparatusopos.security.auth.spi.PTORealmConnectionException;
import net.preparatusopos.security.auth.sql.query.SQLQuery;

public class PTOSQLRealmConnection
implements PTORealmConnection
{
	private PTOSQLRealmSettings settings;
	private Connection connection;
	private SQLQuery selectUserIDQuery;
	private SQLQuery selectGroupsQuery;
	private SQLQuery selectCredentialsQuery;
	private SQLQuery selectCredentialMaxIndexQuery;
	private SQLQuery selectPasswordUserIDQuery;
	private SQLQuery insertCredentialQuery;
	private SQLQuery updateCredentialDecIndexesQuery;
	private SQLQuery deleteCredentialQuery;
	
	public PTOSQLRealmConnection(PTOSQLRealmSettings settings,
			Connection connection)
	{
		this.settings = settings;
		this.connection = connection;
		selectUserIDQuery = new SQLQuery();
		selectGroupsQuery = new SQLQuery();
		selectCredentialsQuery = new SQLQuery();
		selectCredentialMaxIndexQuery = new SQLQuery();
		selectPasswordUserIDQuery = new SQLQuery();
		insertCredentialQuery = new SQLQuery();
		updateCredentialDecIndexesQuery = new SQLQuery();
		deleteCredentialQuery = new SQLQuery();
	}
	
	@Override
	public PTOPrincipal resolve(String name)
	throws PTORealmConnectionException
	{
		try
		{
			Long userID = selectUserID(name);
			return userID == null ? null : new PTOPrincipal(userID);
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}
	
	@Override
	public Set<String> getGroups(PTOPrincipal principal)
	throws PTORealmConnectionException
	{
		try
		{
			return selectGroups(principal.getUserID());
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}

	@Override
	public List<PTOCredential> getCredentials(PTOPrincipal principal)
	throws PTORealmConnectionException
	{
		try
		{
			return selectCredentials(principal.getUserID());
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}

	@Override
	public void addCredential(PTOPrincipal principal,
			PTOCredential credential)
	throws PTORealmConnectionException, PTOCredentialDuplicatedException
	{
		try
		{
			// TODO Duplicated check!
			
			long userID = principal.getUserID();
			Integer index = selectMaxCredentialIndex(principal.getUserID());
			index = index == null ? 0 : index + 1;
			insertCredential(userID, index, credential);
		}
		catch (SQLException | NoSuchAlgorithmException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}

	@Override
	public void removeCredential(PTOPrincipal principal, int index)
	throws PTORealmConnectionException
	{
		try
		{
			long userID = principal.getUserID();
			deleteCredential(userID, index);
			updateCredentialDecIndexesQuery(userID, index);
		}
		catch (SQLException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}

	@Override
	public PTOPrincipal matching(PTOCredential credential)
	throws PTORealmConnectionException
	{
		try
		{
			Long userID = selectPasswordUserID(credential.getUsername(),
					credential.getPassword());
			return userID == null ? null : new PTOPrincipal(userID);
		}
		catch (SQLException | NoSuchAlgorithmException exception)
		{
			throw new PTORealmConnectionException(exception);
		}
	}
	
	@Override
	public void close()
	throws IOException
	{
		try
		{
			selectUserIDQuery.close();
			selectGroupsQuery.close();
			selectCredentialsQuery.close();
			selectCredentialMaxIndexQuery.close();
			selectPasswordUserIDQuery.close();
			insertCredentialQuery.close();
			deleteCredentialQuery.close();
			connection.close();
		}
		catch (SQLException exception)
		{
			throw new IOException(exception);
		}
	}
	
	private Long selectUserID(String name)
	throws SQLException
	{
		if (selectUserIDQuery.empty())
		{
			selectUserIDQuery.select(
				settings.getCredUIDColumnName()
			);
			selectUserIDQuery.from(
				getCredTableName()
			);
			selectUserIDQuery.where(
				condEqual(settings.getCredNameColumnName(), "?")
			);
		}
		
		PreparedStatement ps = selectUserIDQuery.getStatement(connection);
		ps.setString(1, name);
		try (ResultSet rs = ps.executeQuery())
		{
			if (rs.next())
			{
				long userID = rs.getLong(1);
				return rs.wasNull() ? null : userID;
			}
			return null;
		}
	}
	
	private Set<String> selectGroups(long userID)
	throws SQLException
	{
		if (selectGroupsQuery.empty())
		{
			selectGroupsQuery.select(
				settings.getGroupNameColumnName()
			);
			selectGroupsQuery.from(
				getGroupViewName()
			);
			selectGroupsQuery.where(
				condEqual(settings.getGroupUIDColumnName(), "?")
			);
		}
		
		PreparedStatement ps = selectGroupsQuery.getStatement(connection);
		ps.setLong(1, userID);
		try (ResultSet rs = ps.executeQuery())
		{
			Set<String> groups = new HashSet<String>();
			while (rs.next())
				groups.add(rs.getString(settings.getGroupNameColumnName()));
			return groups;
		}
	}
	
	private List<PTOCredential> selectCredentials(long userID)
	throws SQLException
	{
		if (selectCredentialsQuery.empty())
		{
			selectCredentialsQuery.select(
				settings.getCredTypeColumnName(),
				settings.getCredNameColumnName(),
				settings.getCredPasswdColumnName()
			);
			selectCredentialsQuery.from(
				getCredTableName()
			);
			selectCredentialsQuery.where(
				condEqual(settings.getCredUIDColumnName(), "?")
			);
			selectCredentialsQuery.orderBy(
				settings.getCredIndexColumnName()
			);
		}
		
		List<PTOCredential> credentials = new ArrayList<>();
		PreparedStatement ps = selectCredentialsQuery.getStatement(connection);
		ps.setLong(1, userID);
		try (ResultSet rs = ps.executeQuery())
		{
			PTOCredentialFactory credentialFact =
					PTORealmBootstrap.createCredentialFactory();
			while (rs.next())
			{
				int type = rs.getInt(1);
				String name = rs.getString(2);
				
				switch (type)
				{
					case PTOCredential.TYPE_PASSWORD:
					char[] passwd = rs.getString(3).toCharArray();
					credentials.add(credentialFact.newPassword(name, passwd));
					break;
					
					case PTOCredential.TYPE_CERTIFICATE:
					credentials.add(credentialFact.newCertificate(name));
					break;
					
					case PTOCredential.TYPE_EXTERNAL:
					credentials.add(credentialFact.newExternal(name));
					break;
					
					default:
					StringBuilder msg = new StringBuilder();
					msg.append("Unknown credential type ").append(type);
					throw new IllegalStateException(msg.toString());
				}
			}
		}
		return credentials;
	}
	
	private Integer selectMaxCredentialIndex(long userID)
	throws SQLException
	{
		String maxIndexAlias = "CL_MAX_INDEX";
		
		if (selectCredentialMaxIndexQuery.empty())
		{
			selectCredentialMaxIndexQuery.select(
				String.format("MAX(%s) AS %s",
						settings.getCredIndexColumnName(), maxIndexAlias)
			);
			selectCredentialMaxIndexQuery.from(
				getCredTableName()
			);
			selectCredentialMaxIndexQuery.where(
				condEqual(settings.getCredUIDColumnName(), "?")
			);
		}
		
		PreparedStatement ps = selectCredentialMaxIndexQuery.getStatement(
				connection);
		ps.setLong(1, userID);
		try (ResultSet rs = ps.executeQuery())
		{
			if (rs.next())
			{
				int index = rs.getInt(maxIndexAlias);
				return rs.wasNull() ? null : index;
			}
			return null;
		}
	}
	
	private Long selectPasswordUserID(String name, char[] passwd)
	throws SQLException, NoSuchAlgorithmException
	{
		if (selectPasswordUserIDQuery.empty())
		{
			selectPasswordUserIDQuery.select(
				settings.getCredUIDColumnName()
			);
			selectPasswordUserIDQuery.from(
				getCredTableName()
			);
			selectPasswordUserIDQuery.where(
				condEqual(settings.getCredNameColumnName(), "?"),
				condEqual(settings.getCredPasswdColumnName(), "?")
			);
		}
		
		PreparedStatement ps = selectPasswordUserIDQuery.getStatement(
				connection);
		ps.setString(1, name);
		ps.setBytes(2, toBytes(passwd));
		try (ResultSet rs = ps.executeQuery())
		{
			if (rs.next())
			{
				long userID = rs.getLong(settings.getCredUIDColumnName());
				return rs.wasNull() ? null : userID;
			}
		}
		return null;
	}
	
	private void insertCredential(long userID, int index,
			PTOCredential credential)
	throws SQLException, NoSuchAlgorithmException
	{
		if (insertCredentialQuery.empty())
		{
			insertCredentialQuery.insert(
				getCredTableName(),
				settings.getCredUIDColumnName(),
				settings.getCredIndexColumnName(),
				settings.getCredNameColumnName(),
				settings.getCredTypeColumnName(),
				settings.getCredPasswdColumnName()
			);
			insertCredentialQuery.values("?", "?", "?", "?", "?");
		}
		
		char[] passwd = credential.getPassword();
		PreparedStatement ps = insertCredentialQuery.getStatement(connection);
		ps.setLong(1, userID);
		ps.setInt(2, index);
		ps.setString(3, credential.getUsername());
		ps.setInt(4, credential.getType());
		ps.setBytes(5, toBytes(passwd));
		ps.executeUpdate();
	}
	
	private void updateCredentialDecIndexesQuery(long userID,
			int fromIndex)
	throws SQLException
	{
		if (updateCredentialDecIndexesQuery.empty())
		{
			Map<String, Object> updateMap = new HashMap<>();
			updateMap.put(
				settings.getCredIndexColumnName(),
				String.format("%s - 1", settings.getCredIndexColumnName())
			);
			updateCredentialDecIndexesQuery.update(
				getCredTableName(),
				updateMap
			);
			updateCredentialDecIndexesQuery.where(
				String.format("%s > ?", settings.getCredIndexColumnName())
			);
		}
		
		PreparedStatement ps =
				updateCredentialDecIndexesQuery.getStatement(connection);
		ps.setInt(1, fromIndex);
		ps.executeUpdate();
	}
	
	private void deleteCredential(long userID, int index)
	throws SQLException
	{
		if (deleteCredentialQuery.empty())
		{
			deleteCredentialQuery.delete(
				getCredTableName()
			);
			deleteCredentialQuery.where(
				condEqual(settings.getCredUIDColumnName(), "?"),
				condEqual(settings.getCredIndexColumnName(), "?")
			);
		}
		
		PreparedStatement ps = deleteCredentialQuery.getStatement(connection);
		ps.setLong(1, userID);
		ps.setInt(2, index);
		ps.executeUpdate();
	}
	
	private String getGroupViewName()
	{
		return schemed(settings.getGroupViewSchema(),
				settings.getGroupViewName());
	}
	
	private String getCredTableName()
	{
		return schemed(settings.getCredTableSchema(),
				settings.getCredTableName());
	}
	
	private static String schemed(String schema, String name)
	{
		return schema == null ? name : prefixed(schema, name);
	}
	
	private static String prefixed(String prefix, String name)
	{
		return String.format("%s.%s", prefix, name);
	}
	
	private static String condEqual(String name1, String name2)
	{
		return String.format("%s = %s", name1, name2);
	}
	
	private static byte[] toBytes(char[] c)
	{
		return c == null ? null : new String(c).getBytes();
	}
}

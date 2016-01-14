package net.preparatusopos.security.auth.sql.query;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

public class SQLQuery
implements Closeable
{
	private PreparedStatement ps;
	private StringBuilder str;
	
	public SQLQuery()
	{
		ps = null;
		str = new StringBuilder();
	}
	
	public boolean empty()
	{
		return ps == null;
	}
	
	public PreparedStatement getStatement(Connection connection)
	throws SQLException
	{
		if (ps == null)
			ps = connection.prepareStatement(str.toString());
		return ps;
	}
	
	public void select(String... columns)
	{
		str.append("SELECT");
		
		String sep = "\n";
		for (String column : columns)
		{
			str.append(sep).append("  ").append(column);
			sep = ",\n";
		}
		str.append("\n");
	}
	
	public void from(String viewName)
	{
		str.append("FROM ").append(viewName).append("\n");
	}
	
	public void orderBy(String col1, String... colN)
	{
		str.append("ORDER BY\n");
		str.append(col1);
		for (String col : colN)
			str.append(", ").append(col);
		str.append("\n");
	}
	
	public void insert(String tableName, String... columnNames)
	{
		str.append("INSERT INTO ").append(tableName).append(" (");
		String sep = "\n";
		for (String columnName : columnNames)
		{
			str.append(sep).append("  ").append(columnName);
			sep = ",\n";
		}
		str.append("\n)\n");
	}
	
	public void values(String... columnValues)
	{
		str.append("VALUES (");
		String sep = "\n";
		for (String columnValue : columnValues)
		{
			str.append(sep).append("  ").append(columnValue);
			sep = ",\n";
		}
		str.append("\n)\n");
	}
	
	public void update(String tableName, Map<String, Object> updateMap)
	{
		str.append("UPDATE ").append(tableName).append(" SET\n");
		String sep = "";
		for (Entry<String, Object> entry : updateMap.entrySet())
		{
			str.append(sep);
			str.append(entry.getKey()).append(" = ").append(entry.getValue());
			sep = ",\n";
		}
		str.append("\n");
	}
	
	public void delete(String tableName)
	{
		str.append("DELETE FROM ").append(tableName).append("\n");
	}
	
	public void where(String cond1, String... condN)
	{
		str.append("WHERE ");
		conjunctionCond(cond1, condN);
		str.append("\n");
	}
	
	public void close()
	throws IOException
	{
		try
		{
			if (ps != null)
				ps.close();
		}
		catch (SQLException exception)
		{
			throw new IOException(exception);
		}
	}
	
	@Override
	public String toString()
	{
		return str.toString();
	}
	
	private void conjunctionCond(String cond1, String[] condN)
	{
		str.append(cond1);
		for (String cond : condN)
			str.append("\nAND ").append(cond);
	}
}

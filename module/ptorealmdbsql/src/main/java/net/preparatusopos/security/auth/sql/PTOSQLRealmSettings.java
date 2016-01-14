package net.preparatusopos.security.auth.sql;

import java.io.Serializable;

public class PTOSQLRealmSettings
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_GROUP_VIEW_NAME = "VW_GROUP";
	public static final String DEFAULT_GROUP_UID_COLUMN_NAME = "CL_UID";
	public static final String DEFAULT_GROUP_NAME_COLUMN_NAME = "CL_NAME";
	public static final String DEFAULT_CRED_TABLE_NAME = "TB_CRED";
	public static final String DEFAULT_CRED_UID_COLUMN_NAME = "CL_UID";
	public static final String DEFAULT_CRED_INDEX_COLUMN_NAME = "CL_INDEX";
	public static final String DEFAULT_CRED_NAME_COLUMN_NAME = "CL_NAME";
	public static final String DEFAULT_CRED_TYPE_COLUMN_NAME = "CL_TYPE";
	public static final String DEFAULT_CRED_PASSWD_COLUMN_NAME = "CL_PASSWD";
	
	private String schema;
	private String groupViewSchema;
	private String groupViewName;
	private String groupUIDColumnName;
	private String groupNameColumnName;
	private String credTableSchema;
	private String credTableName;
	private String credUIDColumnName;
	private String credIndexColumnName;
	private String credNameColumnName;
	private String credTypeColumnName;
	private String credPasswdColumnName;
	
	public PTOSQLRealmSettings()
	{
		schema = null;
		groupViewSchema = null;
		groupViewName = DEFAULT_GROUP_VIEW_NAME;
		groupUIDColumnName = DEFAULT_GROUP_UID_COLUMN_NAME;
		groupNameColumnName = DEFAULT_GROUP_NAME_COLUMN_NAME;
		credTableSchema = null;
		credTableName = DEFAULT_CRED_TABLE_NAME;
		credUIDColumnName = DEFAULT_CRED_UID_COLUMN_NAME;
		credIndexColumnName = DEFAULT_CRED_INDEX_COLUMN_NAME;
		credNameColumnName = DEFAULT_CRED_NAME_COLUMN_NAME;
		credTypeColumnName = DEFAULT_CRED_TYPE_COLUMN_NAME;
		credPasswdColumnName = DEFAULT_CRED_PASSWD_COLUMN_NAME;
	}
	
	public String getSchema()
	{
		return schema;
	}

	public void setSchema(String schema)
	{
		this.schema = schema;
	}

	public String getGroupViewSchema()
	{
		return getSchema(groupViewSchema);
	}

	public void setGroupViewSchema(String groupViewSchema)
	{
		this.groupViewSchema = groupViewSchema;
	}

	public String getGroupViewName()
	{
		return groupViewName;
	}

	public void setGroupViewName(String groupViewName)
	{
		this.groupViewName = groupViewName;
	}

	public String getGroupUIDColumnName()
	{
		return groupUIDColumnName;
	}

	public void setGroupUIDColumnName(String groupUIDColumnName)
	{
		this.groupUIDColumnName = groupUIDColumnName;
	}

	public String getGroupNameColumnName()
	{
		return groupNameColumnName;
	}

	public void setGroupNameColumnName(String groupNameColumnName)
	{
		this.groupNameColumnName = groupNameColumnName;
	}

	public String getCredTableSchema()
	{
		return getSchema(credTableSchema);
	}

	public void setCredTableSchema(String credTableSchema)
	{
		this.credTableSchema = credTableSchema;
	}

	public String getCredTableName()
	{
		return credTableName;
	}

	public void setCredTableName(String credTableName)
	{
		this.credTableName = credTableName;
	}

	public String getCredUIDColumnName()
	{
		return credUIDColumnName;
	}

	public void setCredUIDColumnName(String credUIDColumnName)
	{
		this.credUIDColumnName = credUIDColumnName;
	}

	public String getCredIndexColumnName()
	{
		return credIndexColumnName;
	}

	public void setCredIndexColumnName(String credIndexColumnName)
	{
		this.credIndexColumnName = credIndexColumnName;
	}
	
	public String getCredNameColumnName()
	{
		return credNameColumnName;
	}

	public void setCredNameColumnName(String credNameColumnName)
	{
		this.credNameColumnName = credNameColumnName;
	}

	public String getCredTypeColumnName()
	{
		return credTypeColumnName;
	}

	public void setCredTypeColumnName(String credTypeColumnName)
	{
		this.credTypeColumnName = credTypeColumnName;
	}

	public String getCredPasswdColumnName()
	{
		return credPasswdColumnName;
	}

	public void setCredPasswdColumnName(String credPasswdColumnName)
	{
		this.credPasswdColumnName = credPasswdColumnName;
	}
	
	private String getSchema(String tableSchema)
	{
		return tableSchema == null ? getSchema() : tableSchema;
	}
}

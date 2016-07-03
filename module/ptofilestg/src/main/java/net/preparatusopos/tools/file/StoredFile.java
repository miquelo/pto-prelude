package net.preparatusopos.tools.file;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoredFile
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String providerName;
	private String name;
	private Date creationDate;
	private Map<String, String> providerData;
	
	public StoredFile()
	{
		providerName = null;
		name = null;
		creationDate = null;
		providerData = new HashMap<>();
	}

	public String getProviderName()
	{
		return providerName;
	}

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public Map<String, String> getProviderData()
	{
		return providerData;
	}

	public void setProviderData(Map<String, String> providerData)
	{
		this.providerData = providerData;
	}
	
	public static StoredFile create(String providerName, String name)
	{
		StoredFile file = new StoredFile();
		file.setProviderName(providerName);
		file.setName(name);
		file.setCreationDate(new Date());
		return file;
	}
}

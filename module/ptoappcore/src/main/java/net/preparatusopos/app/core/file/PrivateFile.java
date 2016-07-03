package net.preparatusopos.app.core.file;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.preparatusopos.app.core.domain.model.ManagedFile;

public class PrivateFile
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long fid;
	private String deviceName;
	private String name;
	private Date creationDate;
	private Map<String, String> deviceData;
	
	public PrivateFile()
	{
		fid = 0l;
		deviceName = null;
		name = null;
		creationDate = null;
		deviceData = new HashMap<>();
	}
	
	public long getFID()
	{
		return fid;
	}
	
	public void setFID(long fid)
	{
		this.fid = fid;
	}
	
	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
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
	
	public Map<String, String> getDeviceData()
	{
		return deviceData;
	}

	public void setDeviceData(Map<String, String> deviceData)
	{
		this.deviceData = deviceData;
	}
	
	public ManagedFile restore()
	{
		ManagedFile managedFile = new ManagedFile();
		managedFile.setFID(fid);
		managedFile.setDeviceName(deviceName);
		managedFile.setName(name);
		managedFile.setCreationDate(creationDate);
		managedFile.setDeviceData(deviceData);
		return managedFile;
	}
}

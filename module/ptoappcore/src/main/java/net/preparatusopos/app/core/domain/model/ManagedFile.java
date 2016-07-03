package net.preparatusopos.app.core.domain.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.preparatusopos.app.core.domain.model.meta.ManagedFile_;
import net.preparatusopos.app.core.file.PrivateFile;
import net.preparatusopos.tools.file.StoredFile;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_MANAGEDFILE"
)
public class ManagedFile
{
	private long fid;
	private String deviceName;
	private String name;
	private Date creationDate;
	private Map<String, String> deviceData;
	
	public ManagedFile()
	{
		fid = 0l;
		deviceName = null;
		name = null;
		creationDate = null;
		deviceData = new HashMap<>();
	}

	@Id
	@GeneratedValue(
		strategy=GenerationType.IDENTITY
	)
	@Column(
		name="CL_FID"
	)
	public long getFID()
	{
		return fid;
	}
	
	public void setFID(long fid)
	{
		this.fid = fid;
	}

	@Column(
		name="CL_DEVNAME"
	)
	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	@Column(
		name="CL_NAME"
	)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Temporal(
		TemporalType.TIMESTAMP
	)
	@Column(
		name="CL_CDATE"
	)
	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	@ElementCollection
	@CollectionTable(
		name="TB_MANAGEDFILEDEVDATA",
		joinColumns=@JoinColumn(
			name="CL_FID"
		)
	)
	@MapKeyColumn(
		name="CL_KEY"
	)
	@Column(
		name="CL_VALUE"
	)
	public Map<String, String> getDeviceData()
	{
		return deviceData;
	}

	public void setDeviceData(Map<String, String> deviceData)
	{
		this.deviceData = deviceData;
	}
	
	@Transient
	public StoredFile getStoredFile(String providerName)
	{
		StoredFile storedFile = new StoredFile();
		storedFile.setProviderName(providerName);
		storedFile.setName(name);
		storedFile.setCreationDate(creationDate);
		storedFile.setProviderData(deviceData);
		return storedFile;
	}

	@Override
	public int hashCode()
	{
		return (int) (fid % 800l);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ManagedFile)
		{
			ManagedFile file = (ManagedFile) obj;
			return fid == file.fid;
		}
		return false;
	}
	
	public static ManagedFile find(EntityManager em, String deviceName,
			String name)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ManagedFile> cq = cb.createQuery(ManagedFile.class);
		Root<ManagedFile> root = cq.from(ManagedFile.class);
		cq.select(root);
		cq.where(cb.and(
			cb.equal(root.get(ManagedFile_.deviceName), deviceName),
			cb.equal(root.get(ManagedFile_.name), name))
		);
		TypedQuery<ManagedFile> q = em.createQuery(cq);
		return q.getSingleResult();
	}
	
	public static ManagedFile create(String deviceName, StoredFile storedFile)
	{
		ManagedFile file = new ManagedFile();
		file.setDeviceName(deviceName);
		file.setName(storedFile.getName());
		file.setCreationDate(storedFile.getCreationDate());
		file.setDeviceData(storedFile.getProviderData());
		return file;
	}
	
	public PrivateFile transport()
	{
		PrivateFile privateFile = new PrivateFile();
		privateFile.setFID(fid);
		privateFile.setDeviceName(deviceName);
		privateFile.setName(name);
		privateFile.setCreationDate(creationDate);
		privateFile.setDeviceData(deviceData);
		return privateFile;
	}
}
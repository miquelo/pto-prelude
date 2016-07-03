package net.preparatusopos.app.ma.web;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.activation.MimeType;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.FileStorage;
import net.preparatusopos.app.domain.Geography;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.Region;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RegionType;
import net.preparatusopos.app.ma.web.util.Util;
import net.preparatusopos.faces.event.UploadFileBehaviorEvent;
import net.preparatusopos.faces.model.FileUploadList;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

@ManagedBean(
	name="accountBean"
)
@ViewScoped
public class AccountBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final String COUNTRY_CODE_SPAIN = "ES";
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private transient Membership membership;
	
	@EJB(
		name="ejb/FileStorageBean"
	)
	private transient FileStorage fileStorage;
	
	@EJB(
		name="ejb/GeographyBean"
	)
	private transient Geography geography;
	
	@ManagedProperty("#{userBean}")
	private UserBean userBean;
	
	private FileUploadList photos;
	private boolean uploadingPhoto;
	private SortedSet<Region> provinces;
	private RegionRef provinceRef;
	private SortedSet<Region> localities;
	private RegionRef localityRef;
	
	public AccountBean()
	{
		membership = null;
		fileStorage = null;
		userBean = null;
		photos = new FileUploadList();
		uploadingPhoto = false;
		provinces = null;
		provinceRef = null;
		localities = null;
		localityRef = null;
	}
	
	public void setUserBean(UserBean userBean)
	{
		this.userBean = userBean;
	}
	
	public Set<Region> getProvinces()
	{
		try
		{
			if (provinces == null)
			{
				provinces = new TreeSet<>(new RegionComparator());
				provinces.addAll(geography.fetch(new CountryRef(
						COUNTRY_CODE_SPAIN), RegionType.PROVINCE,
						Util.getRequestLocale()));
			}
			return provinces;
		}
		catch (GeographyException exception)
		{
			throw new FacesException(exception);
		}
	}
	
	public Set<Region> getLocalities()
	{
		try
		{
			if (localities == null)
			{
				localities = new TreeSet<>(new RegionComparator());
				if (provinceRef == null)
					localities.addAll(geography.fetch(new CountryRef(
							COUNTRY_CODE_SPAIN), RegionType.LOCALITY,
							Util.getRequestLocale()));
				else
					localities.addAll(geography.fetch(provinceRef,
							RegionType.LOCALITY, Util.getRequestLocale()));
			}
			return localities;
		}
		catch (GeographyException exception)
		{
			throw new FacesException(exception);
		}
	}
	
	public boolean isUploadingPhoto()
	{
		return uploadingPhoto;
	}
	
	public FileUploadList getPhotos()
	{
		return photos;
	}

	public void setPhotos(FileUploadList photos)
	{
		this.photos = photos;
	}
	
	public String getMemberName()
	{
		return userBean.getName();
	}
	
	public void setMemberName(String memberName)
	{
		userBean.setName(memberName);
	}
	
	public String getMemberSurname()
	{
		return userBean.getSurname();
	}
	
	public void setMemberSurname(String memberSurname)
	{
		userBean.setSurname(memberSurname);
	}

	public RegionRef getProvinceRef()
	{
		return provinceRef;
	}

	public void setProvinceRef(RegionRef provinceRef)
	{
		this.provinceRef = provinceRef;
	}

	public RegionRef getLocalityRef()
	{
		return localityRef;
	}

	public void setLocalityRef(RegionRef localityRef)
	{
		this.localityRef = localityRef;
	}
	
	public void changeProvince(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		localities = null;
		localityRef = null;
	}
	
	public void changeLocality(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			provinceRef = geography.parent(localityRef, RegionType.PROVINCE);
		}
		catch (GeographyException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}

	public void uploadPhoto(UploadFileBehaviorEvent event)
	throws AbortProcessingException
	{
		if (photos.begin(event))
		{
			uploadPhotoBegin(event);
			uploadingPhoto = true;
		}
		else if (photos.completed(event))
		{
			uploadingPhoto = false;
			uploadPhotoCompleted(event);
		}
	}
	
	private void uploadPhotoBegin(UploadFileBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			photos.clear();
			long size = event.getTotalSize();
			MimeType contentType = event.getContentType();
			StoredFile storedFile = membership.storeMemberPhoto(size,
					contentType);
			photos.push(event, storedFile);
			userBean.updateMemberInfo();
		}
		catch (MembershipException | FileStorageException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	private void uploadPhotoCompleted(UploadFileBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			uploadingPhoto = false;
			fileStorage.submit(photos.getStoredFile(event));
		}
		catch (FileStorageException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	private static class RegionComparator
	implements Comparator<Region>
	{
		public RegionComparator()
		{
		}
		
		@Override
		public int compare(Region r1, Region r2)
		{
			return r1.getDisplayName().compareTo(r2.getDisplayName());
		}
		
	}
}

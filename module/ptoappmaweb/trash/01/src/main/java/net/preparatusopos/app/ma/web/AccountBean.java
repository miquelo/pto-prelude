package net.preparatusopos.app.ma.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;

import net.preparatusopos.app.domain.CredentialInfo;
import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.ProfileInfo;
import net.preparatusopos.app.domain.ProfileRequestInfo;
import net.preparatusopos.app.domain.ProfileRequestType;
import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.ma.web.util.Util;

@ManagedBean(
	name="accountBean"
)
@ViewScoped
public class AccountBean
implements Serializable
{
	private static final long serialVersionUID = 1L;

	@EJB(
		name="ejb/MembershipBean"
	)
	private transient Membership membership;
	
	private String memberName;
	private String memberSurname;
	private List<CredentialEntry> credentials;
	private CredentialEntry credentialRemoval;
	private List<ProfileEntry> profiles;
	private ProfileEntry profileAdding;
	private ProfileEntry profileRemoval;
	
	public AccountBean()
	{
		membership = null;
		memberName = null;
		memberSurname = null;
		credentials = new ArrayList<>();
		credentialRemoval = null;
		profiles = new ArrayList<>();
		profileAdding = null;
		profileRemoval = null;
	}
	
	public ProfileType[] getProfileTypes()
	{
		try
		{
			return membership.availableProfileTypes();
		}
		catch (MembershipException exception)
		{
			throw new IllegalStateException(exception);
		}
	}

	public String getMemberName()
	{
		return memberName;
	}

	public void setMemberName(String memberName)
	{
		this.memberName = Util.normalized(memberName);
	}

	public String getMemberSurname()
	{
		return memberSurname;
	}

	public void setMemberSurname(String memberSurname)
	{
		this.memberSurname = Util.normalized(memberSurname);
	}
	
	public List<CredentialEntry> getCredentials()
	{
		return credentials;
	}
	
	public List<ProfileEntry> getProfiles()
	{
		return profiles;
	}
	
	public CredentialEntry getCredentialRemoval()
	{
		return credentialRemoval;
	}
	
	public ProfileEntry getProfileAdding()
	{
		return profileAdding;
	}
	
	public ProfileEntry getProfileRemoval()
	{
		return profileRemoval;
	}
	
	@PostConstruct
	public void init()
	{
		try
		{
			MemberInfo memberInfo = membership.fetchMemberInfo();
			memberName = memberInfo.getName();
			memberSurname = memberInfo.getSurname();
			
			int index = 0;
			for (CredentialInfo info : membership.fetchCredentials())
			{
				CredentialEntry entry = new CredentialEntry();
				entry.setIndex(index++);
				switch (info.getType())
				{
					case MAIL_ADDRESS:
					entry.setType(CredentialEntryType.MAIL_ADDRESS);
					entry.setDescription(info.getMailAddress());
					break;
					
					case EXTERNAL_UID:
					switch (info.getExternalUIDType())
					{
						case GOOGLE:
						entry.setType(CredentialEntryType.GOOGLE_ACCOUNT);
					}
					entry.setDescription(info.getExternalUID());
					break;
				}
				credentials.add(entry);
			}
			
			updateProfiles();
		}
		catch (MembershipException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	public Object removeCredential()
	{
		try
		{
			int index = credentialRemoval.getIndex();
			membership.removeCredential(index);
			Util.logout();
			return "credential-removed";
		}
		catch (MembershipException exception)
		{
			return null;
		}
	}
	
	public void updateInfo(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			MemberInfo memberInfo = new MemberInfo();
			memberInfo.setName(getMemberName());
			memberInfo.setSurname(getMemberSurname());
			membership.updateMember(memberInfo);
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	public void removeCredentialCancel(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		credentialRemoval = null;
	}
	
	public void addProfile(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		profileAdding = new ProfileEntry();
	}
	
	public void addProfileConfirm(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			ProfileInfo info = profileAdding.getInfo();
			ProfileRequestInfo requestInfo = new ProfileRequestInfo(
					info.getType()); // XXX info.getType() is null!!!
			requestInfo.setRequestType(ProfileRequestType.AUTOMATIC);
			membership.requestProfile(requestInfo);
			
			profileAdding = null;
			updateProfiles();
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	public void addProfileCancel(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		profileAdding = null;
	}
	
	public void removeProfileConfirm(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		try
		{
			membership.removeProfile(profileRemoval.getInfo().getType());
			profileRemoval = null;
			updateProfiles();
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	public void removeProfileCancel(AjaxBehaviorEvent event)
	throws AbortProcessingException
	{
		profileRemoval = null;
	}
	
	private void updateProfiles()
	throws MembershipException
	{
		profiles.clear();
		for (ProfileInfo info : membership.fetchProfiles())
		{
			ProfileEntry entry = new ProfileEntry();
			entry.setInfo(info);
			profiles.add(entry);
		}
	}
	
	public class CredentialEntry
	implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private int index;
		private CredentialEntryType type;
		private String description;
		
		public CredentialEntry()
		{
			index = -1;
			type = null;
			description = null;
		}
		
		public int getIndex()
		{
			return index;
		}

		public void setIndex(int index)
		{
			this.index = index;
		}

		public CredentialEntryType getType()
		{
			return type;
		}

		public void setType(CredentialEntryType type)
		{
			this.type = type;
		}

		public String getDescription()
		{
			return description;
		}

		public void setDescription(String description)
		{
			this.description = description;
		}

		public void remove(AjaxBehaviorEvent event)
		throws AbortProcessingException
		{
			credentialRemoval = this;
		}
	}
	
	public class ProfileEntry
	implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private ProfileInfo info;
		
		public ProfileEntry()
		{
			info = new ProfileInfo();
		}

		public ProfileInfo getInfo()
		{
			return info;
		}

		public void setInfo(ProfileInfo info)
		{
			this.info = info;
		}
		
		public void remove(AjaxBehaviorEvent event)
		throws AbortProcessingException
		{
			profileRemoval = this;
		}
	}
	
	public static enum CredentialEntryType
	{
		MAIL_ADDRESS,
		GOOGLE_ACCOUNT
	}
}
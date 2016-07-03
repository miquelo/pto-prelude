package net.preparatusopos.app.ma.web;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletException;

import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.domain.ReferencedMemberInfo;
import net.preparatusopos.app.ma.web.util.Util;
import net.preparatusopos.tools.file.FileStorageException;

@ManagedBean(
	name="userBean"
)
@SessionScoped
public class UserBean
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private transient Membership membership;
	
	private ReferencedMemberInfo memberInfo;
	
	public UserBean()
	{
		membership = null;
		memberInfo = null;
	}

	public boolean isLogged()
	{
		return memberInfo != null;
	}
	
	public String getRef()
	{
		return memberInfo == null ? null : memberInfo.getRef();
	}
	
	public String getPhotoFileName()
	{
		return memberInfo == null || memberInfo.getPhotoFile() == null ? null
				: memberInfo.getPhotoFile().getName();
	}

	public String getName()
	{
		return memberInfo == null ? null : memberInfo.getName();
	}
	
	public void setName(String name)
	{
		getRequiredMemberInfo().setName(Util.nullableString(name));
	}
	
	public String getSurname()
	{
		return memberInfo == null ? null : memberInfo.getSurname();
	}
	
	public void setSurname(String surname)
	{
		getRequiredMemberInfo().setSurname(Util.nullableString(surname));
	}
	
	public void updateMemberInfo()
	throws MembershipException, FileStorageException
	{
		memberInfo = Util.isLogged() ? membership.fetchMemberInfo() : null;
	}
	
	public void login(String username, byte[] password)
	throws ServletException, MembershipException, FileStorageException
	{
		Util.login(username, password);
		memberInfo = membership.fetchMemberInfo();
	}
	
	public void login(String username, String password)
	throws ServletException, MembershipException, FileStorageException
	{
		login(username, Util.passwordHash(password.toCharArray()));
	}
	
	public void logout(ComponentSystemEvent event)
	throws AbortProcessingException
	{
		try
		{
			Util.logout();
			memberInfo = null;
			Util.redirectToHome();
		}
		catch (IOException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	public void memberInfoChanged(AjaxBehaviorEvent event)
	{
		try
		{
			membership.updateMemberInfo(memberInfo);
		}
		catch (MembershipException exception)
		{
			throw new AbortProcessingException(exception);
		}
	}
	
	@PostConstruct
	public void init()
	{
		try
		{
			updateMemberInfo();
		}
		catch (MembershipException | FileStorageException exception)
		{
			throw new FacesException(exception);
		}
	}
	
	private ReferencedMemberInfo getRequiredMemberInfo()
	{
		if (memberInfo == null)
			throw new IllegalStateException("Member info is empty");
		return memberInfo;
	}
}

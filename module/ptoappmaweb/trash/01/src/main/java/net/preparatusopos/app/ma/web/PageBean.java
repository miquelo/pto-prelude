package net.preparatusopos.app.ma.web;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;

import net.preparatusopos.app.domain.MemberInfo;
import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.app.ma.web.util.Util;

@ManagedBean(
	name="pageBean"
)
@RequestScoped
public class PageBean
{
	private static final String GOOGLE_CLIENT_ID_PROPERTY_NAME =
			"googleClientId";
	
	@Resource(
		name="properties/PTOSettings"
	)
	private Properties settings;
	
	@EJB(
		name="ejb/MembershipBean"
	)
	private Membership membership;
	
	@ManagedProperty("#{messages}")
	private ResourceBundle messages;
	
	public PageBean()
	{
		settings = null;
		membership = null;
	}

	public boolean isLogged()
	{
		return Util.isLogged();
	}
	
	public String getMemberName()
	{
		try
		{
			MemberInfo memberInfo = membership.fetchMemberInfo();
			String memberName = memberInfo.getName();
			return memberName == null
					? messages.getString("annonymousMemberName") : memberName;
		}
		catch (MembershipException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	public String getGoogleClientId()
	{
		return settings.getProperty(GOOGLE_CLIENT_ID_PROPERTY_NAME);
	}
	
	public void setMessages(ResourceBundle messages)
	{
		this.messages = messages;
	}

	public void logout(ComponentSystemEvent event)
	throws AbortProcessingException
	{
		Util.logout();
	}
}
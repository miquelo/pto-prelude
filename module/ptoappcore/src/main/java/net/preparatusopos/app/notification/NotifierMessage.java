package net.preparatusopos.app.notification;

import java.io.Serializable;

public class NotifierMessage
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String target;
	private String subjectText;
	private String bodyText;
	
	public NotifierMessage()
	{
		target = null;
		subjectText = null;
		bodyText = null;
	}

	public String getTarget()
	{
		return target;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}

	public String getSubjectText()
	{
		return subjectText;
	}

	public void setSubjectText(String subjectText)
	{
		this.subjectText = subjectText;
	}

	public String getBodyText()
	{
		return bodyText;
	}

	public void setBodyText(String bodyText)
	{
		this.bodyText = bodyText;
	}
}

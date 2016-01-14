package net.preparatusopos.app.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Request
{
	private byte[] token;
	private Date creationDate;
	
	public Request()
	{
		token = null;
		creationDate = new Date();
	}

	@Column(
		name="CL_TOKEN"
	)
	public byte[] getToken()
	{
		return token;
	}

	public void setToken(byte[] token)
	{
		this.token = token;
	}
	
	@Column(
		name="CL_CREATION"
	)
	@Temporal(
		TemporalType.DATE
	)
	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
}

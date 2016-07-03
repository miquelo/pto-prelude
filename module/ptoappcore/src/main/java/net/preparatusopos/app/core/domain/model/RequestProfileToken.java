package net.preparatusopos.app.core.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_REQPROFTOKEN"
)
@DiscriminatorValue(AuthorizationToken.TYPE_PROFILE)
public class RequestProfileToken
extends AuthorizationToken
{
	private Member owner;
	private RequestProfile request;
	
	public RequestProfileToken()
	{
		owner = null;
		request = null;
	}
	
	@ManyToOne
	@JoinColumn(
		name="CL_ONWERUID",
		referencedColumnName="CL_UID"
	)
	public Member getOwner()
	{
		return owner;
	}

	public void setOwner(Member owner)
	{
		this.owner = owner;
	}

	@OneToOne
	@JoinColumn(
		name="CL_REQREF",
		referencedColumnName="CL_REF"
	)
	public RequestProfile getRequest()
	{
		return request;
	}

	public void setRequest(RequestProfile request)
	{
		this.request = request;
	}

	@Override
	protected String getTypeAsString()
	{
		return TYPE_PROFILE;
	}
}
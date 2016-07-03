package net.preparatusopos.app.core.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.preparatusopos.app.domain.Token;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_REQDIRPROFTOKEN"
)
@DiscriminatorValue(AuthorizationToken.TYPE_DIRECT_PROFILES)
public class RequestDirectProfilesToken
extends AuthorizationToken
{
	private RequestCredential credential;
	private Set<RequestProfile> profiles;
	
	public RequestDirectProfilesToken()
	{
		credential = null;
		profiles = new HashSet<>();
	}
	
	@OneToOne(
		cascade=CascadeType.ALL
	)
	@JoinColumn(
		name="CL_REQCREF",
		referencedColumnName="CL_REF"
	)
	public RequestCredential getCredential()
	{
		return credential;
	}

	public void setCredential(RequestCredential credential)
	{
		this.credential = credential;
	}

	@OneToMany(
		cascade=CascadeType.ALL
	)
	@JoinTable(
		name="TB_REQDIRPROFTOKENREQP",
		joinColumns=@JoinColumn(
			name="CL_REQDPTOKENREF",
			referencedColumnName="CL_REF"
		),
		inverseJoinColumns=@JoinColumn(
			name="CL_REQPREF",
			referencedColumnName="CL_REF"
		)
	)
	public Set<RequestProfile> getProfiles()
	{
		return profiles;
	}

	public void setProfiles(Set<RequestProfile> profiles)
	{
		this.profiles = profiles;
	}
	
	public static RequestDirectProfilesToken build()
	{
		RequestDirectProfilesToken token = new RequestDirectProfilesToken();
		token.setValue(Token.createValue());
		return token;
	}

	@Override
	protected String getTypeAsString()
	{
		return TYPE_DIRECT_PROFILES;
	}
}
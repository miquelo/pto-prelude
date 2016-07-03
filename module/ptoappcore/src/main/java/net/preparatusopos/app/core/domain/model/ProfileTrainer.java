package net.preparatusopos.app.core.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILETRAIN"
)
@DiscriminatorValue(Profile.ROLE_TRAINER)
public class ProfileTrainer
extends Profile
{
	private List<Long> locationRefs;
	
	public ProfileTrainer()
	{
		locationRefs = new ArrayList<>();
	}

	@ElementCollection(
		fetch=FetchType.LAZY
	)
	@CollectionTable(
		name="TB_PROFTRAINLOC",
		joinColumns={
			@JoinColumn(
				name="CL_PROF",
				referencedColumnName="CL_REF"
			)
		}
	)
	@Column(
		name="CL_LOCREF"
	)
	@OrderColumn(
		name="CL_INDEX"
	)
	public List<Long> getLocationRefs()
	{
		return locationRefs;
	}

	public void setLocationRefs(List<Long> locationRefs)
	{
		this.locationRefs = locationRefs;
	}
	
	@Override
	protected String getRoleAsString()
	{
		return Profile.ROLE_TRAINER;
	}

	@Override
	protected Profile cloneRole()
	{
		return new ProfileTrainer();
	}
}

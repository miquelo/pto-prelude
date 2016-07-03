package net.preparatusopos.app.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(
	schema="PTOAPP",
	name="TB_PROFILETRAIN"
)
@Inheritance(
	strategy=InheritanceType.SINGLE_TABLE
)
public abstract class ProfileTrainer
extends Profile
{
	private List<Long> locationRefs;
	
	protected ProfileTrainer()
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
				name="CL_PFTOWNERUID",
				referencedColumnName="CL_OWNERUID"
			),
			@JoinColumn(
				name="CL_PFTTYPE",
				referencedColumnName="CL_TYPE"
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
}
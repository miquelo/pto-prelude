package net.preparatusopos.app.core.geography.model.internal;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="regions"
)
public class RegionsData
{
	private Set<RegionData> items;
	
	public RegionsData()
	{
		items = new HashSet<>();
	}
	
	@XmlElement(
		namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
		name="region"
	)
	public Set<RegionData> getItems()
	{
		return items;
	}
	
	public void setItems(Set<RegionData> items)
	{
		this.items = items;
	}
}

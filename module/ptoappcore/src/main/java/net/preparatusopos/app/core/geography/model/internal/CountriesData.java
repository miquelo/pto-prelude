package net.preparatusopos.app.core.geography.model.internal;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="countries"
)
public class CountriesData
{
	private Set<CountryData> items;
	
	public CountriesData()
	{
		items = new HashSet<>();
	}
	
	@XmlElement(
		namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
		name="country"
	)
	public Set<CountryData> getItems()
	{
		return items;
	}
	
	public void setItems(Set<CountryData> items)
	{
		this.items = items;
	}
}

package net.preparatusopos.app.core.geography.model.internal;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="routes"
)
public class RoutesData
{
	private Set<RouteData> items;
	
	public RoutesData()
	{
		items = new HashSet<>();
	}
	
	@XmlElement(
		namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
		name="route"
	)
	public Set<RouteData> getItems()
	{
		return items;
	}
	
	public void setItems(Set<RouteData> items)
	{
		this.items = items;
	}
}

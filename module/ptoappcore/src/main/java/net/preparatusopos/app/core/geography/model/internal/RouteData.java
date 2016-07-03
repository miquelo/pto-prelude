package net.preparatusopos.app.core.geography.model.internal;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.preparatusopos.app.core.geography.model.NodeManager;
import net.preparatusopos.app.core.geography.model.RouteNode;
import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.RegionRef;
import net.preparatusopos.app.domain.RouteRef;

@XmlType(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="RouteType"
)
public class RouteData
{
	private String name;
	private Set<String> inside;
	private Set<String> zipCodes;
	private Set<LocalizedValueData> displayName;
	
	public RouteData()
	{
		name = null;
		inside = new HashSet<>();
		zipCodes = new HashSet<>();
		displayName = new HashSet<>();
	}

	@XmlAttribute(
		name="name",
		required=true
	)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@XmlElement(
		namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
		name="inside"
	)
	public Set<String> getInside()
	{
		return inside;
	}

	public void setInside(Set<String> inside)
	{
		this.inside = inside;
	}

	@XmlElement(
		namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
		name="display-name"
	)
	public Set<LocalizedValueData> getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(Set<LocalizedValueData> displayName)
	{
		this.displayName = displayName;
	}
	
	public RouteNode build(NodeManager manager, CountryRef countryRef)
	{
		return RouteNode.create(manager, new RouteRef(countryRef, name), map(
				countryRef, inside), zipCodes, LocalizedValueData.build(
				displayName));
	}
	
	private static Set<RegionRef> map(CountryRef countryRef,
			Set<String> inside)
	{
		Set<RegionRef> result = new HashSet<>();
		for (String name : inside)
			result.add(new RegionRef(countryRef, name));
		return result;
	}
}

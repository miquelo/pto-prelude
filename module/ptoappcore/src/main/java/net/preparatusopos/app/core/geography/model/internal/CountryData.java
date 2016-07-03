package net.preparatusopos.app.core.geography.model.internal;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.preparatusopos.app.core.geography.model.CountryNode;
import net.preparatusopos.app.core.geography.model.NodeManager;
import net.preparatusopos.app.domain.CountryRef;

@XmlType(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="CountryType"
)
public class CountryData
{
	private String code;
	private Set<LocalizedValueData> displayName;
	
	public CountryData()
	{
		this.code = null;
		this.displayName = new HashSet<>();
	}

	@XmlAttribute(
		name="code",
		required=true
	)
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
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
	
	public CountryNode build(NodeManager manager)
	{
		return CountryNode.create(manager, new CountryRef(code),
				LocalizedValueData.build( displayName));
	}
}

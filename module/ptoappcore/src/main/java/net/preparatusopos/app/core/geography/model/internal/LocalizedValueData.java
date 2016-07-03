package net.preparatusopos.app.core.geography.model.internal;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import net.preparatusopos.app.core.geography.model.LocalizedValue;

@XmlType(
	namespace="http://xmlns.preparatusopos.net/xml/ns/geography",
	name="LocalizedValueType"
)
public class LocalizedValueData
{
	private String languageTag;
	private String text;
	
	public LocalizedValueData()
	{
		languageTag = null;
		text = null;
	}

	@XmlAttribute(
		name="language",
		required=true
	)
	public String getLanguageTag()
	{
		return languageTag;
	}

	public void setLanguageTag(String languageTag)
	{
		this.languageTag = languageTag;
	}
	
	@XmlValue
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public static LocalizedValue build(Set<LocalizedValueData> data)
	{
		LocalizedValue result = new LocalizedValue();
		for (LocalizedValueData item : data)
			result.put(item.getLanguageTag(), item.getText());
		return result;
	}
}

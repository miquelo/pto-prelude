package net.preparatusopos.app.ma.web.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.RegionRef;

@FacesConverter(
	value="regionRef",
	forClass=RegionRef.class
)
public class RegionRefConverter
implements Converter
{
	public RegionRefConverter()
	{
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value)
	{
		if (value == null)
			return null;
		String[] parts = value.split("@");
		CountryRef countryRef = new CountryRef(parts[0]);
		return countryRef.regionRef(parts[1]);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value)
	{
		if (value == null)
			return null;
		RegionRef regionRef = (RegionRef) value;
		return String.format("%s@%s", regionRef.getCountryCode(),
				regionRef.getName());
	}
}
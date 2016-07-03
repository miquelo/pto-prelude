package net.preparatusopos.app.ma.web.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.preparatusopos.app.domain.ProfileType;
import net.preparatusopos.app.ma.web.RegisterBean.ProfileCountryField;

@FacesConverter(
	value="profileCountryField",
	forClass=ProfileCountryField.class
)
public class ProfileCountryFieldConverter
implements Converter
{
	public ProfileCountryFieldConverter()
	{
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value)
	{
		if (value == null)
			return null;
		ProfileCountryField pcf = new ProfileCountryField();
		String[] parts = value.split("@");
		pcf.setCountry(ProfileType.Country.values()[Integer.parseInt(
				parts[0])]);
		pcf.setField(ProfileType.Field.values()[Integer.parseInt(parts[1])]);
		return pcf;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value)
	{
		if (value == null)
			return null;
		ProfileCountryField pcf = (ProfileCountryField) value;
		return String.format("%d@%d", pcf.getCountry().ordinal(),
				pcf.getField().ordinal());
	}
}
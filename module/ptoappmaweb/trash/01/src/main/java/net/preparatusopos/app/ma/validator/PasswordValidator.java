package net.preparatusopos.app.ma.web.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(
	"passwordValidator"
)
public class PasswordValidator
implements Validator
{
	public PasswordValidator()
	{
	}
	
	@Override
	public void validate(FacesContext context, UIComponent comp, Object value)
	throws ValidatorException
	{
		String password = (String) value;
		
		Map<String, Object> attrs = comp.getAttributes();
		UIInput input = (UIInput) attrs.get("passwordRepeat");
		String passwordRepeat = (String) input.getSubmittedValue();
		
		if (password != null && !password.isEmpty() && passwordRepeat != null
				&& !passwordRepeat.isEmpty()
				&& !password.equals(passwordRepeat))
		{
			input.setValid(false);
			throw new ValidatorException(new FacesMessage(
					"Password does not match with repeated password"));
		}
	}
}
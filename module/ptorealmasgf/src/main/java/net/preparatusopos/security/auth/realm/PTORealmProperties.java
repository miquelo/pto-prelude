package net.preparatusopos.security.auth.realm;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.sun.enterprise.security.auth.realm.BadRealmException;

public class PTORealmProperties
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Properties properties;
	
	public PTORealmProperties(Properties properties)
	{
		this.properties = properties;
	}
	
	public String getProperty(String name)
	throws BadRealmException
	{
		return properties.getProperty(name);
	}
	
	public String getRequiredProperty(String name)
	throws BadRealmException
	{
		String value = getProperty(name);
		if (value == null || value.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Property '").append(name).append("' is required");
			throw new BadRealmException(sb.toString());
		}
		return value;
	}
	
	public void store(Object obj, String prefix)
	throws BadRealmException
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor desc : info.getPropertyDescriptors())
			{
				Object value = propertyValue(prefix, desc);
				if (value != null)
				{
					Method method = desc.getWriteMethod();
					method.invoke(obj, value);
				}
			}
		}
		catch (IntrospectionException | InvocationTargetException |
				IllegalAccessException exception)
		{
			throw new BadRealmException(exception);
		}
	}
	
	private Object propertyValue(String prefix, PropertyDescriptor desc)
	throws BadRealmException
	{
		String descName = desc.getName();
		StringBuilder name = new StringBuilder();
		if (prefix == null)
			name.append(descName);
		else
		{
			name.append(prefix);
			name.append(Character.toUpperCase(descName.charAt(0)));
			name.append(descName.substring(1));
		}
		return getProperty(name.toString());
	}
}

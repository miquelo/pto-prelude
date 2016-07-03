package net.preparatusopos.faces.util;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletContext;

public class Util
{
	private static final String STYLE_CLASS_ATTRIBUTE_NAME = "class";
	private static final String BEHAVIOR_EVENT_PROPERTY_NAME =
			"javax.faces.behavior.event";
	private static final String SOURCE_PROPERTY_NAME = "javax.faces.source";
	
	private Util()
	{
	}
	
	public static void notNull(String varname, Object var)
	{
		if (var == null)
			throw new NullPointerException(varname + " cannot be null");
	}
	
	public static void renderParamsNotNull(FacesContext context,
			UIComponent component)
	{
		Util.notNull("context", context);
		Util.notNull("component", component);
	}
	
	public static ServletContext getServletContext()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Object context = externalContext.getContext();
		if (context instanceof ServletContext)
			return (ServletContext) context;
		
		StringBuilder msg = new StringBuilder();
		msg.append("Context of type ").append(context.getClass());
		msg.append(" is not supported");
		throw new UnsupportedOperationException(msg.toString());
	}
	
	public static boolean isValid(UIComponent component)
	{
		if (component instanceof UIInput && !((UIInput) component).isValid())
			return false;
		if (component instanceof UIPanel)
			for (UIComponent child : component.getChildren())
				if (!isValid(child))
					return false;
		return true;
	}
	
	public static RuntimeException newUnsupportedComponent(UIComponent
			component)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported component of type ");
		msg.append(component.getClass());
		return new IllegalStateException(msg.toString());
	}
	
	public static <T> T getAttribute(UIComponent component, String name,
			Class<T> type, T defaultValue)
	{
		T value = type.cast(component.getAttributes().get(name));
		return value == null ? defaultValue : value;
	}
	
	public static List<UIParameter> getParameters(UIComponent component)
	{
		List<UIParameter> paramList = new ArrayList<>();
		for (UIComponent child : component.getChildren())
			if (child instanceof UIParameter)
				paramList.add((UIParameter) child);
		return paramList;
	}
	
	public static <T> T getParameter(UIComponent component, String name,
			Class<T> type, T defaultValue)
	{
		for (UIComponent child : component.getChildren())
			if (child instanceof UIParameter)
			{
				UIParameter param = (UIParameter) child;
				if (name.equals(param.getName()))
				{
					T value = type.cast(param.getValue());
					return value == null ? defaultValue : value;
				}
			}
		return defaultValue;
	}
	
	public static List<ClientBehavior> getBehaviors(UIComponentBase component,
			String eventName)
	{
		Map<String, List<ClientBehavior>> behaviorsMap =
				component.getClientBehaviors();
		List<ClientBehavior> behaviors = behaviorsMap.get(eventName);
		return behaviors == null ? Collections.<ClientBehavior>emptyList()
				: behaviors;
	}
	
	public static List<SelectItem> selectItems(FacesContext context,
			UIComponent component)
	{
		List<SelectItem> list = new ArrayList<>();
		for (SelectItem selectItem : new SelectItemIterable(context, component))
			list.add(selectItem);
		return list;
	}
	
	public static Object getVariable(FacesContext context, String name)
	{
		ExternalContext externalContext = context.getExternalContext();
		return externalContext.getRequestMap().get(name);
	}
	
	public static void putVariable(FacesContext context, String name,
			Object value)
	{
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getRequestMap().put(name, value);
	}
	
	public static Object removeVariable(FacesContext context, String name)
	{
		ExternalContext externalContext = context.getExternalContext();
		return externalContext.getRequestMap().remove(name);
	}
	
	public static void decodeBehaviors(FacesContext context,
			UIComponentBase component)
	{
		Map<String, List<ClientBehavior>> behaviorsMap =
				component.getClientBehaviors();
		if (!behaviorsMap.isEmpty())
		{
			ExternalContext externalContext = context.getExternalContext();
			Map<String, String> params =
					externalContext.getRequestParameterMap();
			String behaviorEvent = params.get(BEHAVIOR_EVENT_PROPERTY_NAME);
			
			if (behaviorEvent != null)
			{
				List<ClientBehavior> behaviors = behaviorsMap.get(
						behaviorEvent);
				if (behaviors != null && !behaviors.isEmpty())
				{
					String behaviorSource = params.get(SOURCE_PROPERTY_NAME);
					if (isBehaviorSource(context, behaviorSource,
							component.getClientId()))
						for (ClientBehavior behavior : behaviors)
							behavior.decode(context, component);
				}
			}
		}
	}
	
	public static void encodeStyleClass(FacesContext context,
			Set<String> styleClasses, String propertyName)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.writeAttribute(STYLE_CLASS_ATTRIBUTE_NAME, getStyleClassValue(
				styleClasses), propertyName);
	}
	
	public static void encodeOnEventAttribute(FacesContext context,
			UIComponentBase component, String name, String eventName)
	throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		StringBuilder js = new StringBuilder();
		
		String inlineScript = getAttribute(component, name, String.class, null);
		if (inlineScript != null)
			writeScriptChain(js, inlineScript);
		List<ClientBehavior> behaviors = getBehaviors(component, eventName);
		if (!behaviors.isEmpty())
			writeScriptBehaviors(context, component, js, eventName, behaviors);
		
		if (js.length() > 0)
			writer.writeAttribute(name, js, null);
	}
	
	public static void writeScriptChain(StringBuilder js, String script)
	throws IOException
	{
		js.append("if(!(").append(script).append(")){return false;}");
	}
	
	public static void writeScriptBehaviors(FacesContext context, UIComponent
			component, StringBuilder js, String eventName, List<ClientBehavior>
			behaviors)
	throws IOException
	{
		ClientBehaviorContext behaviorContext =
				ClientBehaviorContext.createClientBehaviorContext(
				context, component, eventName, null,
				Collections.<Parameter>emptyList());
		// XXX Use jsf.util.chain with quote escaping instead
		for (ClientBehavior behavior : behaviors)
			writeScriptChain(js, behavior.getScript(behaviorContext));
	}
	
	public static void writeInlineJS(StringBuilder js, Object obj)
	{
		if (obj == null)
			js.append("null");
		else if (obj instanceof Map<?, ?>)
		{
			Map<?, ?> map = (Map<?, ?>) obj;
			js.append("{");
			String sep = "";
			for (Entry<?, ?> entry : map.entrySet())
			{
				js.append(sep);
				js.append("'").append(entry.getKey()).append("':");
				writeInlineJS(js, entry.getValue());
				sep = ",";
			}
			js.append("}");
		}
		else if (obj instanceof Collection<?>)
		{
			Collection<?> collection = (Collection<?>) obj;
			js.append("[");
			String sep = "";
			for (Object value : collection)
			{
				js.append(sep);
				writeInlineJS(js, value);
				sep = ",";
			}
			js.append("]");
		}
		else
			js.append("'").append(obj).append("'");
	}
	
	public static void writeJson(Writer out, Map<?, ?> map)
	{
		try (JsonGenerator generator = Json.createGenerator(out))
		{
			generator.writeStartObject();
			writeJson(generator, map);
			generator.writeEnd();
		}
	}
	
	public static void writeJson(JsonGenerator generator, Map<?, ?> map)
	{
		for (Entry<?, ?> entry : map.entrySet())
		{
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null)
				generator.writeNull(key.toString());
			else if (value instanceof Boolean)
				generator.write(key.toString(), (Boolean) value);
			else if (value instanceof Integer)
				generator.write(key.toString(), (Integer) value);
			else if (value instanceof Long)
				generator.write(key.toString(), (Long) value);
			else if (value instanceof Double)
				generator.write(key.toString(), (Double) value);
			else if (value instanceof BigInteger)
				generator.write(key.toString(), (BigInteger) value);
			else if (value instanceof BigDecimal)
				generator.write(key.toString(), (BigDecimal) value);
			if (value instanceof Map<?, ?>)
			{
				generator.writeStartObject(key.toString());
				writeJson(generator, (Map<?, ?>) value);
				generator.writeEnd();
			}
			else if (value instanceof Collection<?>)
			{
				generator.writeStartArray(key.toString());
				writeJson(generator, (Collection<?>) value);
				generator.writeEnd();
			}
			else
				generator.write(key.toString(), value.toString());
		}
	}
	
	public static void writeJson(JsonGenerator generator, Collection<?>
			collection)
	{
		for (Object value : collection)
		{
			if (value == null)
				generator.writeNull();
			else if (value instanceof Boolean)
				generator.write((Boolean) value);
			else if (value instanceof Integer)
				generator.write((Integer) value);
			else if (value instanceof Long)
				generator.write((Long) value);
			else if (value instanceof Double)
				generator.write((Double) value);
			else if (value instanceof BigInteger)
				generator.write((BigInteger) value);
			else if (value instanceof BigDecimal)
				generator.write((BigDecimal) value);
			if (value instanceof Map<?, ?>)
			{
				generator.writeStartObject();
				writeJson(generator, (Map<?, ?>) value);
				generator.writeEnd();
			}
			else if (value instanceof Collection<?>)
			{
				generator.writeStartArray();
				writeJson(generator, (Collection<?>) value);
				generator.writeEnd();
			}
			else
				generator.write(value.toString());
		}
	}
	
	private static String getStyleClassValue(Set<String> styleClasses)
	{
		if (styleClasses.isEmpty())
			return null;
		
		StringBuilder value = new StringBuilder();
		String sep = "";
		for (String styleClass : styleClasses)
		{
			value.append(sep).append(styleClass);
			sep = " ";
		}
		return value.toString();
	}
	
	private static boolean isBehaviorSource(FacesContext context,
			String behaviorSource, String clientId)
	{
		return true;
	}
	
	private static class SelectItemIterable
	implements Iterable<SelectItem>
	{
		private Iterator<SelectItem> it;
		
		public SelectItemIterable(FacesContext context, UIComponent component)
		{
			it = new SelectItemIterator(context, component);
		}
		
		@Override
		public Iterator<SelectItem> iterator()
		{
			return it;
		}
	}
	
	private static class SelectItemIterator
	implements Iterator<SelectItem>
	{
		private FacesContext context;
		private Iterator<UIComponent> childIt;
		private Iterator<SelectItem> nextIt;
		
		public SelectItemIterator(FacesContext context, UIComponent component)
		{
			this.context = context;
			childIt = component.getChildren().iterator();
			nextIt = nextIterator();
		}

		@Override
		public boolean hasNext()
		{
			return nextIt.hasNext();
		}

		@Override
		public SelectItem next()
		{
			SelectItem selectItem = nextIt.next();
			if (!nextIt.hasNext())
				nextIt = nextIterator();
			return selectItem;
		}

		@Override
		public void remove()
		{
			nextIt.remove();
		}
		
		private Iterator<SelectItem> nextIterator()
		{
			while (childIt.hasNext())
			{
				UIComponent child = childIt.next();
				if (child instanceof UISelectItem)
					return new SelectItemSingleIterator(context, child,
							getAttribute(child, "value", Object.class, null));
				if (child instanceof UISelectItems)
				{
					Object value = getAttribute(child, "value", Object.class,
							null);
					if (value != null)
					{
						if (value.getClass().isArray())
							return new SelectItemArrayIterator(context, child,
									value);
						if (value instanceof Collection<?>)
							return new SelectItemCollectionIterator(context,
									child, value);
					}
					return new SelectItemSingleIterator(context, child, value);
				}
			}
			return new SelectItemEmptyIterator();
		}
	}
	
	private static class SelectItemEmptyIterator
	implements Iterator<SelectItem>
	{
		public SelectItemEmptyIterator()
		{
		}
		
		@Override
		public boolean hasNext()
		{
			return false;
		}

		@Override
		public SelectItem next()
		{
			throw new NoSuchElementException();
		}

		@Override
		public void remove()
		{
		}
	}
	
	private static abstract class SelectItemNonEmptyIterator
	implements Iterator<SelectItem>
	{
		private FacesContext context;
		private UIComponent owner;
		
		public SelectItemNonEmptyIterator(FacesContext context,
				UIComponent owner)
		{
			this.context = context;
			this.owner = owner;
		}

		@Override
		public SelectItem next()
		{
			Object value = nextValue();
			if (value instanceof SelectItem)
				return (SelectItem) value;
			
			Object oldVarValue = null;
			String varName = value == null ? null
					: getAttribute(owner, "var", String.class, null);
			if (varName != null)
			{
				oldVarValue = getVariable(context, varName);
				putVariable(context, varName, value);
			}
			
			SelectItem selectItem = new SelectItem(
				getAttribute(owner, "itemValue", Object.class, null),
				getAttribute(owner, "itemLabel", String.class, null),
				getAttribute(owner, "itemDescription", String.class, null),
				getAttribute(owner, "itemDisabled", Boolean.class, false),
				getAttribute(owner, "escapeItem", Boolean.class, false),
				getAttribute(owner, "noSelectionOption", Boolean.class, false)
			);
			
			if (varName != null)
			{
				removeVariable(context, varName);
				if (oldVarValue != null)
					putVariable(context, varName, oldVarValue);
			}
			
			return selectItem;
		}
		
		protected abstract Object nextValue();
	}
	
	private static class SelectItemSingleIterator
	extends SelectItemNonEmptyIterator
	{
		private Object value;
		private boolean available;
		
		public SelectItemSingleIterator(FacesContext context, UIComponent owner,
				Object value)
		{
			super(context, owner);
			this.value = value;
			available = true;
		}
		
		@Override
		public boolean hasNext()
		{
			return available;
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		protected Object nextValue()
		{
			if (!available)
				throw new NoSuchElementException();
			available = false;
			return value;
		}
	}
	
	private static class SelectItemArrayIterator
	extends SelectItemNonEmptyIterator
	{
		private Object[] value;
		private int index;
		
		public SelectItemArrayIterator(FacesContext context, UIComponent owner,
				Object value)
		{
			super(context, owner);
			this.value = (Object[]) value;
			index = 0;
		}
		
		@Override
		public boolean hasNext()
		{
			return index < value.length;
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected Object nextValue()
		{
			return value[index++];
		}
	}
	
	private static class SelectItemCollectionIterator
	extends SelectItemNonEmptyIterator
	{
		private Iterator<?> it;
		
		public SelectItemCollectionIterator(FacesContext context,
				UIComponent owner, Object value)
		{
			super(context, owner);
			this.it = ((Collection<?>) value).iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return it.hasNext();
		}
		
		@Override
		public void remove()
		{
			it.remove();
		}

		@Override
		protected Object nextValue()
		{
			return it.next();
		}
	}
}

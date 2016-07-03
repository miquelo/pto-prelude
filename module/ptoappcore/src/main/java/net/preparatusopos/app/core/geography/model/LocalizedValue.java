package net.preparatusopos.app.core.geography.model;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class LocalizedValue
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Set<Entry> entries;
	
	public LocalizedValue()
	{
		entries = new HashSet<>();
	}
	
	public Set<Entry> getEntries()
	{
		return entries;
	}

	public void setEntries(Set<Entry> entries)
	{
		this.entries = entries;
	}

	public String getText(Locale locale)
	{
		for (String languageTag : compatibleLanguageTag(locale))
			for (Entry entry : entries)
				if (match(entry, languageTag))
					return entry.getText();
		return null;
	}
	
	public void put(String languageTag, String text)
	{
		entries.add(new Entry(languageTag, text));
	}
	
	private static Iterable<String> compatibleLanguageTag(Locale locale)
	{
		return new CompatibleLanguageTagIterable(locale);
	}
	
	private static boolean match(Entry entry, String languageTag)
	{
		if (languageTag == null)
			return entry.getLanguageTag() == null;
		return languageTag.equals(entry.getLanguageTag());
	}
	
	public static class Entry
	implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private String languageTag;
		private String text;
		
		@ConstructorProperties({
			"languageTag",
			"text"
		})
		public Entry(String languageTag, String text)
		{
			this.languageTag = languageTag;
			this.text = text;
		}

		public String getLanguageTag()
		{
			return languageTag;
		}
		
		public String getText()
		{
			return text;
		}
		
		@Override
		public int hashCode()
		{
			return languageTag == null ? 0 : languageTag.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof LocalizedValue)
			{
				Entry entry = (Entry) obj;
				return languageTag != null && languageTag.equals(
						entry.languageTag);
			}
			return false;
		}
	}
	
	private static class CompatibleLanguageTagIterable
	implements Iterable<String>
	{
		private Locale locale;
		
		public CompatibleLanguageTagIterable(Locale locale)
		{
			this.locale = locale;
		}

		@Override
		public Iterator<String> iterator()
		{
			return new CompatibleLanguageTagIterator(locale);
		}
	}
	
	private static class CompatibleLanguageTagIterator
	implements Iterator<String>
	{
		private int index;
		private String[] languageTags;
		
		public CompatibleLanguageTagIterator(Locale locale)
		{
			index = 0;
			languageTags = new String[] {
				locale.toLanguageTag(),
				locale.getLanguage(),
				locale.getCountry(),
				null
			};
		}

		@Override
		public boolean hasNext()
		{
			return index < languageTags.length;
		}

		@Override
		public String next()
		{
			return languageTags[index++];
		}

		@Override
		public void remove()
		{
		}
	}
}

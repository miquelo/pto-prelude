package net.preparatusopos.tools.file;

import java.io.Serializable;

import javax.activation.MimeType;

public class StoredFileSpecs
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private long size;
	private MimeType contentType;
	
	public StoredFileSpecs()
	{
		name = null;
		size = 0l;
		contentType = null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public MimeType getContentType()
	{
		return contentType;
	}

	public void setContentType(MimeType contentType)
	{
		this.contentType = contentType;
	}
	
	public static BuilderName builder()
	{
		return new BuilderImpl();
	}
	
	public static interface BuilderName
	{
		public BuilderSize setName(String name);
	}
	
	public static interface BuilderSize
	{
		public BuilderContentType setSize(long size);
	}
	
	public static interface BuilderContentType
	{
		public Builder setContentType(MimeType contentType);
	}
	
	public static interface Builder
	{
		public StoredFileSpecs build();
	}
	
	private static class BuilderImpl
	implements BuilderName, BuilderSize, BuilderContentType, Builder
	{
		private String name;
		private long size;
		private MimeType contentType;
		
		public BuilderImpl()
		{
			name = null;
			size = 0l;
			contentType = null;
		}
		
		@Override
		public BuilderSize setName(String name)
		{
			this.name = name;
			return this;
		}
		
		@Override
		public BuilderContentType setSize(long size)
		{
			this.size = size;
			return this;
		}

		@Override
		public Builder setContentType(MimeType contentType)
		{
			this.contentType = contentType;
			return this;
		}
		
		@Override
		public StoredFileSpecs build()
		{
			StoredFileSpecs specs = new StoredFileSpecs();
			specs.setName(name);
			specs.setSize(size);
			specs.setContentType(contentType);
			return specs;
		}
	}
}

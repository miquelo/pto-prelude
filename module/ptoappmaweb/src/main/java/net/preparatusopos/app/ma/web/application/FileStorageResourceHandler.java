package net.preparatusopos.app.ma.web.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.preparatusopos.app.domain.FileStorage;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.tools.file.FileStorageException;
import net.preparatusopos.tools.file.StoredFile;

public class FileStorageResourceHandler
extends ResourceHandlerWrapper
{
	public static String LIBRARY_NAME = "fileStorage";
	
	private ResourceHandler wrapped;
	
	@EJB(
		name="ejb/FileStorageBean"
	)
	private transient FileStorage fileStorage;
	
	public FileStorageResourceHandler(ResourceHandler wrapped)
	{
		this.wrapped = wrapped;
		fileStorage = null;
	}
	
	@Override
	public ResourceHandler getWrapped()
	{
		return wrapped;
	}
	
	@Override
	public Resource createResource(String resourceName, String libraryName)
	{
		try
		{
			if (LIBRARY_NAME.equals(libraryName))
			{
				Resource resource = createUserMediaResource(resourceName);
				if (resource != null)
				{
					resource.setLibraryName(libraryName);
					resource.setResourceName(resourceName);
				}
				return resource;
			}
			return super.createResource(resourceName, libraryName);
		}
		catch (MembershipException exception)
		{
			throw new IllegalStateException(exception);
		}
	}
	
	@Override
	public boolean libraryExists(String libraryName)
	{
		if (LIBRARY_NAME.equals(libraryName))
			return true;
		return super.libraryExists(libraryName);
	}
	
	@Override
	public boolean isResourceRequest(FacesContext context)
	{
		return super.isResourceRequest(context);
	}
	
	private Resource createUserMediaResource(String resourceName)
	throws MembershipException
	{
		return new FileStorageResource(fileStorage, resourceName);
	}
	
	private static class FileStorageResource
	extends Resource
	{
		// XXX Portability leak
		private static final String FACES_MAPPING = ".html";
		
		private static final int STATUS_MOVED_PERMANENTLY = 301;
		private static final String LAST_MODIFIED_HEADER_NAME = "Last-Modified";
		private static final String IF_MODIFIED_SINCE_HEADER_NAME =
				"If-Modified-Since";
		private static final String LOCATION_HEADER_NAME = "Location";
		
		private static final DateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z");
		
		private FileStorage fileStorage;
		private String name;
		private StoredFile file;
		
		public FileStorageResource(FileStorage fileStorage, String name)
		{
			this.fileStorage = fileStorage;
			this.name = name;
			file = null;
		}

		@Override
		public URL getURL()
		{
			return null;
		}
		
		@Override
		public String getRequestPath()
		{
			FacesContext context = FacesContext.getCurrentInstance();
			
			StringBuilder path = new StringBuilder();
			path.append(ResourceHandler.RESOURCE_IDENTIFIER);
			path.append("/").append(getResourceName()).append(FACES_MAPPING);
			path.append("?ln=").append(getLibraryName());
			
			ViewHandler viewHandler = context.getApplication().getViewHandler();
			return viewHandler.getResourceURL(context, path.toString());
		}
		
		@Override
		public boolean userAgentNeedsUpdate(FacesContext context)
		{
			try
			{
				ExternalContext externalContext = context.getExternalContext();
				Map<String, String> headerMap =
						externalContext.getRequestHeaderMap();
				
				String modifiedSinceHeader = headerMap.get(
						IF_MODIFIED_SINCE_HEADER_NAME);
				if (modifiedSinceHeader == null)
					return true;
				Date modifiedSince = parseDate(modifiedSinceHeader);
				
				Date creationDate = getFile().getCreationDate();
				return creationDate.after(modifiedSince);
			}
			catch (ParseException exception)
			{
				return true;
			}
		}
		
		@Override
		public Map<String, String> getResponseHeaders()
		{
			try
			{
				Map<String, String> headers = new HashMap<>();
				headers.put(LAST_MODIFIED_HEADER_NAME, getModifiedDate());
				redirect(headers, fileStorage.createPublicRef(getFile()));
				return headers;
			}
			catch (FileStorageException exception)
			{
				throw new IllegalStateException(exception);
			}
		}
		
		@Override
		public InputStream getInputStream()
		throws IOException
		{
			return new ByteArrayInputStream(new byte[0]);
		}
		
		private StoredFile getFile()
		{
			try
			{
				if (file == null)
					file = fileStorage.find(name);
				return file;
			}
			catch (FileStorageException exception)
			{
				throw new IllegalStateException(exception);
			}
		}
		
		private String getModifiedDate()
		{
			return HTTP_DATE_FORMAT.format(new Date());
		}
		
		private static void redirect(Map<String, String> headers, URI location)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.setResponseStatus(STATUS_MOVED_PERMANENTLY);
			headers.put(LOCATION_HEADER_NAME, location.toASCIIString());
		}
		
		private static Date parseDate(String date)
		throws ParseException
		{
			return HTTP_DATE_FORMAT.parse(date);
		}
	}
}

package net.preparatusopos.tools.file;

public class FileStorageException
extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public FileStorageException(String message)
	{
		super(message);
	}
	
	public FileStorageException(Throwable cause)
	{
		super(cause);
	}
	
	public FileStorageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

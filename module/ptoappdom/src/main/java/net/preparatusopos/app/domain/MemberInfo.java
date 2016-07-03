package net.preparatusopos.app.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

import net.preparatusopos.tools.file.StoredFile;

/**
 * Member info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class MemberInfo
implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private StoredFile photoFile;
	private String name;
	private String surname;
	
	public MemberInfo()
	{
		this(null);
	}
	
	@ConstructorProperties(
		"photoFile"
	)
	public MemberInfo(StoredFile photoFile)
	{
		this.photoFile = photoFile;
		name = null;
		surname = null;
	}

	public StoredFile getPhotoFile()
	{
		return photoFile;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}
}

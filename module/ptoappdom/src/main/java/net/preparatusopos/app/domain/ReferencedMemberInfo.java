package net.preparatusopos.app.domain;

import net.preparatusopos.tools.file.StoredFile;


/**
 * Referenced member info.
 * 
 * @author Miquel Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class ReferencedMemberInfo
extends MemberInfo
implements Referenced<String>
{
	private static final long serialVersionUID = 1L;
	
	private String ref;
	
	public ReferencedMemberInfo()
	{
		this(null, null);
	}
	
	public ReferencedMemberInfo(String ref, StoredFile photoFile)
	{
		super(photoFile);
		this.ref = ref;
	}

	@Override
	public String getRef()
	{
		return ref;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}
}

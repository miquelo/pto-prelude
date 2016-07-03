package net.preparatusopos.app.core.domain.model.meta;

import java.util.Date;

import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.ManagedFile;

@StaticMetamodel(ManagedFile.class)
public class ManagedFile_
{
	public static volatile SingularAttribute<ManagedFile, Long> fid;
	public static volatile SingularAttribute<ManagedFile, String> deviceName;
	public static volatile SingularAttribute<ManagedFile, String> name;
	public static volatile SingularAttribute<ManagedFile, Date> creationDate;
	public static volatile MapAttribute<ManagedFile, String, String> deviceData;
}

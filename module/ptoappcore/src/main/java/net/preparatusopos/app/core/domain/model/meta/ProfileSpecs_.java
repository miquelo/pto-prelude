package net.preparatusopos.app.core.domain.model.meta;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.preparatusopos.app.core.domain.model.Profile;
import net.preparatusopos.app.core.domain.model.ProfileSpecs;
import net.preparatusopos.app.domain.ProfileType;

@StaticMetamodel(Profile.class)
public class ProfileSpecs_
{
	public static volatile SingularAttribute<ProfileSpecs, ProfileType.Role>
			role;
	public static volatile SingularAttribute<ProfileSpecs, ProfileType.Country>
			country;
	public static volatile SingularAttribute<ProfileSpecs, ProfileType.Field>
			field;
	public static volatile SingularAttribute<ProfileSpecs,
			ProfileType.Specialization> specialization;
}
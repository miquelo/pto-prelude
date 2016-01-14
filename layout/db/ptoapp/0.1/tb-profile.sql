CREATE TABLE TB_PROFILE (
	CL_OWNERUID		BIGINT,
	CL_TYPE			INTEGER,
	CONSTRAINT CC_PROFILE_PK
	PRIMARY KEY (CL_OWNERUID, CL_TYPE),
	CONSTRAINT CC_PROFILE_OWNUID_FK
	FOREIGN KEY (CL_OWNERUID)
	REFERENCES TB_MEMBER(CL_UID)
);

REVOKE ALL
ON TABLE TB_PROFILE
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_PROFILE
TO PTOAPP;

COMMENT ON TABLE TB_PROFILE
IS 'Profile of a member';

COMMENT ON COLUMN TB_PROFILE.CL_OWNERUID
IS 'Owner member UID';

COMMENT ON COLUMN TB_PROFILE.CL_TYPE
IS 'Type of profile';

COMMENT ON CONSTRAINT CC_PROFILE_PK
ON TB_PROFILE
IS 'Primary key of profile';

COMMENT ON CONSTRAINT CC_PROFILE_OWNUID_FK
ON TB_PROFILE
IS 'Profile is only for an existing member';

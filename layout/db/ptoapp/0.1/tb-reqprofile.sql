CREATE TABLE TB_REQPROFILE (
	CL_TARGETUID	BIGINT,
	CL_PROFTYPE		INTEGER,
	CL_TYPE			INTEGER NOT NULL,
	CL_TOKEN		BYTEA NOT NULL,
	CL_CREATION		DATE NOT NULL,
	CL_MAIL			VARCHAR(1024),
	CONSTRAINT CC_REQPROFILE_PK
	PRIMARY KEY (CL_TARGETUID, CL_PROFTYPE),
	CONSTRAINT CC_REQPROFILE_TGTUID_FK
	FOREIGN KEY (CL_TARGETUID)
	REFERENCES TB_MEMBER(CL_UID)
);

REVOKE ALL
ON TABLE TB_REQPROFILE
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_REQPROFILE
TO PTOAPP;

COMMENT ON TABLE TB_REQPROFILE
IS 'Profile request';

COMMENT ON COLUMN TB_REQPROFILE.CL_TARGETUID
IS 'Target member UID';

COMMENT ON COLUMN TB_REQPROFILE.CL_PROFTYPE
IS 'Type of requested profile';

COMMENT ON COLUMN TB_REQPROFILE.CL_TYPE
IS 'Type of profile request';

COMMENT ON COLUMN TB_REQPROFILE.CL_TOKEN
IS 'Accreditation token';

COMMENT ON COLUMN TB_REQPROFILE.CL_CREATION
IS 'Creation date';

COMMENT ON COLUMN TB_REQPROFILE.CL_MAIL
IS 'Accreditation e-mail';

COMMENT ON CONSTRAINT CC_REQPROFILE_PK
ON TB_REQPROFILE
IS 'Primary key of profile request';

COMMENT ON CONSTRAINT CC_REQPROFILE_TGTUID_FK
ON TB_REQPROFILE
IS 'Profule request is only for an existing member';

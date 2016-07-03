CREATE TABLE TB_PROFTRAINLOC (
	CL_PROF			BIGINT,
	CL_INDEX		INTEGER,
	CL_LOCREF		BIGINT NOT NULL,
	CONSTRAINT CC_PROFTRAINLOC_PK
	PRIMARY KEY (CL_PROF, CL_INDEX),
	CONSTRAINT CC_PROFTRAINLOC_PROF_FK
	FOREIGN KEY (CL_PROF)
	REFERENCES TB_PROFILETRAIN(CL_REF)
);

REVOKE ALL
ON TABLE TB_PROFTRAINLOC
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_PROFTRAINLOC
TO PTOAPP;

COMMENT ON TABLE TB_PROFTRAINLOC
IS 'Location of trainer profile';

COMMENT ON COLUMN TB_PROFTRAINLOC.CL_PROF
IS 'Trainer profile reference';

COMMENT ON COLUMN TB_PROFTRAINLOC.CL_INDEX
IS 'Location index for the trainer profile';

COMMENT ON COLUMN TB_PROFTRAINLOC.CL_LOCREF
IS 'Location reference';

COMMENT ON CONSTRAINT CC_PROFTRAINLOC_PK
ON TB_PROFTRAINLOC
IS 'Primary key of trainer profile location';

COMMENT ON CONSTRAINT CC_PROFTRAINLOC_PROF_FK
ON TB_PROFTRAINLOC
IS 'Trainer profile is an existing profile';


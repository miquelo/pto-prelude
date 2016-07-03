CREATE SEQUENCE TB_REQPROF_REF_SEQ
START WITH 1;

REVOKE ALL
ON TABLE TB_REQPROF_REF_SEQ
FROM PUBLIC;

GRANT SELECT, UPDATE
ON TABLE TB_REQPROF_REF_SEQ
TO PTOAPP;

COMMENT ON SEQUENCE TB_REQPROF_REF_SEQ
IS 'Sequence for request for profile reference';

CREATE TABLE TB_REQPROF (
	CL_REF			BIGINT
	DEFAULT NEXTVAL('TB_REQPROF_REF_SEQ'),
	CL_ROLE			INTEGER NOT NULL,
	CL_COUNTRY		INTEGER NOT NULL,
	CL_FIELD		INTEGER NOT NULL,
	CL_SPECIAL		INTEGER,
	CONSTRAINT CC_REQPROF_PK
	PRIMARY KEY (CL_REF)
);

REVOKE ALL
ON TABLE TB_REQPROF
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_REQPROF
TO PTOAPP;

COMMENT ON TABLE TB_REQPROF
IS 'Request for profile';

COMMENT ON COLUMN TB_REQPROF.CL_REF
IS 'Request reference';

COMMENT ON COLUMN TB_REQPROF.CL_ROLE
IS 'Role of requested profile';

COMMENT ON COLUMN TB_REQPROF.CL_COUNTRY
IS 'Country of requested profile';

COMMENT ON COLUMN TB_REQPROF.CL_FIELD
IS 'Field of requested profile';

COMMENT ON COLUMN TB_REQPROF.CL_SPECIAL
IS 'Specialization of requested profile';

COMMENT ON CONSTRAINT CC_REQPROF_PK
ON TB_REQPROF
IS 'Primary key of request for profile';

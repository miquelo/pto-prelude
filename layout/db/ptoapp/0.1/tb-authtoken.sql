CREATE SEQUENCE TB_AUTHTOKEN_REF_SEQ
START WITH 1;

REVOKE ALL
ON TABLE TB_AUTHTOKEN_REF_SEQ
FROM PUBLIC;

GRANT SELECT, UPDATE
ON TABLE TB_AUTHTOKEN_REF_SEQ
TO PTOAPP;

COMMENT ON SEQUENCE TB_AUTHTOKEN_REF_SEQ
IS 'Sequence for authorization token reference';

CREATE TABLE TB_AUTHTOKEN (
	CL_REF			BIGINT
	DEFAULT NEXTVAL('TB_AUTHTOKEN_REF_SEQ'),
	CL_TYPE			INTEGER NOT NULL,
	CL_VALUE		BYTEA NOT NULL,
	CONSTRAINT CC_AUTHTOKEN_PK
	PRIMARY KEY (CL_REF)
);

REVOKE ALL
ON TABLE TB_AUTHTOKEN
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_AUTHTOKEN
TO PTOAPP;

COMMENT ON TABLE TB_AUTHTOKEN
IS 'Authorization token';

COMMENT ON COLUMN TB_AUTHTOKEN.CL_REF
IS 'Token reference';

COMMENT ON COLUMN TB_AUTHTOKEN.CL_TYPE
IS 'Token type';

COMMENT ON COLUMN TB_AUTHTOKEN.CL_VALUE
IS 'Token value';

COMMENT ON CONSTRAINT CC_AUTHTOKEN_PK
ON TB_AUTHTOKEN
IS 'Primary key of authorization token';


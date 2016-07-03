CREATE TABLE TB_REQCREDTOKEN (
	CL_REF			BIGINT,
	CL_OWNERUID		BIGINT,
	CL_REQREF		BIGINT,
	CONSTRAINT CC_REQCREDTOKEN_PK
	PRIMARY KEY (CL_REF),
	CONSTRAINT CC_REQCREDTOKEN_TOKEN_FK
	FOREIGN KEY (CL_REF)
	REFERENCES TB_AUTHTOKEN(CL_REF),
	CONSTRAINT CC_REQCREDTOKEN_OWNER_FK
	FOREIGN KEY (CL_OWNERUID)
	REFERENCES TB_MEMBER(CL_UID),
	CONSTRAINT CC_REQCREDTOKEN_REQ_FK
	FOREIGN KEY (CL_REQREF)
	REFERENCES TB_REQCRED(CL_REF)
);

REVOKE ALL
ON TABLE TB_REQCREDTOKEN
FROM PUBLIC;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE TB_REQCREDTOKEN
TO PTOAPP;

COMMENT ON TABLE TB_REQCREDTOKEN
IS 'Token of request for credential';

COMMENT ON COLUMN TB_REQCREDTOKEN.CL_REF
IS 'Token reference';

COMMENT ON COLUMN TB_REQCREDTOKEN.CL_OWNERUID
IS 'Owner user ID';

COMMENT ON COLUMN TB_REQCREDTOKEN.CL_REQREF
IS 'Request for credential reference';

COMMENT ON CONSTRAINT CC_REQCREDTOKEN_PK
ON TB_REQCREDTOKEN
IS 'Primary key of token of request for credential';

COMMENT ON CONSTRAINT CC_REQCREDTOKEN_TOKEN_FK
ON TB_REQCREDTOKEN
IS 'Token must be an existing token';

COMMENT ON CONSTRAINT CC_REQCREDTOKEN_OWNER_FK
ON TB_REQCREDTOKEN
IS 'Owner must be an existing member';

COMMENT ON CONSTRAINT CC_REQCREDTOKEN_REQ_FK
ON TB_REQCREDTOKEN
IS 'Request must be an existing request';


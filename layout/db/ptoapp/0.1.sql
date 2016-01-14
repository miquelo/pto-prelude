DROP SCHEMA IF EXISTS PTOAPP CASCADE;

CREATE SCHEMA PTOAPP;

GRANT USAGE
ON SCHEMA PTOAPP
TO PTOAPP;

COMMENT ON SCHEMA PTOAPP
IS 'PTO Application';

SET SEARCH_PATH TO PTOAPP;

exec '0.1/tb-member.sql'
exec '0.1/tb-membercred.sql'
exec '0.1/tb-profile.sql'
exec '0.1/tb-profilecand.sql'
exec '0.1/tb-profiletrain.sql'
exec '0.1/tb-proftrainloc.sql'
exec '0.1/tb-reqcredmail.sql'
exec '0.1/tb-reqprofile.sql'
exec '0.1/vw-membergroup.sql'


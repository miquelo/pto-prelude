#!/bin/bash

# Installing dependencies
# openjdk-7-jdk-headless is not available yet
apt-get install -y openjdk-7-jdk unzip

# Installing GlassFish Server
if [ ! -f /vagrant/repo/glassfish-4.1.1-b01-10_06_2015.zip ]; then
mkdir /vagrant/repo
echo 'Downloading GlassFish Server (Nightly build)...'
wget -P /vagrant/repo \
http://download.oracle.com/glassfish/4.1.1/nightly/\
glassfish-4.1.1-b01-10_06_2015.zip 2>> /dev/null
fi

echo 'Installing GlassFish Server...'
unzip /vagrant/repo/glassfish-4.1.1*zip -d /usr/lib
mkdir -p /var/glassfish/domains

# Creating user 'glassfish'
useradd \
-d /usr/lib/glassfish4 \
-s /bin/bash \
glassfish
echo "
PATH=\$HOME/bin:\$PATH
export PATH
" > /usr/lib/glassfish4/.bash_profile
chown -R glassfish:glassfish /usr/lib/glassfish4 /var/glassfish

sudo -i -u glassfish bash << EOF

# Installing PostgreSQL JDBC driver
wget -P /usr/lib/glassfish4/glassfish/lib \
https://jdbc.postgresql.org/download/postgresql-9.4.1207.jre6.jar 2>> \
/dev/null

# Deleting default domain
asadmin \
delete-domain \
domain1

# Creating password file
# TODO Use --savemasterpassword and --savelogin options instead ...
# ...  and remove passwords.txt. Then, use --passwordfile only on create-domain
# https://java.net/jira/browse/GLASSFISH-21266
echo "
AS_ADMIN_MASTERPASSWORD=12345678
AS_ADMIN_PASSWORD=12345678
" > passwords.txt

# Creating 'app' domain
asadmin \
--user admin \
--passwordfile passwords.txt \
create-domain \
--adminport 5838 \
--domaindir /var/glassfish/domains \
--usemasterpassword=true \
app

# Protecting password file
chmod 400 passwords.txt

# Starting domain 'app' for configutation
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
start-domain \
--domaindir /var/glassfish/domains \
app

# Enabling secure administration
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
enable-secure-admin

EOF

# Installing PTO realm dependencies
mv /tmp/ptorealm*.jar /var/glassfish/domains/app/lib

# Updating key store
openssl pkcs12 \
-export \
-nodes \
-name s1as-pto-app \
-passout pass:12345678 \
-certfile /vagrant/ssl/certs/pto-app.crt \
-in /vagrant/ssl/certs/pto-app.crt \
-inkey /vagrant/ssl/private/pto-app.key \
-out /tmp/app-keystore.p12

keytool \
-importkeystore \
-srckeystore /tmp/app-keystore.p12 \
-srcstoretype PKCS12 \
-srcalias s1as-pto-app \
-srcstorepass 12345678 \
-destkeystore /var/glassfish/domains/app/config/keystore.jks \
-deststoretype JKS \
-destalias s1as-pto-app \
-deststorepass 12345678

rm /tmp/app-keystore.p12

# Adding CA certificates
keytool \
-keystore /var/glassfish/domains/app/config/cacerts.jks \
-alias pto-app \
-storepass 12345678 \
-importcert \
-trustcacerts \
-noprompt \
-file /vagrant/ssl/certs/pto-app.crt

keytool \
-keystore /var/glassfish/domains/app/config/cacerts.jks \
-alias fnmt \
-storepass 12345678 \
-importcert \
-trustcacerts \
-noprompt \
-file /vagrant/ssl/certs/FNMTClase2CA.cer

# Creating 'app' index file
mv /tmp/app-index.html /var/glassfish/domains/app/docroot/index.html
chown glassfish:glassfish /var/glassfish/domains/app/docroot/index.html

# Creating 'ptoRealm' and 'ptoCertificateRealm' JAAS contexts
echo "
ptoRealm {
	net.preparatusopos.security.auth.login.PTOPasswordLoginModule required;
};

ptoCertificateRealm {
	net.preparatusopos.security.auth.login.PTOCertificateLoginModule required \
realm=pto;
};
" >> /var/glassfish/domains/app/config/login.conf

sudo -i -u glassfish bash << EOF

# Restarting domain 'app' for configutation
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
restart-domain \
--domaindir /var/glassfish/domains \
app

# Deleting HTTP and HTTPS listeners
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
delete-network-listener \
http-listener-1

# TODO Delete HTTPS listener
# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# delete-network-listener \
# http-listener-2

# Deleting HTTP and HTTPS protocols
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
delete-protocol \
http-listener-1

# TODO Delete HTTPS protocol
# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# delete-protocol \
# http-listener-2

# Creating JK connector 1
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
create-protocol \
--securityenabled=false \
jk-connector-1

asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
create-http \
--default-virtual-server server \
jk-connector-1

asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
create-ssl \
--type http-listener \
--certname s1as-pto-app \
jk-connector-1

asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
create-network-listener \
--protocol jk-connector-1 \
--listenerport 9054 \
--jkenabled=true \
jk-connector-1

# Creating JK connector 2
# TODO Create it if needed
# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# create-protocol \
# --securityenabled=true \
# jk-connector-2

# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# create-http \
# --default-virtual-server server \
# jk-connector-2

# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# create-ssl \
# --type http-listener \
# --certname s1as-pto-app \
# jk-connector-2

# asadmin \
# --port 5838 \
# --user admin \
# --passwordfile passwords.txt \
# create-network-listener \
# --protocol jk-connector-2 \
# --listenerport 9011 \
# --jkenabled=true \
# jk-connector-2

# Creating 'pto' realm
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
create-auth-realm \
--classname net.preparatusopos.security.auth.realm.PTOSQLRealm \
--property \
jaas-context=ptoRealm:\
dataSource=jdbc/PTORealmDataSource:\
dataBaseSchema=PTOAPP:\
dataBaseGroupViewName=VW_MEMBERGROUP:\
dataBaseCredTableName=TB_MEMBERCRED \
pto

# Specifying 'ptoCertificateRealm' as 'certificate' realm JAAS context
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
set configs.config.server-config.security-service.auth-realm.certificate.\
property.jaas-context=ptoCertificateRealm

# Stopping domain 'app' for configutation
asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
stop-domain \
--domaindir /var/glassfish/domains \
app

EOF

# Creating service for domain 'app'
mkdir /usr/lib/systemd/system
echo "
[Unit]
Description=GlassFish Server - Domain 'app'
After=syslog.target network.target

[Service]
Type=forking

ExecStart=/bin/su - glassfish -c 'asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
start-domain \
--domaindir /var/glassfish/domains \
--debug=true \
app'
ExecStop=/bin/su - glassfish -c 'asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
stop-domain \
--domaindir /var/glassfish/domains \
app'
ExecRestart=/bin/su - glassfish -c 'asadmin \
--port 5838 \
--user admin \
--passwordfile passwords.txt \
restart-domain \
--domaindir /var/glassfish/domains \
app'

[Install]
WantedBy=multi-user.target
" > /usr/lib/systemd/system/glassfish-app.service

# Enabling and starting service for domain 'app'
systemctl daemon-reload
systemctl enable glassfish-app 2>> /dev/null
systemctl start glassfish-app


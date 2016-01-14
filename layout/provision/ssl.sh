#!/bin/bash
# https://jamielinux.com/docs/openssl-certificate-authority/...
# ...create-the-root-pair.html

DIR_SSL="/vagrant/ssl"
DIR_SSL_CA=$DIR_SSL/ca
DIR_SSL_CERTS=$DIR_SSL/certs
DIR_SSL_PRIVATE=$DIR_SSL/private

if [ ! -d $DIR_SSL ]; then
mkdir -p $DIR_SSL_CA
mkdir -p $DIR_SSL_CERTS
mkdir -p $DIR_SSL_PRIVATE

# Getting CA certificates
wget -P $DIR_SSL_CERTS --no-check-certificate \
https://www.sede.fnmt.gob.es/documents/11614/116099/FNMTClase2CA.cer

# Generate Root CA Certificate
echo "Creating 'Root CA'..."

mkdir -p $DIR_SSL_CA/pto-root-ca/{certs,crl,csr,newcerts,private}
touch $DIR_SSL_CA/pto-root-ca/index.txt
touch $DIR_SSL_CA/pto-root-ca/index.txt.attr

echo "01
" > $DIR_SSL_CA/pto-root-ca/serial

echo "
[ ca ]
default_ca                     = CA_default

[ CA_default ]
dir                            = $DIR_SSL_CA/pto-root-ca
certs                          = \$dir/certs
crl_dir                        = \$dir/crl
new_certs_dir                  = \$dir/newcerts
database                       = \$dir/index.txt
serial                         = \$dir/serial
RANDFILE                       = \$dir/private/.rand

certificate                    = \$dir/certs/pto-root-ca.crt
private_key                    = \$dir/private/pto-root-ca.key

crlnumber                      = \$dir/crlnumber
crl                            = \$dir/crl/crl.pem
crl_extensions                 = crl_ext
default_crl_days               = 30

default_md                     = sha256

name_opt                       = ca_default
cert_opt                       = ca_default
default_days                   = 375
preserve                       = no
policy                         = policy_strict

[ policy_strict ]
countryName                    = match
stateOrProvinceName            = match
organizationName               = match
organizationalUnitName         = optional
commonName                     = supplied
emailAddress                   = optional

[ req ]
default_bits                   = 2048
distinguished_name             = req_distinguished_name
string_mask                    = utf8only

default_md                     = sha256

x509_extensions                = v3_ca

[ req_distinguished_name ]
countryName                    = ES
stateOrProvinceName            = Spain
localityName                   = Barcelona
0.organizationName             = PTO
organizationalUnitName         = Development
commonName                     = Common Name
emailAddress                   = development@preparatusopos.net

countryName_default            = ES
stateOrProvinceName_default    = Spain
localityName_default           = Bilbao
0.organizationName_default     = PTO
organizationalUnitName_default = Development
emailAddress_default           = development@preparatusopos.net

[ v3_ca ]
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid:always,issuer
basicConstraints               = critical, CA:true
keyUsage                       = critical, digitalSignature, cRLSign, \
keyCertSign

[ v3_intermediate_ca ]
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid:always,issuer
basicConstraints               = critical, CA:true, pathlen:0
keyUsage                       = critical, digitalSignature, cRLSign, \
keyCertSign

[ usr_cert ]
basicConstraints               = CA:FALSE
nsCertType                     = client, email
nsComment                      = \"OpenSSL Generated Client Certificate\"
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer
keyUsage                       = critical, nonRepudiation, digitalSignature, \
keyEncipherment
extendedKeyUsage               = clientAuth, emailProtection

[ server_cert ]
basicConstraints               = CA:FALSE
nsCertType                     = server
nsComment                      = \"OpenSSL Generated Server Certificate\"
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer:always
keyUsage                       = critical, digitalSignature, keyEncipherment
extendedKeyUsage               = serverAuth

[ crl_ext ]
authorityKeyIdentifier         = keyid:always

[ ocsp ]
basicConstraints               = CA:FALSE
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer
keyUsage                       = critical, digitalSignature
extendedKeyUsage               = critical, OCSPSigning
" > $DIR_SSL_CA/pto-root-ca.cnf

openssl genrsa -out $DIR_SSL_CA/pto-root-ca/private/pto-root-ca.key 4096

openssl req \
-config $DIR_SSL_CA/pto-root-ca.cnf \
-new \
-x509 \
-days 7300 \
-sha256 \
-subj "/CN=PTO Root CA/O=PTO/OU=Development/ST=Spain/L=Barcelona/C=ES" \
-extensions v3_ca \
-key $DIR_SSL_CA/pto-root-ca/private/pto-root-ca.key \
-out $DIR_SSL_CA/pto-root-ca/certs/pto-root-ca.crt

# Generate Signer Certificate
echo "Creating 'Signer'..."

mkdir -p $DIR_SSL_CA/pto-signer/{certs,crl,csr,newcerts,private}
touch $DIR_SSL_CA/pto-signer/index.txt
touch $DIR_SSL_CA/pto-signer/index.txt.attr

echo "01
" > $DIR_SSL_CA/pto-signer/serial

echo "
[ ca ]
default_ca                     = CA_default

[ CA_default ]
dir                            = $DIR_SSL_CA/pto-signer
certs                          = \$dir/certs
crl_dir                        = \$dir/crl
new_certs_dir                  = \$dir/newcerts
database                       = \$dir/index.txt
serial                         = \$dir/serial
RANDFILE                       = \$dir/private/.rand

certificate                    = \$dir/certs/pto-signer.crt
private_key                    = \$dir/private/pto-signer.key

crlnumber                      = $dir/crlnumber
crl                            = $dir/crl/crl.pem
crl_extensions                 = crl_ext
default_crl_days               = 30

default_md                     = sha256

name_opt                       = ca_default
cert_opt                       = ca_default
default_days                   = 375
preserve                       = no
policy                         = policy_loose

[ policy_loose ]
countryName                    = optional
stateOrProvinceName            = optional
localityName                   = optional
organizationName               = optional
organizationalUnitName         = optional
commonName                     = supplied
emailAddress                   = optional

[ req ]
default_bits                   = 2048
distinguished_name             = req_distinguished_name
string_mask                    = utf8only

default_md                     = sha256

x509_extensions                = v3_ca

[ req_distinguished_name ]
countryName                    = ES
stateOrProvinceName            = Spain
localityName                   = Barcelona
0.organizationName             = PTO
organizationalUnitName         = Development
commonName                     = Common Name
emailAddress                   = development@preparatusopos.net

countryName_default            = ES
stateOrProvinceName_default    = Spain
localityName_default           = Bilbao
0.organizationName_default     = PTO
organizationalUnitName_default = Development
emailAddress_default           = development@preparatusopos.net

[ v3_ca ]
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid:always,issuer
basicConstraints               = critical, CA:true
keyUsage                       = critical, digitalSignature, cRLSign, \
keyCertSign

[ v3_intermediate_ca ]
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid:always,issuer
basicConstraints               = critical, CA:true, pathlen:0
keyUsage                       = critical, digitalSignature, cRLSign, \
keyCertSign

[ usr_cert ]
basicConstraints               = CA:FALSE
nsCertType                     = client, email
nsComment                      = \"OpenSSL Generated Client Certificate\"
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer
keyUsage                       = critical, nonRepudiation, digitalSignature, \
keyEncipherment
extendedKeyUsage               = clientAuth, emailProtection

[ server_cert ]
basicConstraints               = CA:FALSE
nsCertType                     = server
nsComment                      = \"OpenSSL Generated Server Certificate\"
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer:always
keyUsage                       = critical, digitalSignature, keyEncipherment
extendedKeyUsage               = serverAuth

[ crl_ext ]
authorityKeyIdentifier         = keyid:always

[ ocsp ]
basicConstraints               = CA:FALSE
subjectKeyIdentifier           = hash
authorityKeyIdentifier         = keyid,issuer
keyUsage                       = critical, digitalSignature
extendedKeyUsage               = critical, OCSPSigning
" > $DIR_SSL_CA/pto-signer.cnf

openssl genrsa -out $DIR_SSL_CA/pto-signer/private/pto-signer.key 4096

openssl req \
-config $DIR_SSL_CA/pto-signer.cnf \
-new \
-sha256 \
-subj "/CN=PTO Signer/O=PTO/OU=Development/ST=Spain/L=Barcelona/C=ES" \
-key $DIR_SSL_CA/pto-signer/private/pto-signer.key \
-out $DIR_SSL_CA/pto-root-ca/csr/pto-signer.csr

openssl ca \
-config $DIR_SSL_CA/pto-root-ca.cnf \
-days 3650 \
-notext \
-batch \
-md sha256 \
-extensions v3_intermediate_ca \
-in $DIR_SSL_CA/pto-root-ca/csr/pto-signer.csr \
-out $DIR_SSL_CA/pto-signer/certs/pto-signer.crt

cat \
$DIR_SSL_CA/pto-signer/certs/pto-signer.crt \
$DIR_SSL_CA/pto-root-ca/certs/pto-root-ca.crt > \
$DIR_SSL_CA/pto-signer/certs/pto-signer-chain.crt

# Generate 'app' certificate and key

echo "Creating 'app' certificate and key..."

openssl genrsa -out $DIR_SSL_PRIVATE/pto-app.key 2048

openssl req \
-new \
-sha256 \
-subj "/CN=app.preparatusopos.net/O=PTO/OU=Development/ST=Spain/L=Barcelona\
/C=ES" \
-key $DIR_SSL_PRIVATE/pto-app.key \
-out $DIR_SSL_CA/pto-signer/csr/pto-app.csr

openssl ca \
-config $DIR_SSL_CA/pto-signer.cnf \
-days 375 \
-notext \
-batch \
-md sha256 \
-extensions server_cert \
-in $DIR_SSL_CA/pto-signer/csr/pto-app.csr \
-out $DIR_SSL_CERTS/pto-app.crt

# Exporting Signer certificate chain
cp \
$DIR_SSL_CA/pto-signer/certs/pto-signer-chain.crt \
$DIR_SSL_CERTS

# Cleanup
rm -r $DIR_SSL_CA

fi


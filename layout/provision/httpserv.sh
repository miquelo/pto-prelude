#!/bin/bash

# Installing dependencies
apt-get update
apt-get install -y apache2 libapache2-mod-jk

# Installing certificates and keys
cp /vagrant/ssl/certs/FNMTClase2CA.cer /etc/ssl/certs
cp /vagrant/ssl/certs/pto-signer-chain.crt /etc/ssl/certs
cp /vagrant/ssl/certs/pto-app.crt /etc/ssl/certs
cp /vagrant/ssl/private/pto-app.key /etc/ssl/private

# Removing default virtual servers
a2dissite *
rm /etc/apache2/sites-available/*

# Enabling required modules
a2enmod proxy
a2enmod proxy_ajp
a2enmod ssl

# Creating 'app' document root
rm -r /var/www/*
mkdir /var/www/app
chown -R www-data:www-data /var/www/app

# Creating 'app' virtual server
echo "<VirtualHost *:80>

    ServerName app.preparatusopos.net
    ServerAdmin admin@preparatusopos.net
    DocumentRoot /var/www/app
    
    LogLevel info ssl:warn
    ErrorLog \${APACHE_LOG_DIR}/error.log
    CustomLog \${APACHE_LOG_DIR}/access.log combined
    
    ProxyRequests Off
    ProxyPreserveHost On
    
    <Proxy *>
        Require all granted
    </Proxy>
    
    <Location />
    	Require all granted
    	ProxyPass ajp://localhost:9054/
    	ProxyPassReverse ajp://localhost:9054/
    </Location>
    
</VirtualHost>
" > /etc/apache2/sites-available/app.conf
a2ensite app

# Creating 'app-ssl' virtual server
echo "<VirtualHost *:443>

    ServerAdmin admin@preparatusopos.net
    ServerName app.preparatusopos.net
    DocumentRoot /var/www/app
    
    LogLevel info ssl:warn
    ErrorLog \${APACHE_LOG_DIR}/error.log
    CustomLog \${APACHE_LOG_DIR}/access.log combined
    
    SSLEngine on
    SSLCipherSuite \
ALL:!ADH:!EXPORT56:RC4+RSA:+HIGH:+MEDIUM:+LOW:+SSLv2:+EXP:+eNULL
    
    SSLCertificateFile /etc/ssl/certs/pto-app.crt
    SSLCertificateKeyFile /etc/ssl/private/pto-app.key
    SSLCACertificatePath /etc/ssl/certs/
    # SSLCertificateChainFile /etc/apache2/ssl.crt/server-ca.crt
    # SSLCACertificateFile /etc/apache2/ssl.crt/ca-bundle.crt
    # SSLCARevocationPath /etc/apache2/ssl.crl/
    # SSLCARevocationFile /etc/apache2/ssl.crl/ca-bundle.crl
    SSLVerifyClient optional
    SSLVerifyDepth 1
    SSLOptions +StdEnvVars +ExportCertData
    
    # <FilesMatch \"\.(cgi|shtml|phtml|php)$\">
    #     SSLOptions +StdEnvVars
    # </FilesMatch>
    # <Directory /usr/lib/cgi-bin>
    #     SSLOptions +StdEnvVars
    # </Directory>
    
    BrowserMatch \"MSIE [2-6]\" \
nokeepalive ssl-unclean-shutdown downgrade-1.0 force-response-1.0
    BrowserMatch \"MSIE [17-9]\" ssl-unclean-shutdown
    
    ProxyRequests Off
    ProxyPreserveHost On
    
    <Proxy *>
        Require all granted
    </Proxy>
    
    <Location />
    	Require all granted
    	ProxyPass ajp://localhost:9054/
    	ProxyPassReverse ajp://localhost:9054/
    </Location>
    
</VirtualHost>
" > /etc/apache2/sites-available/app-ssl.conf
a2ensite app-ssl

# Restarting server
service apache2 restart


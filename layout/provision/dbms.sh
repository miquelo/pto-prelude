#!/bin/bash

# Installing dependencies
apt-get install -y postgresql

# Configuring PostgreSQL
echo "
data_directory = '/var/lib/postgresql/9.4/main'
hba_file = '/etc/postgresql/9.4/main/pg_hba.conf'
ident_file = '/etc/postgresql/9.4/main/pg_ident.conf'

listen_addresses = '*'
port = 5432
max_connections = 100

unix_socket_directories = '/var/run/postgresql'

ssl = true
ssl_cert_file = '/etc/ssl/certs/ssl-cert-snakeoil.pem'
ssl_key_file = '/etc/ssl/private/ssl-cert-snakeoil.key'

shared_buffers = 128MB

max_prepared_transactions = 200

dynamic_shared_memory_type = posix

log_line_prefix = '%t [%p-%l] %q%u@%d '
log_timezone = 'GMT'

stats_temp_directory = '/var/run/postgresql/9.4-main.pg_stat_tmp'

datestyle = 'iso, mdy'
timezone = 'GMT'

lc_messages = 'en_US.UTF-8'
lc_monetary = 'en_US.UTF-8'
lc_numeric = 'en_US.UTF-8'
lc_time = 'en_US.UTF-8'

default_text_search_config = 'pg_catalog.english'
" > /etc/postgresql/9.4/main/postgresql.conf

# Creating 'admin' and 'pto-app' database users
sudo -i -u postgres bash << EOF

psql -c "CREATE USER ADMIN WITH PASSWORD '12345678';"
psql -c "CREATE USER PTOAPP WITH PASSWORD '12345678';"
psql -c "CREATE DATABASE PTODB OWNER ADMIN;"

EOF

# Configuraing access
echo "
host    template1    admin       all                 md5
host    ptodb        admin       all                 md5
host    ptodb        ptoapp      192.168.50.20/16    md5
" > /etc/postgresql/9.4/main/pg_hba.conf

# Restarting PostgreSQL service
service postgresql restart


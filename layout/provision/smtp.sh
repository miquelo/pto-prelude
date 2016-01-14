#!/bin/bash

debconf-set-selections <<< "postfix postfix/mailname string \
smtp.preparatusopos.net"
debconf-set-selections <<< "postfix postfix/main_mailer_type string \
'Prepara tus Opos'"
apt-get install -y postfix


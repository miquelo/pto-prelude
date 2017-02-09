#!/bin/bash

# add jessie backports
echo "
# jessie-backports
deb http://ftp.debian.org/debian jessie-backports main
" >> /etc/apt/sources.list

apt-get update
# install curl to fix broken wget while retrieving content from secured sites
apt-get -y install curl


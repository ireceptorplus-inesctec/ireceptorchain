#!/bin/bash

# Exit on first error
set -e

#########################################
#                 README                #
#########################################
#########################################
#                                       #
# EXEC:                                 #
# chmod +x install_docker.sh            #
# sudo ./install_docker.sh              #
#                                       #
#########################################

echo ""
echo "*********************************************"
echo "*** INSTALL HYPERLEDGER FABRIC REQUISITES ***";
echo "*********************************************"
echo ""

apt-get update

apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release


curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor --batch --yes -o /usr/share/keyrings/docker-archive-keyring.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null


apt-get update

apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose

apt-get install -y golang nodejs npm python3

echo ""
echo "*********************************************"
echo "***  INSTALL HYPERLEDGER FABRIC BINARIES  ***";
echo "*********************************************"
echo ""

# curl -sSL https://bit.ly/2ysbOFE | bash -s    
source fabric_configuration.sh

echo "*********************************************"
echo "***              INSTALLED                ***";
echo "*********************************************"


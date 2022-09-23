#!/bin/bash

IP1=192.168.1.186
IP2=192.168.1.56

echo ""
echo "download org2"
echo ""

BASEPATH=blockchain/mainNetwork/organizations/certs

rm -rf $BASEPATH

mkdir -p $BASEPATH

echo ""
echo "download certs from org2"
echo ""

scp -r dinis@$IP2:/home/dinis/docublock/$BASEPATH/org2.example.com $BASEPATH

echo ""
echo "upload certs to org1"
echo ""
scp -r $BASEPATH/org2.example.com dinis@$IP1:~/Desktop/docublock/$BASEPATH/org2.example.com
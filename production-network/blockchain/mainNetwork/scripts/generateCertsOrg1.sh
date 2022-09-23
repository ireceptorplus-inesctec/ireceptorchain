#!/bin/bash

. $PWD/networkConfig.sh


function createOrg1() {
  sed -i "s/\${IP1}/${IP1}/" \
      configtx/configtx.yaml

  # update host var
  hosts

  infoln "Generating certificates using Fabric CA"

  docker-compose -p $DOCKER_PROJECT_NAME -f compose/org1_ca.yaml up -d tlsca.org1.example.com
  sleep 1

  mkdir -p $PWD/organizations/certs/org1.example.com/client
  mkdir -p $PWD/organizations/certs/org1.example.com/msp

  infoln "Enrolling the CA admin"

  cp $PWD/organizations/certs/org1.example.com/server/tls-ca/crypto/ca-cert.pem $PWD/organizations/certs/org1.example.com/client/tls-ca-cert.pem

  export FABRIC_CA_CLIENT_HOME=$PWD/organizations/certs/org1.example.com/client
  export FABRIC_CA_CLIENT_TLS_CERTFILES=$PWD/organizations/certs/org1.example.com/client/tls-ca-cert.pem

  set -x
  fabric-ca-client enroll -u https://$TLS_CA_ADMIN:$TLS_CA_ADMIN_PASSWORD@0.0.0.0:7052 -M tls-ca/admin/msp --csr.hosts "${HOSTS}" --enrollment.profile tls
  { set +x; } 2>/dev/null


  set -x
  fabric-ca-client register -d --id.name $ORG_CA_ADMIN --id.secret $ORG_CA_ADMIN_PASSWORD --id.type admin -u https://0.0.0.0:7052 -M tls-ca/admin/msp
  { set +x; } 2>/dev/null

  set -x
  fabric-ca-client register -d --id.name $ORDERER_USER --id.secret $ORDERER_USER_PASSWORD --id.type orderer -u https://0.0.0.0:7052 -M tls-ca/admin/msp
  { set +x; } 2>/dev/null

  set -x
  fabric-ca-client register -d --id.name $OSN_ADMIN --id.secret $OSN_ADMIN_PASSWORD --id.type client -u https://0.0.0.0:7052 -M tls-ca/admin/msp
  { set +x; } 2>/dev/null

  # ADDED
  set -x
  fabric-ca-client register --id.name $PEER0_USER --id.secret $PEER0_USER_PASSWORD --id.type peer -u https://0.0.0.0:7052 -M tls-ca/admin/msp
  { set +x; } 2>/dev/null

  set -x
  fabric-ca-client enroll -u https://$ORG_CA_ADMIN:$ORG_CA_ADMIN_PASSWORD@0.0.0.0:7052 -M tls-ca/orgadmin/msp --csr.hosts "${HOSTS}" --enrollment.profile tls
  { set +x; } 2>/dev/null
  
  set -x
  fabric-ca-client enroll -u https://$ORDERER_USER:$ORDERER_USER_PASSWORD@0.0.0.0:7052 -M tls-ca/orderer/msp --csr.hosts "${HOSTS}" --enrollment.profile tls
  { set +x; } 2>/dev/null

  set -x
  fabric-ca-client enroll -u https://$OSN_ADMIN:$OSN_ADMIN_PASSWORD@0.0.0.0:7052 -M tls-ca/osnadmin/msp --csr.hosts "${HOSTS}" --enrollment.profile tls
  { set +x; } 2>/dev/null

  # ADDED
  set -x
  fabric-ca-client enroll -u https://$PEER0_USER:$PEER0_USER_PASSWORD@0.0.0.0:7052 -M tls-ca/peer0/msp --csr.hosts "${HOSTS}" --enrollment.profile tls
  { set +x; } 2>/dev/null


  mkdir -p $PWD/organizations/certs/org1.example.com/server/org-ca/tls
  mv $PWD/organizations/certs/org1.example.com/client/tls-ca/orgadmin/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/tls-ca/orgadmin/msp/keystore/key.pem
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/orgadmin/msp/signcerts/cert.pem $PWD/organizations/certs/org1.example.com/server/org-ca/tls/
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/orgadmin/msp/keystore/key.pem $PWD/organizations/certs/org1.example.com/server/org-ca/tls/
  mv $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/keystore/client-tls-key.pem
  mv $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/signcerts/cert.pem $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/signcerts/client-tls-cert.pem

  docker-compose -p $DOCKER_PROJECT_NAME -f compose/org1_ca.yaml up -d orgca.org1.example.com


  sleep 1

  infoln "before enrolling admin"

  set -x
  fabric-ca-client enroll -u https://$ORG_CA_ADMIN:$ORG_CA_ADMIN_PASSWORD@0.0.0.0:7053 -M org-ca/admin/msp --csr.hosts "${HOSTS}"
  { set +x; } 2>/dev/null

  infoln "after enrolling admin"

  set -x
  fabric-ca-client register -d --id.name $ORDERER_USER --id.secret $ORDERER_USER_PASSWORD --id.type orderer -u https://0.0.0.0:7053 -M org-ca/admin/msp
  { set +x; } 2>/dev/null

  # ADDED
  set -x
  fabric-ca-client register --id.name $PEER0_USER --id.secret $PEER0_USER_PASSWORD --id.type peer -u https://0.0.0.0:7053 -M org-ca/admin/msp
  { set +x; } 2>/dev/null
  
  infoln "before registering ORG_ADMIN"

  set -x
  fabric-ca-client register -d --id.name $ORG_ADMIN --id.secret $ORG_ADMIN_PASSWORD --id.type admin -u https://0.0.0.0:7053 -M org-ca/admin/msp
  { set +x; } 2>/dev/null

  infoln "after registering ORG_ADMIN"
  infoln "before enrolling ORG_ADMIN"

  set -x
  fabric-ca-client enroll -u https://$ORG_ADMIN:$ORG_ADMIN_PASSWORD@0.0.0.0:7053 -M org-ca/orgadmin/msp --csr.hosts "${HOSTS}"
  { set +x; } 2>/dev/null

  infoln "after enrolling ORG_ADMIN"

  set -x
  fabric-ca-client enroll -u https://$ORDERER_USER:$ORDERER_USER_PASSWORD@0.0.0.0:7053 -M org-ca/orderer/msp --csr.hosts "${HOSTS}"
  { set +x; } 2>/dev/null

  # ADDED
  set -x
  fabric-ca-client enroll -u https://$PEER0_USER:$PEER0_USER_PASSWORD@0.0.0.0:7053 -M org-ca/peer0/msp --csr.hosts "${HOSTS}"
  { set +x; } 2>/dev/null


  mkdir $PWD/organizations/certs/org1.example.com/msp/tlscacerts
  mkdir $PWD/organizations/certs/org1.example.com/msp/cacerts
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca-cert.pem $PWD/organizations/certs/org1.example.com/msp/tlscacerts/
  cp $PWD/organizations/certs/org1.example.com/client/org-ca/orgadmin/msp/cacerts/0-0-0-0-7053.pem $PWD/organizations/certs/org1.example.com/msp/cacerts/
  cp $PWD/organizations/config.yaml $PWD/organizations/certs/org1.example.com/msp

  mv $PWD/organizations/certs/org1.example.com/client/tls-ca/orderer/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/tls-ca/orderer/msp/keystore/key.pem
  mv $PWD/organizations/certs/org1.example.com/client/org-ca/orderer/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/org-ca/orderer/msp/keystore/key.pem

  mkdir -p $PWD/organizations/certs/org1.example.com/orderer/tls
  mkdir $PWD/organizations/certs/org1.example.com/orderer/adminclient
  mkdir $PWD/organizations/certs/org1.example.com/orderer/localMsp
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/orderer/msp/signcerts/cert.pem $PWD/organizations/certs/org1.example.com/orderer/tls/
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/orderer/msp/keystore/key.pem $PWD/organizations/certs/org1.example.com/orderer/tls/
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca-cert.pem $PWD/organizations/certs/org1.example.com/orderer/tls/tls-ca-cert.pem
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/signcerts/client-tls-cert.pem $PWD/organizations/certs/org1.example.com/orderer/adminclient/
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/osnadmin/msp/keystore/client-tls-key.pem $PWD/organizations/certs/org1.example.com/orderer/adminclient/
  cp -r $PWD/organizations/certs/org1.example.com/client/org-ca/orderer/msp/* $PWD/organizations/certs/org1.example.com/orderer/localMsp/
  cp $PWD/organizations/config.yaml $PWD/organizations/certs/org1.example.com/orderer/localMsp
  # TODO: next line
  cp $PWD/configtx/orderer.yaml $PWD/organizations/certs/org1.example.com/orderer/

  # PEER 0 ADDED
  mv $PWD/organizations/certs/org1.example.com/client/tls-ca/peer0/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/tls-ca/peer0/msp/keystore/key.pem
  mv $PWD/organizations/certs/org1.example.com/client/org-ca/peer0/msp/keystore/*_sk $PWD/organizations/certs/org1.example.com/client/org-ca/peer0/msp/keystore/key.pem

  mkdir -p $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls

  cp $PWD/organizations/certs/org1.example.com/client/tls-ca-cert.pem $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls/tls-ca-cert.pem

  mkdir $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/localMsp

  cp -r $PWD/organizations/certs/org1.example.com/client/org-ca/peer0/msp/* $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/localMsp

  cp $PWD/organizations/config.yaml $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/localMsp


  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/peer0/msp/signcerts/cert.pem $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls/
  cp $PWD/organizations/certs/org1.example.com/client/tls-ca/peer0/msp/keystore/key.pem $PWD/organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls/

}




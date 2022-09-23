#!/bin/bash

. scripts/utils.sh

export CORE_PEER_TLS_ENABLED=true

export ORG1_CA=${PWD}/organizations/certs/org1.example.com/client/tls-ca-cert.pem
export ORG2_CA=${PWD}/organizations/certs/org2.example.com/client/tls-ca-cert.pem

export PEER0_ORG1_CA=${PWD}/organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls/tls-ca-cert.pem
export PEER0_ORG2_CA=${PWD}/organizations/certs/org2.example.com/peers/peer0.org2.example.com/tls/tls-ca-cert.pem

export ORDERER_ADMIN_TLS_SIGN_CERT=$PWD/organizations/certs/org1.example.com/orderer/adminclient/client-tls-cert.pem
export ORDERER_ADMIN_TLS_PRIVATE_KEY=$PWD/organizations/certs/org1.example.com/orderer/adminclient/client-tls-key.pem

export ORDERER_TLS_CA=${PWD}/organizations/certs/org1.example.com/client/tls-ca-cert.pem

export ORDERER_ADDRESS=$IP1":7050"
# export ORDERER_ADDRESS="orderer.org1.example.com:7050"

export PEER0_ORG1_HOST=$IP1
export PEER0_ORG1_PORT=8054
export PEER0_ORG2_HOST=$IP2
export PEER0_ORG2_PORT=9054

export CORE_PEER_ID=cli

# Set environment variables for the peer org
setGlobals() {
  ORG=$1
  infoln "Using organization ${ORG}"
  if [ $ORG -eq 1 ]; then
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$ORG1_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/certs/org1.example.com/client/org-ca/orgadmin/msp/
    export CORE_PEER_ADDRESS="${PEER0_ORG1_HOST}:${PEER0_ORG1_PORT}"

  elif [ $ORG -eq 2 ]; then
    export CORE_PEER_LOCALMSPID="Org2MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$ORG2_CA
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/certs/org2.example.com/client/org-ca/orgadmin/msp/
    export CORE_PEER_ADDRESS="${PEER0_ORG2_HOST}:${PEER0_ORG2_PORT}"
  else
    errorln "ORG Unknown"
  fi
}

setGlobalsExternal() {
  ORG=$1
  setGlobals $ORG
  
  export ORDERER_ADDRESS=0.0.0.0:7050

  infoln "Using organization ${ORG}"
  if [ $ORG -eq 1 ]; then
    export CORE_PEER_ADDRESS=$IP1:8054

  elif [ $ORG -eq 2 ]; then
    export CORE_PEER_ADDRESS=$IP2:9054
  else
    errorln "ORG Unknown"
  fi
}

# Set environment variables for use in the CLI container
# setGlobalsCLI() {
#   setGlobals $1
  
#   USING_ORG=$1

#   if [ $USING_ORG -eq 1 ]; then
#     export CORE_PEER_ADDRESS="${PEER0_ORG1_HOST}${PEER0_ORG1_PORT}"
#   elif [ $USING_ORG -eq 2 ]; then
#     export CORE_PEER_ADDRESS="${PEER0_ORG2_HOST}${PEER0_ORG2_PORT}"
#   else
#     errorln "ORG Unknown"
#   fi
# }

# parsePeerConnectionParameters $@
# Helper function that sets the peer connection parameters for a chaincode
# operation
parsePeerConnectionParameters() {
  PEER_CONN_PARMS=()
  PEERS=""
  while [ "$#" -gt 0 ]; do
    setGlobals $1
    PEER="peer0.org$1.example.com"
    ## Set peer addresses
    if [ -z "$PEERS" ]
    then
	PEERS="$PEER"
    else
	PEERS="$PEERS $PEER"
    fi
    PEER_CONN_PARMS=("${PEER_CONN_PARMS[@]}" --peerAddresses $CORE_PEER_ADDRESS)
    ## Set path to TLS certificate
    CA=PEER0_ORG$1_CA
    TLSINFO=(--tlsRootCertFiles "${!CA}")
    PEER_CONN_PARMS=("${PEER_CONN_PARMS[@]}" "${TLSINFO[@]}")
    # shift by one to get to the next organization
    shift
  done
}

verifyResult() {
  if [ $1 -ne 0 ]; then
    fatalln "$2"
  fi
}

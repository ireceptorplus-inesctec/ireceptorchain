#!/bin/bash

source scripts/utils.sh


CHANNEL_NAME=${1:-"docublock"}

FABRIC_CFG_PATH=$PWD/configtx

# import utils
. scripts/envVar.sh

checkCC() {
  ORG=$1
  setGlobalsExternal $ORG

  set -x
  peer lifecycle chaincode querycommitted -C ${CHANNEL_NAME}
  res=$?
  { set +x; } 2>/dev/null

  CURRENT_VERSION=$(peer lifecycle chaincode querycommitted -C ${CHANNEL_NAME} | sed -n 's/.*Version: \([^,]*\),.*/\1/p')
  CURRENT_SEQUENCE=$(peer lifecycle chaincode querycommitted -C ${CHANNEL_NAME} | sed -n 's/.*Sequence: \([^,]*\),.*/\1/p')

  CC_VERSION=$(($CURRENT_VERSION + 1))
  CC_SEQUENCE=$(($CURRENT_SEQUENCE + 1))
  infoln "VERSION: Current=$CURRENT_VERSION; Next=$CC_VERSION"
  infoln "SEQUENCE: Current=$CURRENT_SEQUENCE; Next=$CC_SEQUENCE"
}

checkCC 1
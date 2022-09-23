#!/bin/bash



function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_ccp {
    local PP=$(one_line_pem $6)
    local CP=$(one_line_pem $7)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P0PORT}/$2/" \
        -e "s/\${POADDRESS}/$3/" \
        -e "s/\${CAPORT}/$4/" \
        -e "s/\${CAADDRESS}/$5/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        organizations/ccp-template.json
}

function yaml_ccp {
    local PP=$(one_line_pem $6)
    local CP=$(one_line_pem $7)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P0PORT}/$2/" \
        -e "s/\${POADDRESS}/$3/" \
        -e "s/\${CAPORT}/$4/" \
        -e "s/\${CAADDRESS}/$5/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        organizations/ccp-template.yaml | sed -e $'s/\\\\n/\\\n          /g'
}

function ccpOrg1() {
    ORG=1
    P0PORT=8054
    POADDRESS=$IP1
    CAPORT=7053
    CAADDRESS=$IP2
    PEERPEM=organizations/certs/org1.example.com/peers/peer0.org1.example.com/tls/tls-ca-cert.pem
    CAPEM=organizations/certs/org1.example.com/client/tls-ca-cert.pem

    echo "$(json_ccp $ORG $P0PORT $POADDRESS $CAPORT $CAADDRESS $PEERPEM $CAPEM)" > organizations/certs/org1.example.com/connection-org1.json
    echo "$(yaml_ccp $ORG $P0PORT $POADDRESS $CAPORT $CAADDRESS $PEERPEM $CAPEM)" > organizations/certs/org1.example.com/connection-org1.yaml
}

function ccpOrg2() {
    ORG=2
    P0PORT=9054
    POADDRESS=$IP2
    CAPORT=9053
    CAADDRESS=$IP2
    PEERPEM=organizations/certs/org2.example.com/peers/peer0.org2.example.com/tls/tls-ca-cert.pem
    CAPEM=organizations/certs/org2.example.com/client/tls-ca-cert.pem

    echo "$(json_ccp $ORG $P0PORT $POADDRESS $CAPORT $CAADDRESS $PEERPEM $CAPEM)" > organizations/certs/org2.example.com/connection-org2.json
    echo "$(yaml_ccp $ORG $P0PORT $POADDRESS $CAPORT $CAADDRESS $PEERPEM $CAPEM)" > organizations/certs/org2.example.com/connection-org2.yaml
}


ccpOrg$1
BASEPATH=blockchain
NETWORK_PATH=${BASEPATH}/mainNetwork

CHANNEL_NAME=ireceptorchain
CHAINCODE_NAME=ireceptorchain
CHAINCODE_VERSION=1
CHAINCODE_SEQUENCE=1
# go || java
LANGUAGE=java

export CHAINCODE_DIR=../../../chaincode
export CHAINCODE_PATH=../../../chaincode
export SWARM_NETWORK=ireceptorchain
export CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=${SWARM_NETWORK}
export PEER0_USER=peer0_user


export TLS_CA_ADMIN=admin
export TLS_CA_ADMIN_PASSWORD=adminpw
export ORG_CA_ADMIN=org_ca_admin
export ORG_CA_ADMIN_PASSWORD=org_ca_admin_pw
export ORDERER_USER=ordered_user
export ORDERER_USER_PASSWORD=ordered_user_pw
export OSN_ADMIN=osn_admin
export OSN_ADMIN_PASSWORD=osn_admin_pw
export PEER0_USER=peer0_user
export PEER0_USER_PASSWORD=peer0_user_pw
export ORG_ADMIN=org_admin
export ORG_ADMIN_PASSWORD=org_admin_pw

export ALLOWED_HOSTS=${IP1},${IP2},0.0.0.0,tlsca.org2.example.com,orgca.org2.example.com,tlsca.org1.example.com,orgca.org1.example.com,orderer.org1.example.com,peer0.org1.example.com,peer0.org2.example.com,org1.example.com,localhost

export DOCKER_PROJECT_NAME=docublock

export IP1=192.168.1.186
export IP2=192.168.1.56

export DB_USER=db_user
export DB_PASSWORD=db_password

# give access to all
fullAccess:
	chmod -R 777 .

# delete certs, artifacts, docker containers and volumes
clean: 
	rm -rf ${NETWORK_PATH}/channel-artifacts
	docker-compose -p ${DOCKER_PROJECT_NAME} -f blockchain/mainNetwork/compose/org1_ca.yaml down -v || true
	docker-compose -f blockchain/mainNetwork/compose/org1.yaml down -v || true
	docker-compose -p ${DOCKER_PROJECT_NAME} -f blockchain/mainNetwork/compose/org2_ca.yaml down -v || true
	docker-compose -f blockchain/mainNetwork/compose/org2.yaml down -v || true
	rm -rf server/${CHAINCODE_NAME}/${LANGUAGE}/wallet
 
	@echo -n "Delete certs folder? [y/N] " && read ans && if [ $${ans:-'N'} = 'y' ]; then rm -rf ${NETWORK_PATH}/organizations/certs/; fi


# install fabric images and binaries
install:
	@echo -n "Install Docker? [y/N] " && read ans && if [ $${ans:-'N'} = 'y' ]; then cd ${BASEPATH} && ./installation.sh; fi
	cd ${BASEPATH} && ./fabric_configuration.sh

# start Org1 peers and orderes
resetAndStartOrg1:
	cd ${NETWORK_PATH} && ./network.sh up -o 1

# start Org2 peers and orderes
resetAndStartOrg2:
	cd ${NETWORK_PATH} && ./network.sh up -o 2

fullStartOrg1:
	$(MAKE) clean
	$(MAKE) resetAndStartOrg1

fullStartOrg2:
	$(MAKE) clean
	$(MAKE) resetAndStartOrg2

startOrg1:
	docker-compose -p ${DOCKER_PROJECT_NAME} -f blockchain/mainNetwork/compose/org1_ca.yaml up -d
	docker-compose -f blockchain/mainNetwork/compose/org1.yaml up -d

startOrg2:
	docker-compose -p ${DOCKER_PROJECT_NAME} -f blockchain/mainNetwork/compose/org2_ca.yaml up -d
	docker-compose -f blockchain/mainNetwork/compose/org2.yaml up -d

# create network
createNetwork:
	cd ${NETWORK_PATH} && ./network.sh createChannel -c ${CHANNEL_NAME}

# deploy chaincode aka SmartContracts
deployCC:
	cd ${NETWORK_PATH} && ./network.sh deployCC -ccn ${CHAINCODE_NAME} -ccv ${CHAINCODE_VERSION} -ccs ${CHAINCODE_SEQUENCE} -cci initLedger -ccl ${LANGUAGE} -ccp ${CHAINCODE_PATH} -c ${CHANNEL_NAME}

checkCC:
	cd ${NETWORK_PATH} && ./network.sh checkCC -c ${CHANNEL_NAME}

npmi:
	cd server/ && npm i

# Just for the col4inlog chaincode 
createWalletUsers:
	cd server/${CHAINCODE_NAME}/ && rm -rf wallet && node certificateAuthorityActions.js admin && node certificateAuthorityActions.js user

serverUp:
	cd server && npm run start:prod

serverDown:
	cd server && npm run prod:stop

allUp:
	$(MAKE) createNetwork
	$(MAKE) deployCC
	$(MAKE) createWalletUsers
	$(MAKE) serverUp

BASEPATH=blockchain
NETWORK_PATH=${BASEPATH}/mainNetwork

CHANNEL_NAME=docublock
CHAINCODE_NAME=col4indlog
CHAINCODE_VERSION=1
CHAINCODE_SEQUENCE=1
# go || java
LANGUAGE=javascript

export CHAINCODE_DIR=../../
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
	docker rm -f $$(docker ps -a -q) || true
	docker volume rm $$(docker volume ls -q) || true
	rm -rf server/${CHAINCODE_NAME}/${LANGUAGE}/wallet
 
	@echo -n "Delete certs folder? [y/N] " && read ans && if [ $${ans:-'N'} = 'y' ]; then rm -rf ${NETWORK_PATH}/organizations/certs/; fi


# install fabric images and binaries
install:
	cd ${BASEPATH} && ./fabric_configuration.sh

# start Org1 peers and orderes
startOrg1:
	cd ${NETWORK_PATH} && ./network.sh up -o 1

# start Org2 peers and orderes
startOrg2:
	cd ${NETWORK_PATH} && ./network.sh up -o 2

fullStartOrg1:
	$(MAKE) clean
	$(MAKE) startOrg1

fullStartOrg2:
	$(MAKE) clean
	$(MAKE) startOrg2

# create network
createNetwork:
	cd ${NETWORK_PATH} && ./network.sh createChannel -c ${CHANNEL_NAME}

# deploy chaincode aka SmartContracts
deployCC:
	cd ${NETWORK_PATH} && ./network.sh deployCC -ccn ${CHAINCODE_NAME} -ccv ${CHAINCODE_VERSION} -ccs ${CHAINCODE_SEQUENCE} -cci initLedger -ccl ${LANGUAGE} -ccp ${CHAINCODE_DIR} -c ${CHANNEL_NAME}

checkCC:
	cd ${NETWORK_PATH} && ./network.sh checkCC -c ${CHANNEL_NAME}

npmi:
	cd server/ && npm i

# Just for the col4inlog chaincode 
createWalletUsers:
	cd server/${CHAINCODE_NAME}/ && rm -rf wallet && node certificateAuthorityActions.js admin && node certificateAuthorityActions.js user

serverUp:
	cd server && npm run prod

serverDown:
	cd server && npm run prod:stop

allUp:
	$(MAKE) createNetwork
	$(MAKE) deployCC
	$(MAKE) createWalletUsers
	$(MAKE) serverUp

# Col4indlog

This project allows to run the network in 2 machines, the blockchain Hyperledger Fabric, with the following structure:
  - Machine 1 (```org1.example.com```):
    - orderer
    - peer0
    - tlsca
    - orgca 
    - cli
  - Machine 2 (```org2.example.com```):
    - peer0
    - tlsca
    - orgca

### Sumary

Machine 1 generates certs to the ```org1.example.com``` (org1, orderer, peer0) with containers tlsca and orgca. Turns the containers peer0, orderer and cli up.

Machine 2 generates certs to the ```org2.example.com``` (org2, peer0) with containers tlsca and orgca. Turns the container peer0 up.

In both cases, the certs are generated on ```blockchain/mainNetwork/organizations/certs```.

Copy certs from Machine 2 to Machine 1, to the Machine 1 join Machine 2 on the channel and deploy the chaincode.

To create the network with both machines, we use the docker swarm.

PS: The server with API will run on Machine 1

# Project structure

- blockchain - contains the Hyperledger Fabric network 
	- chaincode - contains the SmartContract; ```./chaincode/$PROJECT_NAME/$LANGUAGE```
	- mainNetwork - scripts to generate the network
		- compose - docker compose files
		- congtx - 
		- organizations - cert files and templates
		- scripts - every script to manage the network
		- network.sh - main script to manage the network
		- networkConfig.sh - file that contains configurable network variables | #TODO: add IPS/Domain and org names
- server
	- col4indlog - communication with smart contract
	- simple API and client to interact with the blockchain - API described on API.md

# Steps and makefile

# Connection between machines

Machine 1: 

```
docker swarm init --advertise-addr <machine 1 address>
docker swarm join-token manager
```

Machine 2:

```
<output from join-token manager> --advertise-addr <machine 2 address>
```

Machine 1: 

```
docker network create --attachable --driver overlay col4indlog
docker network ls
```



The Makefile helps to create the entire network.

Change on makefile (if nedded):

- BASEPATH
- NETWORK_PATH
- CHANNEL_NAME
- CHAINCODE_NAME
- LANGUAGE
- CHAINCODE_VERSION
- CHAINCODE_SEQUENCE

### Step 1: install docker compose and hyperledger fabric binaries
```
make install
````

It creates a bin folder on the ```blockchain``` directory

### Step 2: Start the network
- Machine 1
```
make fullStartOrg1
```
- Machine 2
```
make fullStartOrg2
```

### Step 3: copy the cert files from machine 2 to 1

Machine 1 has the orderer and cli, will be the server too and is the one that can deploy channels and chaincodes

Copy /organizations/certs/org2.example.com

```
./copy_certs.sh
```

TODO: check what certs is need to copy because don't need all of them

PS: probably need to give permissions to copy
```
make fullAccess
```

## After this, all the next steps is to run on machine 1
### Step 4: createNetwork
```
make createNetwork
```

This create the channel, with the name defined on makefile (CHANNEL_NAME)

### Step 5: deployCC
```
make deployCC
```

This deploy the chaincode, with the name defined on makefile (CHAINCODE_NAME)


### Step 6: npm i
```
make npmi
```

This installs the node_models on server

### Step 7: createWalletUsers
```
make createWalletUsers
```

This creates a user to interact with the chaincode (wallet on ```server/col4indlog```)

### Step 8: serverUp
```
make serverUp
```
This starts the API

To stop the API:
```
make serverDown
```

### Step 9: update chaincode

To check the current chaincode version and sequence
```
make checkCC
```

Change on makefile CHAINCODE_VERSION and CHAINCODE_SEQUENCE to the next values.

To update the chaincode:
```
make deployCC
```

`If fails, increase version, but keep the sequence value`


# TODO

- update config file to easily change machine ips and organizations names


# Tips

- find in mainNetwork folder for ips (to change)
- ```make clean``` to delete all containers and network generated files

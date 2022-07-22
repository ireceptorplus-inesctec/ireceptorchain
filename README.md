# iReceptorChain

This repository contains the blockchain prototype that aims to provide data traceability to the iReceptorPlus project. It is composed of the source code that is run on the blockchain and a test network to be able to deploy that same chaincode on a testing environment.

## Test network

The test network is an environment provided by Hyperledger Fabric to run a blockchain prototype for testing purposes. In order to test a certain chaincode, it must be deployed to the test network.

### First time set-up
In order to set up your system to run Hyperledger Fabric, please follow the instructions available at [the official documentation](https://hyperledger-fabric.readthedocs.io/en/latest/install.html).

### Starting the test network

To start the test network, navigate to the `test-network` directory and run the script `restartNetwork.sh`.

This script contains 3 instructions:
```bash
./network.sh down
./network.sh up createChannel -ca -s couchdb
./network.sh deployCC -ccn ireceptorchain -ccv 1 -cci initLedger -ccl java -ccp ../
```

The first stops the network, in case it is already running, it is useful if you want to restart it. The second brings up the network, creating the peers, the orders, the CAs and generating the respective certificates. The third deploys the chaincode available at the `src` dir of this repository on the test network.

## Production Network

This section describes how to setup the production network. For more information and to understand the reasons behind some of the decisions made, please refer to the [Hyperledger Fabric official documentation page on Deploying a Production Network](https://hyperledger-fabric.readthedocs.io/en/release-2.2/deployment_guide_overview.html).

The production network test setup aims to reflect the usage of the ireceptorchain in the academic context. Therefore, the hyperledger fabric organizations correspond to universities that are part of the iReceptor Plus project.

The Hyperledger Fabric blockchain network setup is the following:
  - Machine 1 (```inesctec.example.com```):
    - orderer
    - peer0
    - tlsca
    - orgca 
    - cli
  - Machine 2 (```university-of-haifa.example.com```):
    - peer0
    - tlsca
    - orgca

### Sumary

Machine 1 generates certs to the ```inesctec.example.com``` (org1, orderer, peer0) with containers tlsca and orgca. Turns the containers peer0, orderer and cli up.

Machine 2 generates certs to the ```university-of-haifa.example.com``` (org2, peer0) with containers tlsca and orgca. Turns the container peer0 up.

In both cases, the certs are generated on ```blockchain/mainNetwork/organizations/certs```.

The certificates are then copied so that both machines have the certificate of each other.

To create the network with both machines, we use the docker swarm.

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
docker network create --attachable --driver overlay ireceptorchain
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

It creates a bin folder on the root of the project.

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
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
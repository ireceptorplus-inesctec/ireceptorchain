# iReceptorChain

This repository contains the blockchain prototype that aims to provide data traceability to the iReceptorPlus project.

# Development Environment Setup
To setup development environment using IntelliJ IDE (with support for running the unit tests on the IDE), do the following:
1. Clone this repository
2. On IntelliJ, select File > New >  Project from Existing Sources
3. Select the directory "fabric-samples/chaincode/fabcar/java" of the repository you cloned in step 1 and click OK
4. Select "Import project from external model" and then select "Gradle"
5. Select "Use graddle 'wrapper' task configuration" and leave default values for the remaining options and click "Finish"
6. After IntelliJ performs the gradle project setup, it should be ready to run
7. Go to the class "iReceptorPlus.Blockchain.iReceptorChain" and click the "Play" button to run the unit tests and IntelliJ will automatically add a "Running Configuration" for the unit tests. It is also possible to run only one test method or class, by clicking the corresponding play button

# Deploy the Hyperledger Fabric blockchain in development mode
To run the blockchain in development mode you need docker and docker-compose. After making sure you meet the requirements, do the following:
1. Clone this repository
2. Navigate to the folder "fabric-samples"
3. Run "curl -sSL https://bit.ly/2ysbOFE | bash -s"
4. Navigate to the folder "fabcar" (inside folder "fabric-samples")
5. Run the script "startFabric.sh"
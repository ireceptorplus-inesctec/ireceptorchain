/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Gateway, Wallets } = require('fabric-network');
const path = require('path');
const fs = require('fs');


async function main() {
    try {
        // load the network configuration
        const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
        const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = await Wallets.newFileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const identity = await wallet.get('appUser');
        if (!identity) {
            console.log('An identity for the user "appUser" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }

        //app user
        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');

        // Get the contract from the network.
        const contract = network.getContract('fabcar');


        //Admin
        // Create a new gateway for connecting to our peer node.
        const gatewayAdmin = new Gateway();
        await gatewayAdmin.connect(ccp, { wallet, identity: 'admin', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const networkAdmin = await gatewayAdmin.getNetwork('mychannel');

        // Get the contract from the network.
        const contractAdmin = networkAdmin.getContract('fabcar');

        

        // Evaluate the specified transaction.
        // queryCar transaction - requires 1 argument, ex: ('queryCar', 'CAR4')
        // queryAllCars transaction - requires no arguments, ex: ('queryAllCars')
        //const result = await contract.evaluateTransaction('createCar', 'CARRRR', 'make', 'model', 'color', 'owner');
        console.log("stuff1");
        //const result1 = await contract.submitTransaction('createMockTraceabilityData');
        //console.log(result1);
        console.log("stuff2");
        const createTraceabilityEntryResult = await contractAdmin.submitTransaction('createTraceabilityDataEntry',
        "uuid",
        'inputDatasetHashValue',
        'outputDatasetHashValue',
        'softwareId',
        'softwareVersion',
        'softwareBinaryExecutableHashValue',
        'softwareConfigParams',
        );       
        console.log("stuff3");
        //const result2 = await contract.submitTransaction('testVote');
        //console.log(result2.toString());
        console.log("stuff4");
        const createMockTraceabilityDataResult = await contract.evaluateTransaction('createMockTraceabilityData',
        'uuid',
        );
        console.log(`createMockTraceabilityData: ${createMockTraceabilityDataResult.toString()}`);

        const registerYesVoteForTraceabilityEntryInVotingRoundResult = await contract.submitTransaction('registerYesVoteForTraceabilityEntryInVotingRound',
        'uuid',
        );
        const registerYesVoteForTraceabilityEntryInVotingRoundResult2 = await contract.submitTransaction('registerYesVoteForTraceabilityEntryInVotingRound',
        'uuid',
        );
        const registerYesVoteForTraceabilityEntryInVotingRoundResult3 = await contract.submitTransaction('registerYesVoteForTraceabilityEntryInVotingRound',
        'uuid',
        );
        const registerYesVoteForTraceabilityEntryInVotingRoundResult4 = await contract.submitTransaction('registerYesVoteForTraceabilityEntryInVotingRound',
        'uuid',
        );
        const registerNoVoteForTraceabilityEntryInVotingRoundResult = await contract.submitTransaction('registerNoVoteForTraceabilityEntryInVotingRound',
        'uuid',
        );
        getAllAwaitingValidationTraceabilityDataEntriesResult = await contract.evaluateTransaction('getAllAwaitingValidationTraceabilityDataEntries',
        'uuid',
        );
        const getAllValidatedTraceabilityDataEntriesResult = await contract.evaluateTransaction('getAllValidatedTraceabilityDataEntries',
        'uuid',
        );
        const testNotFinalAttribsResult = await contract.evaluateTransaction('testNotFinalAttribs',
        'uuid',
        );


        //console.log(`createTraceabilityEntryResult: ${createTraceabilityEntryResult.toString()}`);
        console.log(`createTraceabilityEntryResult: ${createTraceabilityEntryResult.toString()}`);
        console.log(`registerYesVoteForTraceabilityEntryInVotingRoundResult: ${registerYesVoteForTraceabilityEntryInVotingRoundResult.toString()}`);
        console.log(`registerYesVoteForTraceabilityEntryInVotingRoundResult2: ${registerYesVoteForTraceabilityEntryInVotingRoundResult2.toString()}`);
        console.log(`registerYesVoteForTraceabilityEntryInVotingRoundResult3: ${registerYesVoteForTraceabilityEntryInVotingRoundResult3.toString()}`);
        console.log(`registerYesVoteForTraceabilityEntryInVotingRoundResult4: ${registerYesVoteForTraceabilityEntryInVotingRoundResult4.toString()}`);
        console.log(`registerNoVoteForTraceabilityEntryInVotingRoundResult: ${registerNoVoteForTraceabilityEntryInVotingRoundResult.toString()}`);
        console.log(`getAllAwaitingValidationTraceabilityDataEntriesResult: ${getAllAwaitingValidationTraceabilityDataEntriesResult.toString()}`);
        console.log(`getAllValidatedTraceabilityDataEntriesResult: ${getAllValidatedTraceabilityDataEntriesResult.toString()}`);
        console.log(`testNotFinalAttribsResult: ${testNotFinalAttribsResult.toString()}`);
    } catch (error) {
        console.error(`Failed to evaluate transaction: ${error}`);
        process.exit(1);
    }
}

main();

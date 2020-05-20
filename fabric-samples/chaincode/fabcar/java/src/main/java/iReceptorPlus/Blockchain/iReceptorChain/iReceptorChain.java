/*
 * SPDX-License-Identifier: Apache-2.0
 */

package iReceptorPlus.Blockchain.iReceptorChain;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataAwatingValidationReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataValidatedReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataAwatingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.*;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Returns.VotingStateMachineReturn;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.TraceabilityInfoStateMachine;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

/**
 * Java implementation of the Fabric Car Contract described in the Writing Your
 * First Application tutorial
 */
@Contract(
        name = "iReceptorChain",
        info = @Info(
                title = "iReceptorChain contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))
@Default
public final class iReceptorChain implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction()
    public void initLedger(final Context ctx) {
        createEntity(ctx, "x509::CN=org2admin, OU=admin, O=Hyperledger, ST=North Carolina, C=US::CN=fabric-ca-server, OU=Fabric, O=Hyperledger, ST=North Carolina, C=US");

        createTraceabilityDataEntry(ctx,
                "uuid",
                "inputDatasetHashValue",
                "outputDatasetHashValue",
                "softwareId",
                "softwareVersion",
                "softwareBinaryExecutableHashValue",
                "softwareConfigParams"
        );
    }

    /**
     * Creates a new entity on the ledger.
     *
     * @param ctx the transaction context
     * @param clientIdentity An instance of class ClientIdentity containing information of the client's identity.
     * @return the EntityData entry just created on the blockchain.
     */
    @Transaction()
    public EntityDataInfo createEntity(final Context ctx, final ClientIdentity clientIdentity) {

        return createEntity(ctx, clientIdentity.getId());
    }

    /**
     * Returns the entity that has called the Transaction, based on the context.
     * @param ctx The context.
     * @return An instance of EntityID representing the entity that has called a given transaction.
     */
    private EntityID getEntityIdFromContext(Context ctx)
    {
        return new EntityID(ctx.getClientIdentity().getId());
    }

    /**
     * Creates a new entity on the ledger.
     *
     * @param ctx the transaction context
     * @param entityID A string representing the id of the entity.
     * @return the EntityData entry just created on the blockchain.
     */
    @Transaction()
    public EntityDataInfo createEntity(final Context ctx, final String entityID) {
        logDebugMsg("createTraceabilityDataEntry");

        ChaincodeStub stub = ctx.getStub();

        EntityData entityData = new EntityData(entityID);
        EntityDataInfo entityDataInfo = new EntityDataInfo(entityData.getId(), entityData);
        EntityDataRepositoryAPI api = new EntityDataRepositoryAPI(ctx);
        try
        {
            api.create(entityDataInfo);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new ChaincodeException("Entity with the same id already exists on the blockchain database");
        }

        return entityDataInfo;
    }

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param traceabilityData An instance of class TraceabilityDataAwatingValidation containing the traceability data to be inserted in the blockchain.
     */
    @Transaction()
    public TraceabilityDataAwatingValidationReturnType createTraceabilityDataEntry(final Context ctx, final String newUUID, final TraceabilityDataAwatingValidation traceabilityData)
    {
        ProcessingDetails processingDetails = traceabilityData.getProcessingDetails();
        return createTraceabilityDataEntry(ctx, newUUID, traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                processingDetails.getSoftwareConfigParams());
    }

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     *
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param inputDatasetHashValue the hash value of the input dataset used to perform the data transformation.
     * @param outputDatasetHashValue the hash value of the output dataset used to perform the data transformation.
     * @param softwareId an unique identifier of the software used to perform the data transformation.
     * @param softwareVersion the version of the software used to perform the data transformation.
     * @param softwareBinaryExecutableHashValue the hash value of the binary executable used to perform the data transformation.
     * @param softwareConfigParams the configuration parameters of the software used to perform the data transformation.
     * @return the traceability entry and the UUID used to reference the traceability information.
     */
    @Transaction()
    public TraceabilityDataAwatingValidationReturnType createTraceabilityDataEntry(final Context ctx, final String newUUID, final String inputDatasetHashValue,
                                                            final String outputDatasetHashValue, final String softwareId,
                                                            final String softwareVersion, final String softwareBinaryExecutableHashValue,
                                                            final String softwareConfigParams) {
        logDebugMsg("createTraceabilityDataEntry");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityDataAwatingValidation traceabilityData = new TraceabilityDataAwatingValidation(inputDatasetHashValue, outputDatasetHashValue,
                new ProcessingDetails(softwareId, softwareVersion, softwareBinaryExecutableHashValue, softwareConfigParams), new EntityID(ctx.getClientIdentity().getId()));

        TraceabilityDataInfo dataInfo = new TraceabilityDataInfo(newUUID, traceabilityData);

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);

        try
        {
            TraceabilityInfoStateMachine stateMachine = new TraceabilityInfoStateMachine(dataInfo, api);
            stateMachine.initVotingRound(new EntityID(ctx.getClientIdentity().getId()));
        } catch (UnsupportedTypeOfTraceabilityInfo unsupportedTypeOfTraceabilityInfo)
        {
            throw new ChaincodeException(unsupportedTypeOfTraceabilityInfo.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry entityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry.getMessage());
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new ChaincodeException(givenIdIsAlreadyAssignedToAnotherObject.getMessage());
        }

        TraceabilityDataAwatingValidationReturnType traceabilityDataInfo = new TraceabilityDataAwatingValidationReturnType(newUUID, traceabilityData);

        logDebugMsg("createTraceabilityDataEntry END");

        return traceabilityDataInfo;
    }

    /**
     * Allows a node to vote yes for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote yes for.
     * @return a string identifying the success of the operation.
     */
    @Transaction()
    public String registerYesVoteForTraceabilityEntryInVotingRound(final Context ctx, final String uuid) {

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound");

        TraceabilityInfoStateMachine traceabilityInfoStateMachine = getTraceabilityDataFromDBAndBuildVotingStateMachine(ctx, uuid);
        VotingStateMachineReturn votingStateMachineReturn;
        try
        {
            votingStateMachineReturn = traceabilityInfoStateMachine.voteYesForTheVeracityOfTraceabilityInfo(getEntityIdFromContext(ctx));
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToPlaceVote entityDoesNotHaveEnoughReputationToPlaceVote)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToPlaceVote.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        }

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound END");

        return votingStateMachineReturn.getMessage();
    }

    /**
     * Allows a node to vote no for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote no for.
     * @return a string identifying the success of the operation.
     */
    @Transaction()
    public String registerNoVoteForTraceabilityEntryInVotingRound(final Context ctx, final String uuid) {
        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound");

        TraceabilityInfoStateMachine traceabilityInfoStateMachine = getTraceabilityDataFromDBAndBuildVotingStateMachine(ctx, uuid);
        VotingStateMachineReturn votingStateMachineReturn;
        try
        {
            votingStateMachineReturn = traceabilityInfoStateMachine.voteNoForTheVeracityOfTraceabilityInfo(getEntityIdFromContext(ctx));
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToPlaceVote entityDoesNotHaveEnoughReputationToPlaceVote)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToPlaceVote.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        }


        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound END");

        return votingStateMachineReturn.getMessage();
    }

    /**
     * Auxiliary method for the voting methods.
     * Validates the info on the database and builds the state machine that implements the voting rounds logic.
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to support voting for.
     * @return a string identifying the success of the operation.
     */
    private TraceabilityInfoStateMachine getTraceabilityDataFromDBAndBuildVotingStateMachine(Context ctx, String uuid)
    {
        logDebugMsg("getTraceabilityDataFromDBAndBuildVotingStateMachine");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityData traceabilityData;

        TraceabilityDataAwatingValidationRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        try
        {
            traceabilityData = (TraceabilityData) api.read(uuid);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ChaincodeException(objectWithGivenKeyNotFoundOnBlockchainDB.getMessage());
        }

        String voterID = ctx.getClientIdentity().getId();
        String creatorID = traceabilityData.getCreatorID().getId();
        if (voterID.equals(creatorID))
            throw new ChaincodeException("Creator of traceability data cannot vote for it.");

        TraceabilityDataInfo traceabilityDataInfo = new TraceabilityDataInfo(uuid, traceabilityData);
        TraceabilityInfoStateMachine traceabilityInfoStateMachine;
        try
        {
            traceabilityInfoStateMachine = new TraceabilityInfoStateMachine(traceabilityDataInfo, api);
        } catch (UnsupportedTypeOfTraceabilityInfo unsupportedTypeOfTraceabilityInfo)
        {
            throw new ChaincodeException("Voting on information with this type is not supported");
        }
        logDebugMsg("getTraceabilityDataFromDBAndBuildVotingStateMachine END");

        return traceabilityInfoStateMachine;
    }

    /**
     * Retrieves all traceability data that is in state awating validation.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */
    @Transaction()
    public TraceabilityDataAwatingValidationReturnType[] getAllAwaitingValidationTraceabilityDataEntries(final Context ctx) {
        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries");
        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataAwatingValidationReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            logDebugMsg("uuid: " + traceabilityDataInfo.getUUID());
            TraceabilityDataAwatingValidationReturnType dataReturnType = new TraceabilityDataAwatingValidationReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataAwatingValidation) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
        }

        TraceabilityDataAwatingValidationReturnType[] response = resultsToReturn.toArray(new TraceabilityDataAwatingValidationReturnType[resultsToReturn.size()]);
        System.err.println("*************** response: " + genson.serialize(response));

        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries END");

        return response;
    }

    /**
     * Retrieves all traceability data that is in state validated.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */
    @Transaction()
    public TraceabilityDataValidatedReturnType[] getAllValidatedTraceabilityDataEntries(final Context ctx) {
        logDebugMsg("getAllValidatedTraceabilityDataEntries");

        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataValidatedRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            TraceabilityDataValidatedReturnType dataReturnType = new TraceabilityDataValidatedReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataValidated) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
    }

        TraceabilityDataValidatedReturnType[] response = resultsToReturn.toArray(new TraceabilityDataValidatedReturnType[resultsToReturn.size()]);
        logDebugMsg("getAllValidatedTraceabilityDataEntries END");

        return response;
    }

    private void logDebugMsg(String msg)
    {
        System.err.println("************************** " + msg + " **************************");
    }
}

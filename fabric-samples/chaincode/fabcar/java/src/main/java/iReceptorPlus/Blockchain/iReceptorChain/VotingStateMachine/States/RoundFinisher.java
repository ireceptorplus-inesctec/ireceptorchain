package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataAwatingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.EntityReputationManager;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;

/**
 * This is a delegate class used to finish the voting round.
 * This means to perform the necessary changes to approve of reject a traceability data entry, based on the method called.
 * The class is called by the state classes to help performing the round closing procedures.
 */
public class RoundFinisher
{
    /**
     * The TraceabilityData instance where the class will operate.
     */
    TraceabilityDataInfo traceabilityDataInfo;

    /**
     * An instance of class HyperledgerFabricBlockhainRepositoryAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (VotingStateMachine).
     */
    HyperledgerFabricBlockhainRepositoryAPI api;

    void approveTraceabilityDataEntry(TraceabilityData traceabilityData, EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity
    {
        removeTraceabilityDataFromDB();

        api = new TraceabilityDataValidatedRepositoryAPI(api);
        TraceabilityData newTraceabilityData = new TraceabilityDataValidated(traceabilityData.getInputDatasetHashValue(),
                traceabilityData.getOutputDatasetHashValue(), traceabilityData.getProcessingDetails(),
                ((TraceabilityDataAwatingValidation) traceabilityData).getApprovers());
        TraceabilityDataInfo newTraceabilityDataInfo = new TraceabilityDataInfo(traceabilityDataInfo.getUUID(), newTraceabilityData);
        try
        {
            api.create(newTraceabilityDataInfo);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }

        api = new EntityDataRepositoryAPI(api);
        long rewardAmount = ChaincodeConfigs.reputationRewardForCreatingTruthfulTraceabiltiyDataEntry.get();
        EntityReputationManager entityReputationManager = new EntityReputationManager(api);
        try
        {
            entityReputationManager.rewardEntity(voterID, rewardAmount);
        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new InternalError("Internal error occurred on processing by the state machine: got not enough reputation error on trying to reward entity. This means that a number was going to be made negative when adding a positive factor to it. Something went really wrong...");
        }
    }

    void rejectTraceabilityDataEntry(TraceabilityData data, EntityID voterID) throws IncosistentInfoFoundOnDB
    {
        removeTraceabilityDataFromDB();
    }

    private void removeTraceabilityDataFromDB() throws IncosistentInfoFoundOnDB
    {
        api = new TraceabilityDataAwatingValidationRepositoryAPI(api);
        try
        {
            api.remove(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("object not found when trying to delete the info on the DB in order to switch state");
        }
    }
}
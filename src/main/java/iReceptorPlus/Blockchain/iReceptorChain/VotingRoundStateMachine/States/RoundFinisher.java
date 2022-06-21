package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataAwaitingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.EntityReputationManager;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.ReferenceToNonexistentEntity;

import java.util.ArrayList;

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
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (VotingRoundStateMachine).
     */
    HyperledgerFabricBlockhainRepositoryAPI api;

    /**
     * Constructor for this class. Receives the traceability data of which to finish the voting round and the repository where that traceability data is stored.
     * @param traceabilityDataInfo An instance of class TraceabilityDataInfo representing the traceability data of which to finish the voting round.
     * @param api An instance of class HyperledgerFabricBlockhainRepositoryAPI that implements the database logic to access the "table" where that traceability data is stored.
     */
    public RoundFinisher(TraceabilityDataInfo traceabilityDataInfo, HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.traceabilityDataInfo = traceabilityDataInfo;
        this.api = api;
    }

    /**
     * This method performs the procedures necessary for the termination of the voting round when the network decides by consensus to approve the traceability data.
     * @param traceabilityData An instance of class TraceabilityData representing the traceability data of which the voting round will be terminated and that will be registered as valid in the blockchain.
     * @throws IncosistentInfoFoundOnDB Exception thrown in case there is an error that shouldn't be possible in this method, due to early integrity checks. The error can only be thrown in case of lack of those verifications and, therefore, if inconsistent information is on the database.
     * @throws ReferenceToNonexistentEntity Exception thrown in case there is a reference to an unexistant entity during the method execution (if either the creator or at least one of the voters of the traceability data doesn't exist). This also shouldn't happen because this information shouldn't be registered in the blockchain.
     */
    void approveTraceabilityDataEntry(TraceabilityData traceabilityData) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity
    {
        removeTraceabilityDataFromDB();

        api = new TraceabilityDataValidatedRepositoryAPI(api);
        TraceabilityData newTraceabilityData = new TraceabilityDataValidated(traceabilityData.getInputDatasets(),
                traceabilityData.getCommand(), traceabilityData.getOutputDatasets(),
                traceabilityData.getCreatorID(), traceabilityData.getApprovers(),
                traceabilityData.getRejecters(), 0.0);
        TraceabilityDataInfo newTraceabilityDataInfo = new TraceabilityDataInfo(traceabilityDataInfo.getUUID(), newTraceabilityData);
        try
        {
            api.create(newTraceabilityDataInfo);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }

        api = new EntityDataRepositoryAPI(api);
        EntityReputationManager entityReputationManager = new EntityReputationManager(api);

        unStakeCreatorAndVotersReputation(traceabilityData, entityReputationManager);
        Double value = traceabilityData.getValue();

        Double rewardForCreating = ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForCreatingCorrectTraceabilityData(value);
        Double totalRewardForApprovers = ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForUpVotingCorrectTraceabilityData(value);
        Double totalPenaltyForRejecters = ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForDownVotingCorrectTraceabilityData(value);
        ArrayList<Double> rewardForEachApprover = ChaincodeConfigs.rewardDistributor.distributeReputation(totalRewardForApprovers, traceabilityData.getApprovers());
        ArrayList<Double> penaltyForEachRejecter = ChaincodeConfigs.rewardDistributor.distributeReputation(totalPenaltyForRejecters, traceabilityData.getRejecters());

        try
        {
            entityReputationManager.rewardEntity(traceabilityData.getCreatorID(), rewardForCreating);
            entityReputationManager.rewardEntities(traceabilityData.getApprovers(), rewardForEachApprover);
            entityReputationManager.penalizeEntities(traceabilityData.getRejecters(), penaltyForEachRejecter);
        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new InternalError("Internal error occurred on processing by the state machine: got not enough reputation error on closing voting round: rewarding and penalizing creator and voters");
        }
    }

    /**
     * This auxiliary method performs the procedures for unstaking the creator and voters' reputation upon termination of the voting round. The procedures are common for both when the network decides by consensus to approve or reject the traceability data.
     * @param traceabilityData An instance of class TraceabilityData representing the traceability data of which the voting round will be terminated and that will be registered as valid in the blockchain.
     * @param entityReputationManager An instance of class EntityReputationManager responsible for managing the reputation of entities.
     * @throws ReferenceToNonexistentEntity Exception thrown in case there is a reference to an unexistant entity during the method execution (if either the creator or at least one of the voters of the traceability data doesn't exist). This also shouldn't happen because this information shouldn't be registered in the blockchain.
     */
    private void unStakeCreatorAndVotersReputation(TraceabilityData traceabilityData, EntityReputationManager entityReputationManager) throws ReferenceToNonexistentEntity
    {
        Double value = traceabilityData.getValue();
        Double unStakeForCreating = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForCreatingTraceabilityData(value);
        Double unStakeForApprovers = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(value);
        Double unStakeForRejecters = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(value);
        try
        {
            entityReputationManager.unstakeEntityReputation(traceabilityData.getCreatorID(), unStakeForCreating);
            entityReputationManager.unstakeEntitiesReputation(traceabilityData.getApprovers(), unStakeForApprovers);
            entityReputationManager.unstakeEntitiesReputation(traceabilityData.getRejecters(), unStakeForRejecters);

        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new InternalError("Internal error occurred on processing by the state machine: got not enough reputation error on closing voting round: unstaking stake ammounts");
        }
    }

    /**
     * This method performs the procedures necessary for the termination of the voting round when the network decides by consensus to reject the traceability data.
     * @param traceabilityData An instance of class TraceabilityData representing the traceability data of which the voting round will be terminated and that will be registered as valid in the blockchain.
     * @throws IncosistentInfoFoundOnDB Exception thrown in case there is an error that shouldn't be possible in this method, due to early integrity checks. The error can only be thrown in case of lack of those verifications and, therefore, if inconsistent information is on the database.
     * @throws ReferenceToNonexistentEntity Exception thrown in case there is a reference to an unexistant entity during the method execution (if either the creator or at least one of the voters of the traceability data doesn't exist). This also shouldn't happen because this information shouldn't be registered in the blockchain.
     */
    void rejectTraceabilityDataEntry(TraceabilityData traceabilityData) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity
    {
        removeTraceabilityDataFromDB();

        EntityReputationManager entityReputationManager = new EntityReputationManager(api);
        unStakeCreatorAndVotersReputation(traceabilityData, entityReputationManager);

        Double value = traceabilityData.getValue();
        Double penaltyForCreating = ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForCreatingIncorrectTraceabilityData(value);
        Double penaltyForApprovers = ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForUpVotingIncorrectTraceabilityData(value);
        Double rewardForRejecters = ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForDownVotingIncorrectTraceabilityData(value);
        try
        {
            entityReputationManager.rewardEntity(traceabilityData.getCreatorID(), penaltyForCreating);
            entityReputationManager.penalizeEntities(traceabilityData.getApprovers(), penaltyForApprovers);
            entityReputationManager.rewardEntities(traceabilityData.getRejecters(), rewardForRejecters);

        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new InternalError("Internal error occurred on processing by the state machine: got not enough reputation error on trying to reward entity. This means that a number was going to be made negative when adding a positive factor to it. Something went really wrong...");
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            referenceToNonexistentEntity.printStackTrace();
        }
    }

    /**
     * This auxiliary method performs the procedures for removing the traceability data from the database upon termination of the voting round. The procedures are common for both when the network decides by consensus to approve or reject the traceability data.
     * @throws IncosistentInfoFoundOnDB Exception thrown in case there is an error that shouldn't be possible in this method, due to early integrity checks. The error can only be thrown in case of lack of those verifications and, therefore, if inconsistent information is on the database.
     */
    private void removeTraceabilityDataFromDB() throws IncosistentInfoFoundOnDB
    {
        api = new TraceabilityDataAwaitingValidationRepositoryAPI(api);
        try
        {
            api.remove(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("object not found when trying to delete the info on the DB in order to switch state");
        }
    }
}
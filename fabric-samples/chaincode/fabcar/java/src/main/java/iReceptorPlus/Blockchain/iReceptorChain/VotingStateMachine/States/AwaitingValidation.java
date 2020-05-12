package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPlaceVote;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;

import javax.swing.text.html.parser.Entity;

/**
 * This is the sub class for the state machine for the traceability information.
 * This sub class implements the specific behaviour necessary for when the traceability information is in the AwaitingValidation state.
 */
public class AwaitingValidation extends State
{
    public AwaitingValidation(TraceabilityDataInfo traceabilityData, HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(traceabilityData, api);
    }

    @Override
    public void voteYesForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
    {
        updateEntityReputation(voterID);

        TraceabilityData traceabilityData = traceabilityDataInfo.getTraceabilityData();
        traceabilityData.registerYesVoteForValidity(voterID);
        System.err.println("traceabilityData.getNumberOfApprovers(): " + traceabilityData.getNumberOfApprovers());
        System.err.println("((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters(): " + ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters());
        System.err.println("numberOfApprovers.doubleValue() / numberOfRejecters.doubleValue(): " + (double) traceabilityData.getNumberOfApprovers() / ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters());
        if (conditionToApproveTraceabilityInfo(traceabilityData.getNumberOfApprovers(), ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters()))
        {
            switchInfoStateFromAwatingValidationToValidated(traceabilityData);



        }
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to update traceability entry after registering yes vote");
        }
    }

    private void updateEntityReputation(EntityID voterID) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
    {
        EntityDataRepositoryAPI entityRepository = new EntityDataRepositoryAPI(api);
        EntityData entityData;
        try
        {
            entityData = (EntityData) entityRepository.read(voterID.getId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(voterID.getId());
        }
        Long currentReputation = entityData.getReputation();
        Long reputationAtStake = entityData.getReputationAtStake();
        long stakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry.get();
        if (currentReputation < stakeNecessary)
        {
            throw new EntityDoesNotHaveEnoughReputationToPlaceVote(currentReputation, stakeNecessary);
        }
        currentReputation -= stakeNecessary;
        reputationAtStake += stakeNecessary;
        entityData = new EntityData(entityData.getClientIdentity(), currentReputation, reputationAtStake);
        try
        {
            entityRepository.update(voterID.getId(), entityData);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(voterID.getId());
        }
    }

    private void switchInfoStateFromAwatingValidationToValidated(TraceabilityData traceabilityData) throws IncosistentInfoFoundOnDB
    {
        try
        {
            api.remove(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("object not found when trying to delete the info on the DB in order to switch state");
        }

        TraceabilityData newTraceabilityData = new TraceabilityDataValidated(traceabilityData.getInputDatasetHashValue(),
                traceabilityData.getOutputDatasetHashValue(), traceabilityData.getProcessingDetails(),
                ((TraceabilityDataAwatingValidation)traceabilityData).getApprovers());
        TraceabilityDataInfo newTraceabilityDataInfo = new TraceabilityDataInfo(traceabilityDataInfo.getUUID(), newTraceabilityData);
        api = new TraceabilityDataValidatedRepositoryAPI(api);
        try
        {
            api.create(newTraceabilityDataInfo);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }
    }

    private boolean conditionToApproveTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return numberOfApprovers >= ChaincodeConfigs.numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid.get()
                && numberOfApprovers.doubleValue() / numberOfRejecters.doubleValue() >= ChaincodeConfigs.racioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid.get();
    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB
    {
        //TODO ver o q fazer neste caso (shut down the round immediately???)
        traceabilityDataInfo.getTraceabilityData().registerNoVoteForValidity(voterID);
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }
    }

}

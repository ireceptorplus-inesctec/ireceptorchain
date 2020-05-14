package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.EntityReputationManager;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPlaceVote;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;

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
        long stakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry.get();
        EntityReputationManager entityReputationManager = new EntityReputationManager(api);
        try
        {
            entityReputationManager.stakeEntityReputation(voterID, stakeNecessary);
        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new EntityDoesNotHaveEnoughReputationToPlaceVote(entityDoesNotHaveEnoughReputationToPerformAction.getReputationOfEntity(), entityDoesNotHaveEnoughReputationToPerformAction.getNecessaryReputation());
        }

        TraceabilityData traceabilityData = traceabilityDataInfo.getTraceabilityData();
        traceabilityData.registerYesVoteForValidity(voterID);
        System.err.println("traceabilityData.getNumberOfApprovers(): " + traceabilityData.getNumberOfApprovers());
        System.err.println("((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters(): " + ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters());
        System.err.println("numberOfApprovers.doubleValue() / numberOfRejecters.doubleValue(): " + (double) traceabilityData.getNumberOfApprovers() / ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters());
        if (conditionToApproveTraceabilityInfo(traceabilityData.getNumberOfApprovers(), ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters()))
        {
            RoundFinisher roundFinisher = new RoundFinisher();
            roundFinisher.approveTraceabilityDataEntry(traceabilityData, voterID);
        }
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to update traceability entry after registering yes vote");
        }
    }

    private boolean conditionToApproveTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return ChaincodeConfigs.conditionToApproveTraceabilityInfo(numberOfApprovers, numberOfRejecters);
    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
    {
        long stakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForDownVotingTraceabilityDataEntry.get();
        EntityReputationManager entityReputationManager = new EntityReputationManager(api);
        try
        {
            entityReputationManager.stakeEntityReputation(voterID, stakeNecessary);
        } catch (EntityDoesNotHaveEnoughReputationToPerformAction entityDoesNotHaveEnoughReputationToPerformAction)
        {
            throw new EntityDoesNotHaveEnoughReputationToPlaceVote(entityDoesNotHaveEnoughReputationToPerformAction.getReputationOfEntity(), entityDoesNotHaveEnoughReputationToPerformAction.getNecessaryReputation());
        }

        //TODO ver o q fazer neste caso (shut down the round immediately???)
        traceabilityDataInfo.getTraceabilityData().registerNoVoteForValidity(voterID);
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }
        TraceabilityData traceabilityData = (TraceabilityData) traceabilityDataInfo.getData();
        if (conditionToRejectTraceabilityInfo(traceabilityData.getNumberOfApprovers(), ((TraceabilityDataAwatingValidation) traceabilityData).getNumberOfRejecters()))
        {
            RoundFinisher roundFinisher = new RoundFinisher();
            roundFinisher.rejectTraceabilityDataEntry(traceabilityData, voterID);
        }
    }

    private boolean conditionToRejectTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return ChaincodeConfigs.conditionToRejectTraceabilityInfo(numberOfApprovers, numberOfRejecters);
    }
}

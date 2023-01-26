package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.EntityReputationManager;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.*;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Returns.VotingStateMachineReturn;

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
    public VotingStateMachineReturn voteYesForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote, EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry
    {
        if (!entityIsNotVotingTwice(voterID))
            throw new EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry(voterID, traceabilityDataInfo.getUUID());

        Double value = traceabilityDataInfo.getTraceabilityData().getValue();
        double stakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(value);
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
        System.err.println("(getEntityIdFromContext(ctx)traceabilityData).getNumberOfRejecters(): " + traceabilityData.getNumberOfRejecters());
        System.err.println("numberOfApprovers.doubleValue() / numberOfRejecters.doubleValue(): " + (double) traceabilityData.getNumberOfApprovers() / traceabilityData.getNumberOfRejecters());
        if (conditionToApproveTraceabilityInfo(traceabilityData.getNumberOfApprovers(), traceabilityData.getNumberOfRejecters()))
        {
            RoundFinisher roundFinisher = new RoundFinisher(traceabilityDataInfo, api);
            roundFinisher.approveTraceabilityDataEntry(traceabilityData);
            return new VotingStateMachineReturn("Vote submitted successfully. Traceability data was approved.", true);
        }
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to update traceability entry after registering yes vote.");
        }

        return new VotingStateMachineReturn("Vote submitted successfully. Traceability data remains waiting for validation.", false);
    }

    private boolean conditionToApproveTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return ChaincodeConfigs.conditionToApproveTraceabilityInfo(numberOfApprovers, numberOfRejecters);
    }

    @Override
    public VotingStateMachineReturn voteNoForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote, EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry
    {
        if (!entityIsNotVotingTwice(voterID))
            throw new EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry(voterID, traceabilityDataInfo.getUUID());

        Double value = traceabilityDataInfo.getTraceabilityData().getValue();
        Double stakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(value);
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
        if (conditionToRejectTraceabilityInfo(traceabilityData.getNumberOfApprovers(), traceabilityData.getNumberOfRejecters()))
        {
            RoundFinisher roundFinisher = new RoundFinisher(traceabilityDataInfo, api);
            roundFinisher.rejectTraceabilityDataEntry(traceabilityData);
            return new VotingStateMachineReturn("Vote submitted successfully. Traceability data was rejected.", true);
        }

        return new VotingStateMachineReturn("Vote submitted successfully. Traceability data remains waiting for validation.", false);
    }

    private boolean conditionToRejectTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return ChaincodeConfigs.conditionToRejectTraceabilityInfo(numberOfApprovers, numberOfRejecters);
    }

    private boolean entityIsNotVotingTwice(EntityID entityID)
    {
        return traceabilityDataInfo.getTraceabilityData().getApprovers().indexOf(entityID) == -1 &&
                traceabilityDataInfo.getTraceabilityData().getRejecters().indexOf(entityID) == -1;
    }
}

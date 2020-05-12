package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToPlaceVote extends TraceabilityInfoStateMachineException
{
    Long reputationOfEntity;

    Long necessaryReputationForVoting;

    public EntityDoesNotHaveEnoughReputationToPlaceVote(Long reputationOfEntity, Long necessaryReputationForVoting)
    {
        super("Entity does not have enough reputation to place vote");
        this.reputationOfEntity = reputationOfEntity;
        this.necessaryReputationForVoting = necessaryReputationForVoting;
    }
}

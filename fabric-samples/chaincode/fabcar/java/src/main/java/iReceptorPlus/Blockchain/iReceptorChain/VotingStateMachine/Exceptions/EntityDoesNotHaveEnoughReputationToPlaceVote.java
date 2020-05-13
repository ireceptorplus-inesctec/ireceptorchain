package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToPlaceVote extends EntityDoesNotHaveEnoughReputationToPerformAction
{

    public EntityDoesNotHaveEnoughReputationToPlaceVote(Long reputationOfEntity, Long necessaryReputationForVoting)
    {
        super("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputationForVoting, reputationOfEntity, necessaryReputationForVoting);
    }
}

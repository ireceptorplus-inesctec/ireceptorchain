package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToPlaceVote extends EntityDoesNotHaveEnoughReputationToPerformAction
{
    public EntityDoesNotHaveEnoughReputationToPlaceVote(Long necessaryReputationForVoting)
    {
        super("Entity does not have enough reputation to place vote.", necessaryReputationForVoting);
    }

    @Override
    public String getMessage()
    {
        return message + " Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputation;
    }
}

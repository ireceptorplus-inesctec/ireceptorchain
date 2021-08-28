package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToPlaceVote extends EntityDoesNotHaveEnoughReputationToPerformAction
{
    public EntityDoesNotHaveEnoughReputationToPlaceVote(Double reputationOfEntity, Double necessaryReputationForVoting)
    {
        super("Entity does not have enough reputation to place vote.", reputationOfEntity, necessaryReputationForVoting);
    }

    @Override
    public String getMessage()
    {
        return message + " Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputation;
    }
}

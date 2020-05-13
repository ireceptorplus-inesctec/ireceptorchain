package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry extends EntityDoesNotHaveEnoughReputationToPerformAction
{
    public EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry(Long necessaryReputationForCreating)
    {
        super("Entity does not have enough reputation to place vote.", necessaryReputationForCreating);
    }

    @Override
    public String getMessage()
    {
        return message + " Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputation;
    }

}

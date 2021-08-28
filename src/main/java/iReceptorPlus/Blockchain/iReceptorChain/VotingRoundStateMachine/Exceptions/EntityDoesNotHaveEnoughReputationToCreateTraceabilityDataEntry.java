package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry extends EntityDoesNotHaveEnoughReputationToPerformAction
{
    public EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry(Double reputationOfEntity, Double necessaryReputationForCreating)
    {
        super("Entity does not have enough reputation to create traceability data entry.", reputationOfEntity, necessaryReputationForCreating);
    }

    @Override
    public String getMessage()
    {
        return message + " Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputation;
    }

}

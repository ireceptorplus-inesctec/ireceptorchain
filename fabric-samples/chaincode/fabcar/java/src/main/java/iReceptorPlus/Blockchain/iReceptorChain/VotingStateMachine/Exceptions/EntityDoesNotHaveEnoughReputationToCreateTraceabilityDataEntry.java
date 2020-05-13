package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry extends EntityDoesNotHaveEnoughReputationToPerformAction
{

    public EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry(Long reputationOfEntity, Long necessaryReputationForCreating)
    {
        super("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationOfEntity + " and necessary reputation is " + necessaryReputationForCreating, reputationOfEntity, necessaryReputationForCreating);
    }
}

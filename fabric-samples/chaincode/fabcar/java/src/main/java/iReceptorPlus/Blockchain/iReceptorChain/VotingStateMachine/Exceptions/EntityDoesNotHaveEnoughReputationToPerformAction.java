package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

public class EntityDoesNotHaveEnoughReputationToPerformAction extends TraceabilityInfoStateMachineException
{
    Long reputationOfEntity;

    Long necessaryReputation;

    /**
     * Constructor for this class that receives the message as parameter.
     * Should be called by the subclasses and the message should be customized by them (either built or received as their own constructor parameter).
     *
     * @param message The string message that will be set as the message for the exception that occurred.
     */
    public EntityDoesNotHaveEnoughReputationToPerformAction(String message, Long reputationOfEntity, Long necessaryReputation)
    {
        super(message);
        this.reputationOfEntity = reputationOfEntity;
        this.necessaryReputation = necessaryReputation;
    }

    public Long getReputationOfEntity()
    {
        return reputationOfEntity;
    }

    public Long getNecessaryReputation()
    {
        return necessaryReputation;
    }
}

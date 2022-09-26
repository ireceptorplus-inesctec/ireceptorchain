package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

public class IncosistentInfoFoundOnDB extends TraceabilityInfoStateMachineException
{
    /**
     * Constructor for this class that receives the message as parameter.
     *
     * @param message The string message that will be set as the message for the exception that occurred.
     */
    public IncosistentInfoFoundOnDB(String message)
    {
        super(message);
    }
}

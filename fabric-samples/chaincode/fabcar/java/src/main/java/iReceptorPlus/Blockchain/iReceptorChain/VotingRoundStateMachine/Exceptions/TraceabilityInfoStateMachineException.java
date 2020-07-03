package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

/**
 * This is the base class for representing exceptions that occur upon processing by the state machine.
 * Derived classes implement specific information for each type of exception.
 */
public abstract class TraceabilityInfoStateMachineException extends Exception
{
    /**
     * A string describing the exception.
     */
    protected String message;

    /**
     * Getter for the string describing the exception.
     * Subclasses may choose to override this method in order to display customized messages (different than the ones saved in the message attribute).
     * They may as well set the message attribute and leave this method as is (not overridden), achieving the objective.
     * @return the string held by the message attribute of this class.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Constructor for this class that receives the message as parameter.
     * Should be called by the subclasses and the message should be customized by them (either built or received as their own constructor parameter).
     * @param message The string message that will be set as the message for the exception that occurred.
     */
    public TraceabilityInfoStateMachineException(String message)
    {
        this.message = message;
    }
}

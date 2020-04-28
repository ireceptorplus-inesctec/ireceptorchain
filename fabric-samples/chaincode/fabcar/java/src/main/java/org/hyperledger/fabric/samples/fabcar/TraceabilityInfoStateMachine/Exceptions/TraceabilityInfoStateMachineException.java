package org.hyperledger.fabric.samples.fabcar.TraceabilityInfoStateMachine.Exceptions;

/**
 * This is the base class for representing exceptions that occur upon processing by the state machine.
 * Derived classes implement specific information for each type of exception.
 */
public abstract class TraceabilityInfoStateMachineException
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

    public TraceabilityInfoStateMachineException(String message)
    {
        this.message = message;
    }
}

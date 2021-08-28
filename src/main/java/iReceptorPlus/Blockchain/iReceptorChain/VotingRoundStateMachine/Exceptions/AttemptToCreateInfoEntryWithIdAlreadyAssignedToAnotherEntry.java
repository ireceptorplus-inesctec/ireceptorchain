package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

/**
 * This is a subclass that represents a specific type of exception upon processing by the state machine:
 * Attempt to create an entry of traceability information with the id already assigned to another entry.
 */
public class AttemptToCreateInfoEntryWithIdAlreadyAssignedToAnotherEntry extends TraceabilityInfoStateMachineException
{
    /**
     * The id used to create the traceability information.
     */
    String id;

    public AttemptToCreateInfoEntryWithIdAlreadyAssignedToAnotherEntry(String message, String id)
    {
        super("Attempt to create traceability information entry with id already assigned to another entry. Id attempted was " + id);
        this.id = id;
    }
}

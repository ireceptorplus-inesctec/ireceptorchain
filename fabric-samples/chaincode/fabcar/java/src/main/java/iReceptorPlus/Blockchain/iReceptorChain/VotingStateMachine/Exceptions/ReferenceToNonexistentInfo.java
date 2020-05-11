package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

/**
 * This is a subclass that represents a specific type of exception upon processing by the state machine:
 * Reference to a nonexistent entry of traceability information by its id.
 */
public class ReferenceToNonexistentInfo extends ReferenceToIdException
{
    public ReferenceToNonexistentInfo(String id)
    {
        super("Reference to nonexistent traceability data information entry.", id);
    }
}

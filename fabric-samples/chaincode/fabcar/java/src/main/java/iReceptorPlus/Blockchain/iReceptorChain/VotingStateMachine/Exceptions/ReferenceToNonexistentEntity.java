package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions;

/**
 * This is a subclass that represents a specific type of exception upon processing by the state machine:
 * Reference to a nonexistent entity by its id.
 */
public class ReferenceToNonexistentEntity extends ReferenceToIdException
{
    /**
     * Constructor for this class that receives the id as parameter.
     *
     * @param id The id used to reference the information.
     */
    public ReferenceToNonexistentEntity(String id)
    {
        super("Reference to nonexistent traceability data information entry.", id);
    }
}

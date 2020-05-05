package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.Exceptions;

/**
 * This is a subclass that represents a specific type of exception upon processing by the state machine:
 * Reference to a nonexistent entry of traceability information by its id.
 */
public class ReferenceToNonexistentInfo extends TraceabilityInfoStateMachineException
{
    /**
     * The id used to reference the traceability information.
     */
    String id;

    public ReferenceToNonexistentInfo(String id)
    {
        super("Reference to nonexistent traceability information entry, using id " + id);
        this.id = id;
    }

    /**
     * Getter to the id object.
     * @return the id object.
     */
    public String getId()
    {
        return id;
    }

}

package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Returns;

/**
 * This class represents the return of the voting state machine.
 * All returns of the state machine should be instances of subclasses of this class.
 */
public abstract class VotingStateMachineReturn
{
    /**
     * A message describing the return.
     */
    String message;

    /**
     * A boolean identifying whether the execution was successful or not.
     */
    boolean success;

    /**
     * A boolean identifying whether the execution has caused the traceability information to change its state.
     */
    boolean stateChange;

    public VotingStateMachineReturn(String message, boolean success, boolean stateChange)
    {
        this.message = message;
        this.success = success;
        this.stateChange = stateChange;
    }
}

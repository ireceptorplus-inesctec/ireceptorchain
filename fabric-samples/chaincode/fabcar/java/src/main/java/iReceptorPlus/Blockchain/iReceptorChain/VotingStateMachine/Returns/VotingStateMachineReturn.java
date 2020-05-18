package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Returns;

/**
 * This class represents the return of the voting state machine.
 * All returns of the state machine should be instances of subclasses of this class.
 */
public class VotingStateMachineReturn
{
    /**
     * A message describing the return.
     */
    String message;

    /**
     * A boolean identifying whether the execution has caused the traceability information to change its state.
     */
    boolean stateChange;

    public VotingStateMachineReturn(String message, boolean stateChange)
    {
        this.message = message;
        this.stateChange = stateChange;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isStateChange()
    {
        return stateChange;
    }
}

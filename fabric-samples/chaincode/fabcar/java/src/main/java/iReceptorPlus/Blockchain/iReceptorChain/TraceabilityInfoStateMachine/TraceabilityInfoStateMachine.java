package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.TraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.TraceabilityInfoAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.TraceabilityInfoValidated;
import iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.Exceptions.UnsupportedTypeOfTraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States.AwaitingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States.State;
import iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States.Validated;

/**
 * This class implements a state machine for the traceability information.
 * Since all the information must be saved on the blockchain, the data classes are well split from the logic classes.
 * This class is called to perform actions based on the state of the traceability information.
 */
public class TraceabilityInfoStateMachine
{
    /**
     * The TraceabilityInfo instance where the class will operate.
     */
    TraceabilityInfo traceabilityInfo;
    /**
     * An instance of a subclass of class State that implements the required logic for the specific state that the state machine should be in.
     * The instance is created on the constructor, based on the type of TraceabilityInfo passed as argument to the constructor.
     */
    State state;

    public TraceabilityInfoStateMachine(TraceabilityInfo traceabilityInfo) throws UnsupportedTypeOfTraceabilityInfo
    {
        this.traceabilityInfo = traceabilityInfo;
        if (traceabilityInfo instanceof TraceabilityInfoAwatingValidation)
            state = new AwaitingValidation();
        else if (traceabilityInfo instanceof TraceabilityInfoValidated)
            state = new Validated();
        else
            throw new UnsupportedTypeOfTraceabilityInfo("The traceability information given is not supported by the state machine");

    }
}

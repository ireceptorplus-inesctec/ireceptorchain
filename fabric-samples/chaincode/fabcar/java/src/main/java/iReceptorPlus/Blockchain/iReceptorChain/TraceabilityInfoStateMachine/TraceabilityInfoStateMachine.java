package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityInfoAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityInfoValidated;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;
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

    /**
     * An instance of class HyperledgerFabricChainCodeAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (TraceabilityInfoStateMachine).
     */
    HyperledgerFabricChainCodeAPI api;

    public TraceabilityInfoStateMachine(TraceabilityInfo traceabilityInfo, HyperledgerFabricChainCodeAPI api) throws UnsupportedTypeOfTraceabilityInfo
    {
        this.traceabilityInfo = traceabilityInfo;
        this.api = api;
        if (traceabilityInfo instanceof TraceabilityInfoAwatingValidation)
            state = new AwaitingValidation(traceabilityInfo, api);
        else if (traceabilityInfo instanceof TraceabilityInfoValidated)
            state = new Validated(traceabilityInfo, api);
        else
            throw new UnsupportedTypeOfTraceabilityInfo("The traceability information given is not supported by the state machine");

    }
}

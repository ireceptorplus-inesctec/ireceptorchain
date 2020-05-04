package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.TraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;

/**
 * This is the base class for the state machine for the traceability information.
 * Subclasses of this class implement the specific actions for each state of the traceability information.
 * Since all the information must be saved on the blockchain, the data classes are well split from the logic classes. The state classes implement the logic.
 * This class is called to perform actions based on the state of the traceability information.
 */
public abstract class State
{
    /**
     * The TraceabilityInfo instance where the class will operate.
     */
    TraceabilityInfo traceabilityInfo;

    /**
     * An instance of class HyperledgerFabricChainCodeAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (TraceabilityInfoStateMachine).
     */
    HyperledgerFabricChainCodeAPI api;

    public State(TraceabilityInfo traceabilityInfo, HyperledgerFabricChainCodeAPI api)
    {
        this.traceabilityInfo = traceabilityInfo;
        this.api = api;
    }

    public abstract void voteYesForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter);

    public abstract void voteNoForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter);

    public abstract void flagTraceabilityInfoAsFalse(TraceabilityInfo traceabilityInfo, Entity whistleblower);

}

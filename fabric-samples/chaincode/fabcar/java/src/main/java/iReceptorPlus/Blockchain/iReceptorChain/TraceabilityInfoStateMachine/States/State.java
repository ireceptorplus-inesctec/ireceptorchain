package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;

/**
 * This is the base class for the state machine for the traceability information.
 * Subclasses of this class implement the specific actions for each state of the traceability information.
 * Since all the information must be saved on the blockchain, the data classes are well split from the logic classes. The state classes implement the logic.
 * This class is called to perform actions based on the state of the traceability information.
 */
public abstract class State
{
    /**
     * The TraceabilityData instance where the class will operate.
     */
    TraceabilityDataInfo traceabilityDataInfo;

    /**
     * An instance of class HyperledgerFabricChainCodeAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (TraceabilityInfoStateMachine).
     */
    HyperledgerFabricChainCodeAPI api;

    public State(TraceabilityDataInfo traceabilityDataInfo, HyperledgerFabricChainCodeAPI api)
    {
        this.traceabilityDataInfo = traceabilityDataInfo;
        this.api = api;
    }

    public abstract void voteYesForTheVeracityOfTraceabilityInfo(TraceabilityData traceabilityData, Entity voter);

    public abstract void voteNoForTheVeracityOfTraceabilityInfo(TraceabilityData traceabilityData, Entity voter);

    public abstract void flagTraceabilityInfoAsFalse(TraceabilityData traceabilityData, Entity whistleblower);

}

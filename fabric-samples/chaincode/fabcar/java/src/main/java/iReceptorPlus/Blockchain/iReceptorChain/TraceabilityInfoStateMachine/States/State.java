package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.Exceptions.IncosistentInfoFoundOnDB;

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
     * An instance of class HyperledgerFabricBlockhainRepositoryAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (TraceabilityInfoStateMachine).
     */
    HyperledgerFabricBlockhainRepositoryAPI api;

    public State(TraceabilityDataInfo traceabilityDataInfo, HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.traceabilityDataInfo = traceabilityDataInfo;
        this.api = api;
    }

    public abstract void voteYesForTheVeracityOfTraceabilityInfo(Entity voter) throws IncosistentInfoFoundOnDB;

    public abstract void voteNoForTheVeracityOfTraceabilityInfo(Entity voter) throws IncosistentInfoFoundOnDB;

}

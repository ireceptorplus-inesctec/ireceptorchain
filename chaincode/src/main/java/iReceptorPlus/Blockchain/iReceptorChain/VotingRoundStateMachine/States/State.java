package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPlaceVote;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.ReferenceToNonexistentEntity;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Returns.VotingStateMachineReturn;

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
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (VotingRoundStateMachine).
     */
    HyperledgerFabricBlockhainRepositoryAPI api;

    public State(TraceabilityDataInfo traceabilityDataInfo, HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.traceabilityDataInfo = traceabilityDataInfo;
        this.api = api;
    }

    public abstract VotingStateMachineReturn voteYesForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote, EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry;

    public abstract VotingStateMachineReturn voteNoForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote, EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry;

}

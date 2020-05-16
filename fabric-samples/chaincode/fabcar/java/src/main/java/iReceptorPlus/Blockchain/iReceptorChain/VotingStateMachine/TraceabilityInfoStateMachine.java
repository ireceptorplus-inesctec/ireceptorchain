package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.*;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States.AwaitingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States.State;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States.Validated;
import org.hyperledger.fabric.shim.ChaincodeException;

/**
 * This class implements a state machine for the traceability information.
 * Since all the information must be saved on the blockchain, the data classes are well split from the logic classes.
 * This class is called to perform actions based on the state of the traceability information.
 */
public class TraceabilityInfoStateMachine
{
    /**
     * The TraceabilityData instance where the class will operate.
     */
    TraceabilityDataInfo traceabilityDataInfo;

    /**
     * An instance of a subclass of class State that implements the required logic for the specific state that the state machine should be in.
     * The instance is created on the constructor, based on the type of TraceabilityData passed as argument to the constructor.
     */
    State state;

    /**
     * An instance of class HyperledgerFabricBlockhainRepositoryAPI created using the current blockchain context.
     * The class implements all calls to the Hyperledger API in order to abstract that logic from this class (VotingStateMachine).
     */
    HyperledgerFabricBlockhainRepositoryAPI api;

    public TraceabilityInfoStateMachine(TraceabilityDataInfo traceabilityDataInfo, HyperledgerFabricBlockhainRepositoryAPI api) throws UnsupportedTypeOfTraceabilityInfo
    {
        this.traceabilityDataInfo = traceabilityDataInfo;
        this.api = api;
        TraceabilityData traceabilityData = traceabilityDataInfo.getTraceabilityData();

        if (traceabilityData instanceof TraceabilityDataAwatingValidation)
            state = new AwaitingValidation(traceabilityDataInfo, api);
        else if (traceabilityData instanceof TraceabilityDataValidated)
            state = new Validated(traceabilityDataInfo, api);
        else
            throw new UnsupportedTypeOfTraceabilityInfo("The traceability information given is not supported by the state machine");

    }

    /**
     * Makes the necessary procedures to initiate the voting round.
     * Creates the entry on the database and stakes the reputation for the peer who creates it.
     * @param creatorID An instance of class EntityID containing the information of the creator's id.
     * @throws ReferenceToNonexistentEntity Exception thrown when an entity with an id that is not registered is passed as parameter.
     * @throws EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry Exception thrown when the entity corresponding to the id passed as parameter does not have enough reputation to stake for creation of the traceability entry.
     */
    public void initVotingRound(EntityID creatorID) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction, GivenIdIsAlreadyAssignedToAnotherObject
    {
        long stakeNecessaryForCreating = ChaincodeConfigs.reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry.get();
        EntityReputationManager entityReputationManager = new EntityReputationManager(api);
        entityReputationManager.stakeEntityReputation(creatorID, stakeNecessaryForCreating);

        api.create(traceabilityDataInfo.getUUID(), traceabilityDataInfo.getTraceabilityData());
    }

    public void voteYesForTheVeracityOfTraceabilityInfo(EntityID voter) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
    {
        state.voteYesForTheVeracityOfTraceabilityInfo(voter);
    }

    public void voteNoForTheVeracityOfTraceabilityInfo(EntityID voter) throws IncosistentInfoFoundOnDB, ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
    {
        state.voteNoForTheVeracityOfTraceabilityInfo(voter);
    }
}

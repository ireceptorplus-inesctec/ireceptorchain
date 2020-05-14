package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPlaceVote;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States.AwaitingValidation;

import java.util.ArrayList;

/**
 * This class is used by the voting state machine classes to manage the reputation of a peer.
 * The methods should be called perform the changes to the reputation of the peer.
 */
public class EntityReputationManager
{
    /**
     * An instance of class HyperledgerFabricBlockhainRepositoryAPI that represents the repository for querying the data.
     */
    private final HyperledgerFabricBlockhainRepositoryAPI api;

    /**
     * Boolean identifying if negative reputation should be allowed.
     * If negative reputation is allowed, when subtracting reputation to the peer, no restrictions will be applied.
     */
    private boolean allowNegativeReputation;

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.api = api;
        this.allowNegativeReputation = false;
    }

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api, boolean allowNegativeReputation)
    {
        this.api = api;
        this.allowNegativeReputation = allowNegativeReputation;
    }

    public void stakeEntityReputation(EntityID voterID, Long stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -stakeNecessary, stakeNecessary);
    }

    public void unstakeEntityReputation(EntityID voterID, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, +unstakeAmount, -unstakeAmount);
    }

    public void unstakeEntitiesReputation(ArrayList<EntityID> voterID, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : voterID)
        {
            unstakeEntityReputation(currVoterID, unstakeAmount);
        }
    }

    public void rewardEntity(EntityID voterID, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, reward, new Long(0));
    }

    public void rewardEntities(ArrayList<EntityID> voterID, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : voterID)
        {
            rewardEntity(currVoterID, reward);
        }
    }

    public void penalizeEntity(EntityID voterID, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -penalty, new Long(0));
    }

    public void penalizeEntities(ArrayList<EntityID> voterID, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : voterID)
        {
            penalizeEntity(currVoterID, penalty);
        }
    }

    private void updateEntityReputation(EntityID voterID, Long addToCurrentReputation, Long addToReputationAtStake) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        EntityDataRepositoryAPI entityRepository = new EntityDataRepositoryAPI(api);
        EntityData entityData;
        try
        {
            entityData = (EntityData) entityRepository.read(voterID.getId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(voterID.getId());
        }
        Long currentReputation = entityData.getReputation();
        Long reputationAtStake = entityData.getReputationAtStake();
        if (currentReputation < -addToCurrentReputation && !allowNegativeReputation)
        {
            throw new EntityDoesNotHaveEnoughReputationToPerformAction("Entity does not have enough reputation", currentReputation, addToReputationAtStake);
        }
        currentReputation += addToCurrentReputation;
        reputationAtStake += addToReputationAtStake;
        entityData = new EntityData(entityData.getClientIdentity(), currentReputation, reputationAtStake);
        try
        {
            entityRepository.update(voterID.getId(), entityData);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(voterID.getId());
        }
    }
}
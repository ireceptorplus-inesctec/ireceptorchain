package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;

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

    public void stakeEntityReputation(EntityID entityID, Long stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, -stakeNecessary, stakeNecessary);
    }

    public void unstakeEntityReputation(EntityID entityID, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, +unstakeAmount, -unstakeAmount);
    }

    public void unstakeEntitiesReputation(ArrayList<EntityID> entityIDS, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            unstakeEntityReputation(currVoterID, unstakeAmount);
        }
    }

    public void rewardEntity(EntityID entityID, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, reward, new Long(0));
    }

    public void rewardEntities(ArrayList<EntityID> entityIDS, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            rewardEntity(currVoterID, reward);
        }
    }

    public void penalizeEntity(EntityID entityID, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, -penalty, new Long(0));
    }

    public void penalizeEntities(ArrayList<EntityID> entityIDS, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            penalizeEntity(currVoterID, penalty);
        }
    }

    private void updateEntityReputation(EntityID entityID, Long addToCurrentReputation, Long addToReputationAtStake) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        EntityDataRepositoryAPI entityRepository = new EntityDataRepositoryAPI(api);
        EntityData entityData;
        try
        {
            entityData = (EntityData) entityRepository.read(entityID.getId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(entityID.getId());
        }
        Long currentReputation = entityData.getReputation();
        Long reputationAtStake = entityData.getReputationAtStake();
        if (currentReputation < -addToCurrentReputation && !allowNegativeReputation)
        {
            throw new EntityDoesNotHaveEnoughReputationToPerformAction("Entity does not have enough reputation", currentReputation, addToReputationAtStake);
        }
        currentReputation += addToCurrentReputation;
        reputationAtStake += addToReputationAtStake;
        entityData = new EntityData(entityID.getId(), currentReputation, reputationAtStake);
        try
        {
            entityRepository.update(entityID.getId(), entityData);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(entityID.getId());
        }
    }
}
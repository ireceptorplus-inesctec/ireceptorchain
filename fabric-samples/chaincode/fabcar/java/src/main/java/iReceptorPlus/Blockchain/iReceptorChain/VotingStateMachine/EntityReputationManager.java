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

public class EntityReputationManager
{
    private final HyperledgerFabricBlockhainRepositoryAPI api;
    private EntityDoesNotHaveEnoughReputationToPerformAction exceptionToThrow;

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api, EntityDoesNotHaveEnoughReputationToPerformAction exceptionToThrow)
    {
        this.api = api;
        this.exceptionToThrow = exceptionToThrow;
    }

    public void stakeEntityReputation(EntityID voterID, Long stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -stakeNecessary, stakeNecessary, false);
    }

    public void unstakeEntityReputation(EntityID voterID, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, +unstakeAmount, -unstakeAmount);
    }

    public void penalizeEntity(EntityID voterID, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -penalty, new Long(0), true);
    }

    public void rewardEntity(EntityID voterID, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, reward, new Long(0), true);
    }

    private void updateEntityReputation(EntityID voterID, Long addToCurrentReputation, Long addToReputationAtStake) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, addToCurrentReputation, addToReputationAtStake);
    }

    private void updateEntityReputation(EntityID voterID, Long addToCurrentReputation, Long addToReputationAtStake, boolean allowNegativeReputation) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
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
            exceptionToThrow.setReputationOfEntity(currentReputation);
            throw exceptionToThrow;
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
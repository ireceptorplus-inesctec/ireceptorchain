package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPlaceVote;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.ReferenceToNonexistentEntity;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States.AwaitingValidation;

public class EntityReputationManager
{
    private final HyperledgerFabricBlockhainRepositoryAPI api;

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.api = api;
    }

    public void updateEntityReputation(EntityID voterID, Long stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPlaceVote
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
        if (currentReputation < stakeNecessary)
        {
            throw new EntityDoesNotHaveEnoughReputationToPlaceVote(currentReputation, stakeNecessary);
        }
        currentReputation -= stakeNecessary;
        reputationAtStake += stakeNecessary;
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
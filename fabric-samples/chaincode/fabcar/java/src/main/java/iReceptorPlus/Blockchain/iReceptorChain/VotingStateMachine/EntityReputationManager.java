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
     * An instance of class EntityDoesNotHaveEnoughReputationToPerformAction initialized with the appropriate subclass instance.
     */
    private EntityDoesNotHaveEnoughReputationToPerformAction exceptionToThrow;

    /**
     * Boolean identifying if negative reputation should be allowed.
     * If negative reputation is allowed, when subtracting reputation to the peer, no restrictions will be applied.
     * The value of this variable is set by the constructor and used by the functions.
     */
    private boolean allowNegativeReputation;

    /**
     * Constructor for this class. Receives the repository api and the exception to throw in case of negative resulting reputation.
     * @param api An instance of class HyperledgerFabricBlockhainRepositoryAPI that represents the repository for querying the data.
     * @param exceptionToThrow An instance of class EntityDoesNotHaveEnoughReputationToPerformAction initialized with the appropriate subclass instance.
     */
    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api, EntityDoesNotHaveEnoughReputationToPerformAction exceptionToThrow)
    {
        this.api = api;
        this.exceptionToThrow = exceptionToThrow;
        this.allowNegativeReputation = false;
    }

    /**
     * Constructor for this class. Receives the only the the repository api.
     * It is used when the exception should be ignored, i.e., when negative reputation should be allowed.
     * @param api An instance of class HyperledgerFabricBlockhainRepositoryAPI that represents the repository for querying the data.
     */
    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.api = api;
        this.allowNegativeReputation = true;
    }

    public void stakeEntityReputation(EntityID voterID, Long stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -stakeNecessary, stakeNecessary);
    }

    public void unstakeEntityReputation(EntityID voterID, Long unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, +unstakeAmount, -unstakeAmount);
    }

    public void penalizeEntity(EntityID voterID, Long penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, -penalty, new Long(0));
    }

    public void rewardEntity(EntityID voterID, Long reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(voterID, reward, new Long(0));
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
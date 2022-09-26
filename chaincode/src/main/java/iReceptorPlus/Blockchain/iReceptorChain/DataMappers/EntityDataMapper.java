package iReceptorPlus.Blockchain.iReceptorChain.DataMappers;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.EntityDataReturnType;

public class EntityDataMapper
{
    public EntityDataReturnType getEntityDataReturnTypeFromEntityData(EntityData data)
    {
        return new EntityDataReturnType(data.getId(), data.getId(), data.getReputation(), data.getReputationAtStake());
    }
}

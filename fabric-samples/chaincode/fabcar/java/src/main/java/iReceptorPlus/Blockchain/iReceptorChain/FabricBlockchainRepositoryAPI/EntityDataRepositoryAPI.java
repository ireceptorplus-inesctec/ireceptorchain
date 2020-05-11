package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import org.hyperledger.fabric.contract.Context;

public class EntityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    public EntityDataRepositoryAPI(Context ctx)
    {
        super(ctx, ChaincodeConfigs.getEntityDataKeyPrefix());
    }

    public EntityDataRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(api, ChaincodeConfigs.getEntityDataKeyPrefix());
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        return genson.deserialize(serializedData, EntityData.class);
    }

    @Override
    protected iReceptorChainDataTypeInfo deserializeData(String uuid, String serializedData)
    {
        return null;
    }
}

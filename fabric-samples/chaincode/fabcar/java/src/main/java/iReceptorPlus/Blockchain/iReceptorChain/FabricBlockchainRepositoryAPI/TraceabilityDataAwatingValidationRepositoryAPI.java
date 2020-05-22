package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import org.hyperledger.fabric.contract.Context;

public class TraceabilityDataAwatingValidationRepositoryAPI extends TraceabilityDataRepositoryAPI
{

    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public TraceabilityDataAwatingValidationRepositoryAPI(Context ctx)
    {
        super(ctx, ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix());
    }

    /**
     * Constructor that creates a new instance of this class from another class of the same type.
     * Uses the same blockchain context but not the same objectTypeIdentifier. Is is used when another repository is meant to be created when another repository is needed and the same context should be used.
     * @param api The instance of HyperledgerFabricBlockhainRepositoryAPI from which to copy the context from.
     */
    public TraceabilityDataAwatingValidationRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(api.ctx, ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix());
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        return genson.deserialize(serializedData, TraceabilityDataAwatingValidation.class);
    }

    @Override
    protected iReceptorChainDataTypeInfo deserializeData(String uuid, String serializedData)
    {
        return new TraceabilityDataInfo(uuid, deserializeData(serializedData));
    }
}

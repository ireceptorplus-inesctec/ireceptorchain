package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import org.hyperledger.fabric.contract.Context;

public class TraceabilityDataValidatedRepositoryAPI extends TraceabilityDataRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public TraceabilityDataValidatedRepositoryAPI(Context ctx)
    {
        super(ctx, ChaincodeConfigs.getTraceabilityValidatedKeyPrefix());
    }

    /**
     * Constructor that creates a new instance of this class from another class of the same type.
     * Uses the same blockchain context but not the same objectTypeIdentifier. Is is used when another repository is meant to be created when another repository is needed and the same context should be used.
     * @param api The instance of HyperledgerFabricBlockhainRepositoryAPI from which to copy the context from.
     */
    public TraceabilityDataValidatedRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(api.ctx, ChaincodeConfigs.getTraceabilityValidatedKeyPrefix());
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        return genson.deserialize(serializedData, TraceabilityDataValidated.class);
    }
}

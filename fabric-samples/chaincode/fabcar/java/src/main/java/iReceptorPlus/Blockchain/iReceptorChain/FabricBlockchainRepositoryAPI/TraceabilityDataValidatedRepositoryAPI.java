package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
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
        super(ctx);
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        return genson.deserialize(serializedData, TraceabilityDataValidated.class);
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import org.hyperledger.fabric.contract.Context;

/**
 * This is a superclass of the HyperledgerFabricBlockhainRepositoryAPI
 */
public class TraceabilityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public TraceabilityDataRepositoryAPI(Context ctx)
    {
        super(ctx, TraceabilityData.class);
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        //TODO
        return null;
    }
}

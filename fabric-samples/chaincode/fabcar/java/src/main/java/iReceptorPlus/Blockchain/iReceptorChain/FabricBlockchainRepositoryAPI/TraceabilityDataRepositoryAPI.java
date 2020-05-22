package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import org.hyperledger.fabric.contract.Context;

/**
 * This is a superclass of the HyperledgerFabricBlockhainRepositoryAPI
 */
public abstract class TraceabilityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     * @param objectTypeIdentifier A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     */
    public TraceabilityDataRepositoryAPI(Context ctx, String objectTypeIdentifier)
    {
        super(ctx, objectTypeIdentifier);
    }
}

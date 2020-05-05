package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import org.hyperledger.fabric.contract.Context;

public class TraceabilityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public TraceabilityDataRepositoryAPI(Context ctx)
    {
        super(ctx);
    }
}

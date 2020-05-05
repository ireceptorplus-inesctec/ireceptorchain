package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

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
}

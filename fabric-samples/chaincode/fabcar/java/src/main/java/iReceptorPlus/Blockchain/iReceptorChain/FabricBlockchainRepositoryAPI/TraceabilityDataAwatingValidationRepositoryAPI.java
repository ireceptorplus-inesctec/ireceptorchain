package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

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
        super(ctx);
    }
}

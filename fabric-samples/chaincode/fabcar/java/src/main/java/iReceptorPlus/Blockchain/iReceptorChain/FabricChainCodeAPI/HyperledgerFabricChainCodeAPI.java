package iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI;

import org.hyperledger.fabric.contract.Context;

/**
 * This class is an API that handles all calls to the hyperledger functions.
 * It is used to abstract complicated calls to the hyperledger API from the application logic.
 * The class receives the blockchain context in which this class will perform the required tasks and can then be called to perform operations on that same context without the need for interacting directly with hyperledger fabric's methods.
 */
public class HyperledgerFabricChainCodeAPI
{
    /**
     * The blockchain context in which this class will perform the required tasks.
     */
    private Context ctx;

    /**
     * Constructor for this class.
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public HyperledgerFabricChainCodeAPI(Context ctx)
    {
        this.ctx = ctx;
    }
}

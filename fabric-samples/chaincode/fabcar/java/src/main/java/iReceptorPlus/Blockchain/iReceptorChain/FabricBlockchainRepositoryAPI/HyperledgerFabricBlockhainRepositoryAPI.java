package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import org.hyperledger.fabric.contract.Context;

/**
 * This class is an API that handles all calls to the hyperledger functions.
 * It is used to abstract complicated calls to the hyperledger API from the application logic.
 * The class receives the blockchain context in which this class will perform the required tasks and can then be called to perform operations on that same context without the need for interacting directly with hyperledger fabric's methods.
 */
public abstract class HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * The blockchain context in which this class will perform the required tasks.
     */
    protected Context ctx;

    /**
     * Constructor for this class.
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public HyperledgerFabricBlockhainRepositoryAPI(Context ctx)
    {
        this.ctx = ctx;
    }

    public iReceptorChainDataTypeInfo create(iReceptorChainDataType data)
    {
        //TODO
        return null;
    }

    public iReceptorChainDataTypeInfo getTraceabilityInfo(String key)
    {
        //TODO
        return null;
    }

    public iReceptorChainDataTypeInfo updateTraceabilityInfo(iReceptorChainDataTypeInfo traceabilityDataInfo)
    {
        //TODO
        return null;
    }


    public void switchTraceabilityInfoStateFromAwaitingValidationToValidated(TraceabilityDataInfo newTraceabilityDataInfo)
    {
        //TODO delete older one and insert new one
    }
}

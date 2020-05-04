package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;

public class TraceabilityDataInfo
{
    /**
     * The traceability data that this class refers to just as it is stored on chain.
     */
    TraceabilityData traceabilityData;

    /**
     * The key used to store the traceability data on the blockchain.
     */
    String key;
}

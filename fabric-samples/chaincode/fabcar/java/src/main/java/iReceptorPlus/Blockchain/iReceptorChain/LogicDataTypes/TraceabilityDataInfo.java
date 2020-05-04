package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;

public class TraceabilityDataInfo
{
    /**
     * The traceability data that this class refers to just as it is stored on chain.
     */
    protected TraceabilityData traceabilityData;

    /**
     * The key used to store the traceability data on the blockchain.
     */
    protected String key;

    public TraceabilityDataInfo(TraceabilityData traceabilityData, String key)
    {
        this.traceabilityData = traceabilityData;
        this.key = key;
    }

    public TraceabilityData getTraceabilityData()
    {
        return traceabilityData;
    }

    public String getKey()
    {
        return key;
    }
}

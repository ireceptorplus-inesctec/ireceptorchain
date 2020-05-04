package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;

public class TraceabilityDataInfo
{
    /**
     * The key used to store the traceability data on the blockchain.
     */
    protected String key;

    /**
     * The traceability data that this class refers to just as it is stored on chain.
     */
    protected TraceabilityData traceabilityData;

    public TraceabilityDataInfo(String key, TraceabilityData traceabilityData)
    {
        this.key = key;
        this.traceabilityData = traceabilityData;
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

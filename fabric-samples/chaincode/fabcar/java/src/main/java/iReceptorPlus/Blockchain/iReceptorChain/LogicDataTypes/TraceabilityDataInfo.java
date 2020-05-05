package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;

public class TraceabilityDataInfo extends iReceptorChainDataTypeInfo
{
    public TraceabilityDataInfo(String key, iReceptorChainDataType data)
    {
        super(key, data);
    }

    public TraceabilityData getTraceabilityData()
    {
        return (TraceabilityData) data;
    }

    public String getKey()
    {
        return key;
    }
}

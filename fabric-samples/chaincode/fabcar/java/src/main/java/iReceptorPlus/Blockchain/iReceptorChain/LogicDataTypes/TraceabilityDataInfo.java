package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;

public class TraceabilityDataInfo extends iReceptorChainDataTypeInfo
{
    public TraceabilityDataInfo(String uuid, iReceptorChainDataType data)
    {
        super(uuid, data);
    }

    public TraceabilityData getTraceabilityData()
    {
        return (TraceabilityData) data;
    }

    public String getUUID()
    {
        return UUID;
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.DataMappers;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataReturnType;

import java.util.UUID;

public class TraceabilityDataMapper
{
    public TraceabilityDataReturnType getReturnTypeForTraceabilityData(String uuid, TraceabilityData data)
    {
        TraceabilityDataReturnType traceabilityDataReturnType = new TraceabilityDataReturnType(uuid.toString(),
                data.getProcessingDetails(), data.getCreatorID(), data.getApprovers(),
                data.getRejecters(), data.getValue());

        return traceabilityDataReturnType;
    }
}

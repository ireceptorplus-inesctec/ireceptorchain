package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeInputDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;

public class TraceabilityDataToSubmit extends TraceabilityData
{
    public TraceabilityDataToSubmit(String inputDatasetHashValue, String outputDatasetHashValue, ProcessingDetails processingDetails)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, new NullEntityID());
    }

    public TraceabilityDataToSubmit(String inputDatasetHashValue, String outputDatasetHashValue, ProcessingDetails processingDetails, Double value)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, new NullEntityID());
    }

    @Override
    public void registerYesVoteForValidity(EntityID entityID)
    {

    }

    @Override
    public void registerNoVoteForValidity(EntityID entityID)
    {

    }
}

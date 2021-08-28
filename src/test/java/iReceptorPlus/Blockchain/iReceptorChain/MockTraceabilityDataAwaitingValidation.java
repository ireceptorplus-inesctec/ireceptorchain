package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwaitingValidation;

import java.io.IOException;
import java.nio.channels.Channel;
import java.security.cert.CertificateException;

public class MockTraceabilityDataAwaitingValidation
{
    TraceabilityDataAwaitingValidation traceabilityData;

    public MockTraceabilityDataAwaitingValidation() throws CertificateException, IOException
    {
        MockClientIdentity mockClientIdentity = new MockClientIdentity();
        traceabilityData = new TraceabilityDataAwaitingValidation("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue", "softwareConfigParams"), new EntityID(mockClientIdentity.id), ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
    }

    public MockTraceabilityDataAwaitingValidation(String creatorID)
    {
        traceabilityData = new TraceabilityDataAwaitingValidation("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue", "softwareConfigParams"), new EntityID(creatorID), ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
    }
}

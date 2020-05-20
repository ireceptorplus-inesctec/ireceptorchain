package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;

import java.io.IOException;
import java.security.cert.CertificateException;

public class MockTraceabilityAwaitingData
{
    TraceabilityDataAwatingValidation traceabilityData;

    public MockTraceabilityAwaitingData() throws CertificateException, IOException
    {
        MockClientIdentity mockClientIdentity = new MockClientIdentity();
        traceabilityData = new TraceabilityDataAwatingValidation("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue", "softwareConfigParams"), new EntityID(mockClientIdentity.id));
    }

    public MockTraceabilityAwaitingData(String creatorID)
    {
        traceabilityData = new TraceabilityDataAwatingValidation("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue", "softwareConfigParams"), new EntityID(creatorID));
    }
}

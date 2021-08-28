package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class MockTraceabilityDataValidated
{
    TraceabilityDataValidated traceabilityData;

    public MockTraceabilityDataValidated() throws CertificateException, IOException
    {
        MockClientIdentity mockClientIdentity = new MockClientIdentity();
        traceabilityData = new TraceabilityDataValidated("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue",
                        "softwareConfigParams"), new EntityID(mockClientIdentity.id), new ArrayList<>(), new ArrayList<>(), 0.0);
    }

    public MockTraceabilityDataValidated(String creatorID)
    {
        traceabilityData = new TraceabilityDataValidated("inputDatasetHashValue", "outputDatasetHashValue",
                new ProcessingDetails("softwareId", "softwareVersion", "softwareBinaryExecutableHashValue",
                        "softwareConfigParams"), new EntityID(creatorID), new ArrayList<>(), new ArrayList<>(), 0.0);
    }
}

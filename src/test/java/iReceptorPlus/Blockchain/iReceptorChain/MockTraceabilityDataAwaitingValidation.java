package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import org.json.JSONException;

import java.io.IOException;
import java.nio.channels.Channel;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class MockTraceabilityDataAwaitingValidation
{
    TraceabilityDataAwaitingValidation traceabilityData;

    public MockTraceabilityDataAwaitingValidation() throws CertificateException, IOException, JSONException
    {
        this(new MockClientIdentity().id);
    }

    public MockTraceabilityDataAwaitingValidation(String creatorID)
    {
        DownloadbleFile inputDataset = new DownloadbleFile("767465ff8282385bfe1bc23a005f98ee2df325c369cefe9e89c7a42a60d22afa",
                "71cc009c-e91b-11ec-8fea-0242ac120002", "https://213.544.435.34/dataset/71cc009c-e91b-11ec-8fea-0242ac120002");
        ArrayList<DownloadbleFile> inputDatasets = new ArrayList<DownloadbleFile>();
        inputDatasets.add(inputDataset);
        DownloadbleFile outputDataset = new DownloadbleFile("7dbcd8ac1dbc8d09b57ed18f7f8229f16a1f8c46b5eaa87899c33216b21ec650",
                "d9359bb2-e91b-11ec-8fea-0242ac120002", "https://213.544.435.34/dataset/d9359bb2-e91b-11ec-8fea-0242ac120002");
        ArrayList<DownloadbleFile> outputDatasets = new ArrayList<DownloadbleFile>();
        outputDatasets.add(outputDataset);
        ProcessingDetails processingDetails = new ProcessingDetails(inputDatasets,
                new Command("MiXCR", "align"), outputDatasets);
        traceabilityData = new TraceabilityDataAwaitingValidation(processingDetails, new EntityID(creatorID),
                ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
    }
}

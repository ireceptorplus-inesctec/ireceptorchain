package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import org.json.JSONException;

import java.io.IOException;
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
        DownloadbleFile inputDataset = new DownloadbleFile("71cc009c-e91b-11ec-8fea-0242ac120002", "fasta",
                "https://213.544.435.34/dataset/71cc009c-e91b-11ec-8fea-0242ac120002",
                "767465ff8282385bfe1bc23a005f98ee2df325c369cefe9e89c7a42a60d22afa");
        ArrayList<DownloadbleFile> inputDatasets = new ArrayList<DownloadbleFile>();
        inputDatasets.add(inputDataset);
        DownloadbleFile outputDataset = new DownloadbleFile("d9359bb2-e91b-11ec-8fea-0242ac120002", "fasta",
                "https://213.544.435.34/dataset/d9359bb2-e91b-11ec-8fea-0242ac120002",
                "7dbcd8ac1dbc8d09b57ed18f7f8229f16a1f8c46b5eaa87899c33216b21ec650");
        ArrayList<DownloadbleFile> outputDatasets = new ArrayList<DownloadbleFile>();
        outputDatasets.add(outputDataset);
        Command command = new Command("MiXCR", "align");
        traceabilityData = new TraceabilityDataAwaitingValidation(inputDatasets,
                command, outputDatasets, new EntityID(creatorID),
                ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
    }
}

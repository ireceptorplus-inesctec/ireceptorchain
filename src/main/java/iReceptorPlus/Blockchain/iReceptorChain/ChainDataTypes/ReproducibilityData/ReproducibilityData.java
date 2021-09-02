package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class ReproducibilityData
{
    /**
     * The source from which the input dataset(s) can be fetched so that the processing may be performed.
     */
    @Property()
    ArrayList<DatasetSource> inputDatasets;

    /**
     * An executable script that allows to reproduce the processing made to the data.
     */
    @Property()
    ReproducibleScript script;

    /**
     * The source from which the output dataset(s) can be fetched to validate the output of the processing.
     */
    @Property()
    ArrayList<DatasetSource> outputDatasets;

    public ReproducibilityData(ArrayList<DatasetSource> inputDatasets, ReproducibleScript script, ArrayList<DatasetSource> outputDatasets)
    {
        this.inputDatasets = inputDatasets;
        this.script = script;
        this.outputDatasets = outputDatasets;
    }

    public ArrayList<DatasetSource> getInputDatasets()
    {
        return inputDatasets;
    }

    public ReproducibleScript getScript()
    {
        return script;
    }

    public ArrayList<DatasetSource> getOutputDatasets()
    {
        return outputDatasets;
    }
}

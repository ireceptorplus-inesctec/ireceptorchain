package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Objects;

public class ReproducibilityData
{
    /**
     * The source from which the input dataset(s) can be fetched so that the processing may be performed.
     */
    @Property()
    private final ArrayList<DownloadbleFile> inputDatasets;

    /**
     * An executable script that allows to reproduce the processing made to the data.
     */
    @Property()
    private final DownloadbleFile script;

    /**
     * The source from which the output dataset(s) can be fetched to validate the output of the processing.
     */
    @Property()
    private final ArrayList<DownloadbleFile> outputDatasets;

    public ReproducibilityData(ArrayList<DownloadbleFile> inputDatasets, DownloadbleFile script, ArrayList<DownloadbleFile> outputDatasets)
    {
        this.inputDatasets = inputDatasets;
        this.script = script;
        this.outputDatasets = outputDatasets;
    }

    public ArrayList<DownloadbleFile> getInputDatasets()
    {
        return inputDatasets;
    }

    public DownloadbleFile getScript()
    {
        return script;
    }

    public ArrayList<DownloadbleFile> getOutputDatasets()
    {
        return outputDatasets;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReproducibilityData that = (ReproducibilityData) o;
        return inputDatasets.equals(that.inputDatasets) && script.equals(that.script) && outputDatasets.equals(that.outputDatasets);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(inputDatasets, script, outputDatasets);
    }
}

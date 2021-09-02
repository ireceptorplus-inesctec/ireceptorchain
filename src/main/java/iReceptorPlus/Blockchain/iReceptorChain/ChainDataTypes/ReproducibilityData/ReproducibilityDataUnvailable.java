package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import java.util.ArrayList;

public class ReproducibilityDataUnvailable extends ReproducibilityData
{
    public ReproducibilityDataUnvailable(ArrayList<DatasetSource> inputDatasets, ReproducibleScript script, ArrayList<DatasetSource> outputDatasets)
    {
        super(inputDatasets, script, outputDatasets);
    }


    public ArrayList<DatasetSource> getInputDatasets()
    {
        return new ArrayList<>();
    }

    public ReproducibleScript getScript()
    {
        return new ReproducibleScriptUnavailable();
    }

    public ArrayList<DatasetSource> getOutputDatasets()
    {
        return new ArrayList<>();
    }
}

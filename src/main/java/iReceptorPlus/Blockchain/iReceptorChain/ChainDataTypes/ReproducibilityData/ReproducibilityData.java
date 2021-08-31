package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class ReproducibilityData
{
    /**
     * The source from which the input dataset(s) can be fetched so that the processing may be performed.
     */
    ArrayList<DatasetSource> inputDatasets;

    /**
     * An executable script that allows to reproduce the processing made to the data.
     */
    ReproducibleScript script;

    /**
     * The source from which the output dataset(s) can be fetched to validate the output of the processing.
     */
    ArrayList<DatasetSource> outputDatasets;


}

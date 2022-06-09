package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.*;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents the steps taken to make a data transformation.
 */
@DataType()
public class ProcessingDetails implements iReceptorChainDataType
{
    /**
     * The source from which the input dataset(s) can be fetched so that the processing may be performed.
     */
    @Property()
    private final ArrayList<DownloadbleFile> inputDatasets;

    /**
     * The command that should be run on the processing tool to execute the desired data processing.
     */
    @Property()
    private final Command command;

    /**
     * The source from which the output dataset(s) can be fetched to validate the output of the processing.
     */
    @Property()
    private final ArrayList<DownloadbleFile> outputDatasets;

    public ProcessingDetails(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                             @JsonProperty("command") final Command command,
                             @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets)
    {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
    }

    public ArrayList<DownloadbleFile> getInputDatasets()
    {
        return inputDatasets;
    }

    public Command getCommand()
    {
        return command;
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
        ProcessingDetails that = (ProcessingDetails) o;
        return inputDatasets.equals(that.inputDatasets) && command.equals(that.command) && outputDatasets.equals(that.outputDatasets);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(inputDatasets, command, outputDatasets);
    }
}

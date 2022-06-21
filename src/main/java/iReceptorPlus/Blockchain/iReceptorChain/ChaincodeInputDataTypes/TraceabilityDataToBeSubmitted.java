package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeInputDataTypes;

import com.owlike.genson.annotation.JsonProperty;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;

import java.util.ArrayList;

public class TraceabilityDataToBeSubmitted
{
    /**
     * The source from which the input dataset(s) can be fetched so that the processing may be performed.
     */
    private final ArrayList<DownloadbleFile> inputDatasets;

    /**
     * The command that should be run on the processing tool to execute the desired data processing.
     */
    private final Command command;

    /**
     * The source from which the output dataset(s) can be fetched to validate the output of the processing.
     */
    private final ArrayList<DownloadbleFile> outputDatasets;

    /**
     * The value of this traceability data that will be used to calculate rewards and penalties for the voters.
     * Optionally, the creator may decide to include an additional reward that will be split among the traceability data validators.
     * The double representing the reward will be available to be consulted even after the traceability data is registered as validated.
     */
    protected Double additionalValue;

    public TraceabilityDataToBeSubmitted(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                                         @JsonProperty("command") final Command command,
                                         @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets)
    {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.additionalValue = 0.0;
    }

    public TraceabilityDataToBeSubmitted(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                                         @JsonProperty("command") final Command command,
                                         @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets,
                                         @JsonProperty("additionalValue") final Double additionalValue)
    {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.additionalValue = additionalValue;
    }

    public ArrayList<DownloadbleFile> getInputDatasets() {
        return inputDatasets;
    }

    public Command getCommand() {
        return command;
    }

    public ArrayList<DownloadbleFile> getOutputDatasets() {
        return outputDatasets;
    }

    public Double getAdditionalValue() {
        return additionalValue;
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeInputDataTypes;

import com.owlike.genson.annotation.JsonProperty;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;

public class TraceabilityDataToBeSubmitted
{
    /**
     * This is an instance of the class ProcessingDetails which contains information regarding the steps taken to perform the data transformation.
     * These steps are necessary in order to check the veracity of the traceability information.
     */
    protected final ProcessingDetails processingDetails;

    /**
     * The value of this traceability data that will be used to calculate rewards and penalties for the voters.
     * Optionally, the creator may decide to include an additional reward that will be split among the traceability data validators.
     * The double representing the reward will be available to be consulted even after the traceability data is registered as validated.
     */
    protected Double additionalValue;

    public TraceabilityDataToBeSubmitted(@JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        this.processingDetails = processingDetails;
        this.additionalValue = 0.0;
    }

    public TraceabilityDataToBeSubmitted(@JsonProperty("processingDetails") final ProcessingDetails processingDetails,
                                         @JsonProperty("additionalValue") final Double additionalValue)
    {
        this.processingDetails = processingDetails;
        this.additionalValue = additionalValue;
    }

    public ProcessingDetails getProcessingDetails()
    {
        return processingDetails;
    }

    public Double getValue()
    {
        return additionalValue;
    }
}

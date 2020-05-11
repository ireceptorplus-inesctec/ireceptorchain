package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * This class represents an entry of traceability information.
 * This is the base class for traceability information.
 * Sub classes are used where there are necessary additional attributes, depending on the state of validation of the traceability information.
 * Please check the VotingStateMachine package for more information.
 */
@DataType()
public abstract class TraceabilityData implements iReceptorChainDataType
{
    /**
     * The hash value of the input dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    @Property()
    protected final String inputDatasetHashValue;

    /**
     * The hash value of the output dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    @Property()
    protected final String outputDatasetHashValue;

    /**
     * This is an instance of the class ProcessingDetails which contains information regarding the steps taken to perform the data transformation.
     * These steps are necessary in order to check the veracity of the traceability information.
     */
    @Property()
    protected final ProcessingDetails processingDetails;

    public TraceabilityData(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                            @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                            @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
    }

    public String getInputDatasetHashValue()
    {
        return inputDatasetHashValue;
    }

    public String getOutputDatasetHashValue()
    {
        return outputDatasetHashValue;
    }

    public ProcessingDetails getProcessingDetails()
    {
        return processingDetails;
    }

    /**
     * This method is called whenever a user votes yes for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    public abstract void registerYesVoteForValidity(EntityID entityID);

    /**
     * This method is called whenever a user votes no for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    public abstract void registerNoVoteForValidity(EntityID entityID);

    /**
     * Returns the number of approvers of the traceability information represented by this class.
     * @return the number of approvers of the traceability information represented by this class.
     */
    public abstract long getNumberOfApprovers();
}

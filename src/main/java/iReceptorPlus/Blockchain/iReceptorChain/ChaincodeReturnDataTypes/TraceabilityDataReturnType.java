package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ProcessingDetails;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.Objects;

@DataType()
public class TraceabilityDataReturnType extends ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data entry.
     */
    @Property()
    protected String uuid;

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

    /**
     * An instance of class EntityID containing information about the id of the entity that created the traceability data entry.
     */
    @Property()
    protected final EntityID creatorID;

    /**
     * An array of entities who have submitted a YES vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    protected ArrayList<EntityID> approvers;

    /**
     * An array of entities who have submitted a NO vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    protected ArrayList<EntityID> rejecters;

    /**
     * The value of this traceability data that will be used to calculate rewards and penalties for the voters.
     * Optionally, the creator may decide to include an additional reward that will be split among the traceability data validators.
     * The double representing the reward will be available to be consulted even after the traceability data is registered as validated.
     */
    @Property()
    protected Double value;

    public TraceabilityDataReturnType(@JsonProperty("uuid") String uuid,
                                      @JsonProperty("inputDatasetHashValue") String inputDatasetHashValue,
                                      @JsonProperty("outputDatasetHashValue") String outputDatasetHashValue,
                                      @JsonProperty("processingDetails") ProcessingDetails processingDetails,
                                      @JsonProperty("creatorID") EntityID creatorID,
                                      @JsonProperty("approvers") ArrayList<EntityID> approvers,
                                      @JsonProperty("rejecters") ArrayList<EntityID> rejecters,
                                      @JsonProperty("value") Double value)
    {
        this.uuid = uuid;
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
        this.approvers = approvers;
        this.rejecters = rejecters;
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityDataReturnType that = (TraceabilityDataReturnType) o;
        return uuid.equals(that.uuid) && inputDatasetHashValue.equals(that.inputDatasetHashValue) && outputDatasetHashValue.equals(that.outputDatasetHashValue) && processingDetails.equals(that.processingDetails) && creatorID.equals(that.creatorID) && approvers.equals(that.approvers) && rejecters.equals(that.rejecters) && value.equals(that.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, approvers, rejecters, value);
    }

    public String getUuid()
    {
        return uuid;
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

    public EntityID getCreatorID()
    {
        return creatorID;
    }

    public ArrayList<EntityID> getApprovers()
    {
        return approvers;
    }

    public ArrayList<EntityID> getRejecters()
    {
        return rejecters;
    }

    public Double getValue()
    {
        return value;
    }
}

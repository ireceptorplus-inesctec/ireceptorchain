package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.Objects;

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

    public TraceabilityData(String inputDatasetHashValue,
                            String outputDatasetHashValue,
                            ProcessingDetails processingDetails,
                            EntityID creatorID)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
    }

    public TraceabilityData(@JsonProperty("inputDatasetHashValue") String inputDatasetHashValue,
                            @JsonProperty("outputDatasetHashValue") String outputDatasetHashValue,
                            @JsonProperty("processingDetails") ProcessingDetails processingDetails,
                            @JsonProperty("creatorID") EntityID creatorID,
                            @JsonProperty("approvers") ArrayList<EntityID> approvers,
                            @JsonProperty("rejecters") ArrayList<EntityID> rejecters)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
        this.approvers = approvers;
        this.rejecters = rejecters;
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
    public long getNumberOfApprovers()
    {
        return approvers.size();
    }

    /**
     * Returns the number of rejecters of the traceability information represented by this class.
     * @return the number of rejecters of the traceability information represented by this class.
     */
    public long getNumberOfRejecters()
    {
        return rejecters.size();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityData that = (TraceabilityData) o;
        return inputDatasetHashValue.equals(that.inputDatasetHashValue) &&
                outputDatasetHashValue.equals(that.outputDatasetHashValue) &&
                processingDetails.equals(that.processingDetails) &&
                creatorID.equals(that.creatorID) &&
                approvers.equals(that.approvers) &&
                rejecters.equals(that.rejecters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, approvers, rejecters);
    }
}

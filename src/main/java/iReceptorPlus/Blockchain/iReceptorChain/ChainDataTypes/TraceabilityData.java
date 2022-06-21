package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents an entry of traceability information.
 * This is the base class for traceability information.
 * Sub classes are used where there are necessary additional attributes, depending on the state of validation of the traceability information.
 * Please check the VotingRoundStateMachine package for more information.
 */
@DataType()
public abstract class TraceabilityData implements iReceptorChainDataType
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
    @Property
    protected Double value;

    public TraceabilityData(ArrayList<DownloadbleFile> inputDatasets, Command command,
                            ArrayList<DownloadbleFile> outputDatasets, EntityID creatorID) {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.creatorID = creatorID;
    }

    public TraceabilityData(ArrayList<DownloadbleFile> inputDatasets, Command command,
                            ArrayList<DownloadbleFile> outputDatasets, EntityID creatorID,
                            Double value) {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.creatorID = creatorID;
        this.value = value;
    }

    public TraceabilityData(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                            @JsonProperty("command") final Command command,
                            @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets,
                            @JsonProperty("creatorID") EntityID creatorID,
                            @JsonProperty("approvers") ArrayList<EntityID> approvers,
                            @JsonProperty("rejecters") ArrayList<EntityID> rejecters,
                            @JsonProperty("value") Double value)
    {
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.creatorID = creatorID;
        this.approvers = approvers;
        this.rejecters = rejecters;
        this.value = value;
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

    public void setApprovers(ArrayList<EntityID> approvers) {
        this.approvers = approvers;
    }

    public void setRejecters(ArrayList<EntityID> rejecters) {
        this.rejecters = rejecters;
    }

    public void setValue(Double value) {
        this.value = value;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityData that = (TraceabilityData) o;
        return inputDatasets.equals(that.inputDatasets) && command.equals(that.command) && outputDatasets.equals(that.outputDatasets) && creatorID.equals(that.creatorID) && approvers.equals(that.approvers) && rejecters.equals(that.rejecters) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputDatasets, command, outputDatasets, creatorID, approvers, rejecters, value);
    }
}

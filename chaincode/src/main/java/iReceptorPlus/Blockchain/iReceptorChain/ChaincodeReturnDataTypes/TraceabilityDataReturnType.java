package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
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
    @Property()
    protected Double value;

    public TraceabilityDataReturnType(@JsonProperty("uuid") String uuid,
                                      @JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                                      @JsonProperty("command") final Command command,
                                      @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets,
                                      @JsonProperty("creatorID") EntityID creatorID,
                                      @JsonProperty("approvers") ArrayList<EntityID> approvers,
                                      @JsonProperty("rejecters") ArrayList<EntityID> rejecters,
                                      @JsonProperty("value") Double value)
    {
        this.uuid = uuid;
        this.inputDatasets = inputDatasets;
        this.command = command;
        this.outputDatasets = outputDatasets;
        this.creatorID = creatorID;
        this.approvers = approvers;
        this.rejecters = rejecters;
        this.value = value;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public EntityID getCreatorID() {
        return creatorID;
    }

    public ArrayList<EntityID> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<EntityID> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<EntityID> getRejecters() {
        return rejecters;
    }

    public void setRejecters(ArrayList<EntityID> rejecters) {
        this.rejecters = rejecters;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityDataReturnType that = (TraceabilityDataReturnType) o;
        return uuid.equals(that.uuid) && inputDatasets.equals(that.inputDatasets) && command.equals(that.command) && outputDatasets.equals(that.outputDatasets) && creatorID.equals(that.creatorID) && approvers.equals(that.approvers) && rejecters.equals(that.rejecters) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, inputDatasets, command, outputDatasets, creatorID, approvers, rejecters, value);
    }
}

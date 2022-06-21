package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after being validated and registered on the blockchain as valid traceability information.
 */
@DataType()
public class TraceabilityDataValidated extends TraceabilityData
{

    /**
     * An array of entities which have voted for the validity of the traceability information after its voting round had endeds.
     * (They have submitted a yes vote when the traceability information after it was already registered on the blockchain as valid.)
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<EntityID> corroborators;

    public TraceabilityDataValidated(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                                     @JsonProperty("command") final Command command,
                                     @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets,
                                     @JsonProperty("creatorID") final EntityID creatorID,
                                     @JsonProperty("approvers") final ArrayList<EntityID> approvers,
                                     @JsonProperty("rejecters") final ArrayList<EntityID> rejecters,
                                     @JsonProperty("value") Double value)
    {
        super(inputDatasets, command, outputDatasets, creatorID, approvers, rejecters, value);
        corroborators = new ArrayList<>();
    }

    @Override
    public void registerYesVoteForValidity(EntityID entityID)
    {
        corroborators.add(entityID);
    }

    @Override
    public void registerNoVoteForValidity(EntityID entityID)
    {
        //TODO

    }

    public ArrayList<EntityID> getCorroborators()
    {
        return corroborators;
    }
}

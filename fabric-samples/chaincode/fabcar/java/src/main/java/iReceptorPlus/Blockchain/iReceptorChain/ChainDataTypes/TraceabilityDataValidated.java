package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
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
     * An array of entities which have voted for the validity of the traceability information during the its voting round.
     * (They have submitted a yes vote when the traceability information was in a voting round.)
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<EntityID> validators;

    /**
     * An array of entities which have voted for the validity of the traceability information after its voting round had endeds.
     * (They have submitted a yes vote when the traceability information after it was already registered on the blockchain as valid.)
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<EntityID> corroborators;

    public TraceabilityDataValidated(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                     @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                     @JsonProperty("processingDetails") final ProcessingDetails processingDetails,
                                     @JsonProperty("validators") final ArrayList<EntityID> validators)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails);
        this.validators = validators;
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

    /**
     * Returns the number of approvers of the traceability information represented by this class.
     * @return the number of approvers of the traceability information represented by this class.
     */
    @Override
    public long getNumberOfApprovers()
    {
        return validators.size();
    }

    public ArrayList<EntityID> getValidators()
    {
        return validators;
    }

    public ArrayList<EntityID> getCorroborators()
    {
        return corroborators;
    }
}

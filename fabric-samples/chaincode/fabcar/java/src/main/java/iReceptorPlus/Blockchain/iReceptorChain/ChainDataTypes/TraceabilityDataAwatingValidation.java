package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after the entry was first created and submitted to the blockchain.
 */
@DataType()
public class TraceabilityDataAwatingValidation extends TraceabilityData
{

    /**
     * An array of entities who have submitted a YES vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<Entity> approvers;

    /**
     * An array of entities who have submitted a NO vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<Entity> rejecters;


    public TraceabilityDataAwatingValidation(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                             @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                             @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails);
        approvers = new ArrayList<>();
        rejecters = new ArrayList<>();
    }

    public ArrayList<Entity> getApprovers()
    {
        return approvers;
    }

    public ArrayList<Entity> getRejecters()
    {
        return rejecters;
    }

    /**
     * Registers the entity passed as parameter as an approver of the traceability information.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    @Override
    public void registerYesVoteForValidity(Entity entity)
    {
        approvers.add(entity);
    }

    /**
     * Registers the entity passed as parameter as a rejecter of the traceability information.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    @Override
    public void registerNoVoteForValidity(Entity entity)
    {
        rejecters.add(entity);
    }

    /**
     * Returns the number of approvers of the traceability information represented by this class.
     * @return the number of approvers of the traceability information represented by this class.
     */
    @Override
    public long getNumberOfApprovers()
    {
        return approvers.size();
    }
}
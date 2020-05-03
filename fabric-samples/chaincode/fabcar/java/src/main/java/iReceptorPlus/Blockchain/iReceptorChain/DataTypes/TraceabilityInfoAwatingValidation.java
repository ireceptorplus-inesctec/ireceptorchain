package iReceptorPlus.Blockchain.iReceptorChain.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityInfo, representing traceability information in a specific state: after the entry was first created and submitted to the blockchain.
 */
@DataType()
public class TraceabilityInfoAwatingValidation extends TraceabilityInfo
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


    public TraceabilityInfoAwatingValidation(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                     @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                     @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails);
        approvers = new ArrayList<>();
        rejecters = new ArrayList<>();
    }

    /**
     * Registers the entity passed as parameter as an approver of the traceability information.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    @Override
    public void registerYesVoteForValidity(Entity entity)
    {
        System.err.println("*****TraceabilityInfoAwatingValidation*******");
        Logger.getLogger("TraceabilityInfoAwatingValidation").debug("stuff");

        //TODO
    }

    /**
     * Registers the entity passed as parameter as a rejecter of the traceability information.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    @Override
    public void registerNoVoteForValidity(Entity entity)
    {
        //TODO

    }
}

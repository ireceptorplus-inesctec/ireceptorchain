package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityInfo, representing a traceability information in a specific state: after being validated and registered on the blockchain as valid traceability information.
 */
public class TraceabilityInfoValidated extends TraceabilityInfo
{

    /**
     * An array of entities which have voted for the validity of the traceability information.
     * (They have submitted a yes vote when the traceability information was in a voting round.)
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    @Property()
    private final ArrayList<Entity> validators;

    public TraceabilityInfoValidated(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                     @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                     @JsonProperty("processingDetails") final ProcessingDetails processingDetails,
                                     @JsonProperty("validators") final ArrayList<Entity> validators)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails);
        this.validators = validators;
    }
}

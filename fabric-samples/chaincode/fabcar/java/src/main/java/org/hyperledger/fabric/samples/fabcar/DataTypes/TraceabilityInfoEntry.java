package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TraceabilityInfoEntry
{
    @Property()
    private final String inputDatasetHashValue;

    @Property()
    private final String outputDatasetHashValue;

    @Property()
    private final ProcessingDetails processingDetails;

    @Property()
    private final Validator[] validators;

    public TraceabilityInfoEntry(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                 @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                 @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        validators = new Validator[0];
    }
}

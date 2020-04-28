package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

public class ValidatedTraceabilityInfo extends TraceabilityInfo
{

    @Property()
    private final ArrayList<Validator> validators;

    public ValidatedTraceabilityInfo(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                            @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                            @JsonProperty("processingDetails") final ProcessingDetails processingDetails,
                            @JsonProperty("validators") final ArrayList<Validator> validators)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails);
        this.validators = validators;
    }
}

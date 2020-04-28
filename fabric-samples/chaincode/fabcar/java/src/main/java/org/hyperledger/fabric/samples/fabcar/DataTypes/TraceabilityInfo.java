package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

@DataType()
public class TraceabilityInfo
{
    @Property()
    protected final String inputDatasetHashValue;

    @Property()
    protected final String outputDatasetHashValue;

    @Property()
    protected final ProcessingDetails processingDetails;

    public TraceabilityInfo(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                            @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                            @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
    }
}

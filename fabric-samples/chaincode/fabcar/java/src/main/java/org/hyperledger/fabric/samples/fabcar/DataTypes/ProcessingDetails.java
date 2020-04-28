package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class ProcessingDetails
{
    @Property()
    private final String softwareId;

    @Property()
    private final String softwareVersion;

    @Property()
    private final String softwareBinaryExecutable;

    @Property()
    private final String softwareConfigParams;


    public ProcessingDetails(@JsonProperty("softwareId") final String softwareId,
                             @JsonProperty("softwareVersion") final String softwareVersion,
                             @JsonProperty("softwareBinaryExecutable") final String softwareBinaryExecutable,
                             @JsonProperty("softwareConfigParams") final String softwareConfigParams)
    {
        this.softwareId = softwareId;
        this.softwareVersion = softwareVersion;
        this.softwareBinaryExecutable = softwareBinaryExecutable;
        this.softwareConfigParams = softwareConfigParams;
    }
}

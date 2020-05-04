package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * This class represents the steps taken to make a data transformation.
 */
@DataType()
public class ProcessingDetails
{
    /**
     * An unique identifier of the software used to perform the data transformation.
     * For the nodes to be able to validate the traceability information, a corresponding and valid entry in the software list config file must exist.
     */
    @Property()
    private final String softwareId;

    /**
     * The version of the software used to perform the data transformation.
     */
    @Property()
    private final String softwareVersion;

    /**
     * The hash value of the binary executable used to perform the data transformation.
     * This is used to validate the integrity of the binary used, in case a verification of the information is desired.
     * Only by trusting the executable can the traceability information be trusted.
     */
    @Property()
    private final String softwareBinaryExecutableHashValue;

    /**
     * The configuration parameters of the software used to perform the data transformation.
     * This should be a string containing the command line arguments, ready to be passed to the executable binary file.
     */
    @Property()
    private final String softwareConfigParams;


    public ProcessingDetails(@JsonProperty("softwareId") final String softwareId,
                             @JsonProperty("softwareVersion") final String softwareVersion,
                             @JsonProperty("softwareBinaryExecutableHashValue") final String softwareBinaryExecutableHashValue,
                             @JsonProperty("softwareConfigParams") final String softwareConfigParams)
    {
        this.softwareId = softwareId;
        this.softwareVersion = softwareVersion;
        this.softwareBinaryExecutableHashValue = softwareBinaryExecutableHashValue;
        this.softwareConfigParams = softwareConfigParams;
    }
}

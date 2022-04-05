package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.ReproducibilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.ReproducibilityDataUnvailable;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.ReproducibleScript;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

/**
 * This class represents the steps taken to make a data transformation.
 */
@DataType()
public class ProcessingDetails implements iReceptorChainDataType
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

    /**
     * An instance of class ReproducibilityData containing the necessary data to reproduce the processing.
     */
    @Property()
    private final ReproducibilityData reproducibilityData;

    public String getSoftwareId()
    {
        return softwareId;
    }

    public String getSoftwareVersion()
    {
        return softwareVersion;
    }

    public String getSoftwareBinaryExecutableHashValue()
    {
        return softwareBinaryExecutableHashValue;
    }

    public String getSoftwareConfigParams()
    {
        return softwareConfigParams;
    }

    public ReproducibilityData getReproducibilityData()
    {
        return reproducibilityData;
    }

    public ProcessingDetails(@JsonProperty("softwareId") final String softwareId,
                             @JsonProperty("softwareVersion") final String softwareVersion,
                             @JsonProperty("softwareBinaryExecutableHashValue") final String softwareBinaryExecutableHashValue,
                             @JsonProperty("softwareConfigParams") final String softwareConfigParams)
    {
        this.softwareId = softwareId;
        this.softwareVersion = softwareVersion;
        this.softwareBinaryExecutableHashValue = softwareBinaryExecutableHashValue;
        this.softwareConfigParams = softwareConfigParams;
        this.reproducibilityData = new ReproducibilityDataUnvailable();
    }

    public ProcessingDetails(@JsonProperty("softwareId") final String softwareId,
                             @JsonProperty("softwareVersion") final String softwareVersion,
                             @JsonProperty("softwareBinaryExecutableHashValue") final String softwareBinaryExecutableHashValue,
                             @JsonProperty("softwareConfigParams") final String softwareConfigParams,
                             @JsonProperty("reproducibilityData") final ReproducibilityData reproducibilityData)
    {
        this.softwareId = softwareId;
        this.softwareVersion = softwareVersion;
        this.softwareBinaryExecutableHashValue = softwareBinaryExecutableHashValue;
        this.softwareConfigParams = softwareConfigParams;
        this.reproducibilityData = reproducibilityData;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingDetails that = (ProcessingDetails) o;
        return softwareId.equals(that.softwareId) &&
                softwareVersion.equals(that.softwareVersion) &&
                softwareBinaryExecutableHashValue.equals(that.softwareBinaryExecutableHashValue) &&
                softwareConfigParams.equals(that.softwareConfigParams);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(softwareId, softwareVersion, softwareBinaryExecutableHashValue, softwareConfigParams);
    }
}

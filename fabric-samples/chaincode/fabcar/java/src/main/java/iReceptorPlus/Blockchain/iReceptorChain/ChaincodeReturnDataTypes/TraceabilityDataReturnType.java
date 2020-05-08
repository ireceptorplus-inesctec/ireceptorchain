package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import org.hyperledger.fabric.contract.annotation.Property;

public class TraceabilityDataReturnType implements ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    String uuid;

    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityData traceabilityData;

    public TraceabilityDataReturnType(@JsonProperty("inputDatasetHashValue") final String uuid,
                                      @JsonProperty("outputDatasetHashValue") final TraceabilityData traceabilityData)
    {
        this.uuid = uuid;
        this.traceabilityData = traceabilityData;
    }
}

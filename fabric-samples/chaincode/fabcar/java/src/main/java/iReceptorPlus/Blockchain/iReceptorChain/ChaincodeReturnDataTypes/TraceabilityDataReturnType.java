package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import org.hyperledger.fabric.contract.annotation.Property;

public abstract class TraceabilityDataReturnType implements ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    String uuid;

    public TraceabilityDataReturnType(@JsonProperty("inputDatasetHashValue") final String uuid)
    {
        this.uuid = uuid;
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

public abstract class ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    protected String uuid;

    public ChaincodeReturnDataType(@JsonProperty("uuid") final String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }
}

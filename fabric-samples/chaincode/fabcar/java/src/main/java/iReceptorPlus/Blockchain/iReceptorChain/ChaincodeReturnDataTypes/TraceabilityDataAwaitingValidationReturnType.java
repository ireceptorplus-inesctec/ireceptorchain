package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class TraceabilityDataAwaitingValidationReturnType extends TraceabilityDataReturnType
{

    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    protected String uuid;
    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataAwaitingValidation traceabilityDataAwaitingValidationData;

    public TraceabilityDataAwaitingValidation getTraceabilityDataAwaitingValidationData()
    {
        return traceabilityDataAwaitingValidationData;
    }

    public TraceabilityDataAwaitingValidationReturnType(@JsonProperty("uuid") final String uuid,
                                                        @JsonProperty("traceabilityDataAwaitingValidationData") final TraceabilityDataAwaitingValidation traceabilityDataAwaitingValidationData)
    {
        this.uuid = uuid;
        this.traceabilityDataAwaitingValidationData = traceabilityDataAwaitingValidationData;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityDataAwaitingValidationReturnType that = (TraceabilityDataAwaitingValidationReturnType) o;
        return uuid.equals(that.uuid) &&
                traceabilityDataAwaitingValidationData.equals(that.traceabilityDataAwaitingValidationData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, traceabilityDataAwaitingValidationData);
    }

    public String getUuid()
    {
        return uuid;
    }
}

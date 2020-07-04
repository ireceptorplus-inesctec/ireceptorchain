package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class TraceabilityDataAwatingValidationReturnType extends TraceabilityDataReturnType
{

    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataAwaitingValidation traceabilityDataAwatingValidationData;

    public TraceabilityDataAwaitingValidation getTraceabilityDataAwatingValidationData()
    {
        return traceabilityDataAwatingValidationData;
    }

    public TraceabilityDataAwatingValidationReturnType(@JsonProperty("uuid") final String uuid,
                                                       @JsonProperty("traceabilityDataAwatingValidationData") final TraceabilityDataAwaitingValidation traceabilityDataAwatingValidationData)
    {
        super(uuid);
        this.traceabilityDataAwatingValidationData = traceabilityDataAwatingValidationData;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityDataAwatingValidationReturnType that = (TraceabilityDataAwatingValidationReturnType) o;
        return uuid.equals(that.uuid) &&
                traceabilityDataAwatingValidationData.equals(that.traceabilityDataAwatingValidationData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, traceabilityDataAwatingValidationData);
    }

}

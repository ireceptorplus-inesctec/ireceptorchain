package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TraceabilityDataAwatingValidationReturnType extends TraceabilityDataReturnType
{
    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData;

    public TraceabilityDataAwatingValidationReturnType(@JsonProperty("uuid") final String uuid,
                                                       @JsonProperty("traceabilityDataAwatingValidationData") final TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData)
    {
        super(uuid);
        this.traceabilityDataAwatingValidationData = traceabilityDataAwatingValidationData;
    }
}

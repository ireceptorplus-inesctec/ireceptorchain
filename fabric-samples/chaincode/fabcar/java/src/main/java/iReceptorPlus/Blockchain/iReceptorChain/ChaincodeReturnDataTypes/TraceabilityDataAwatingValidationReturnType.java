package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TraceabilityDataAwatingValidationReturnType
{
    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    private final String uuid;

    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    private final String traceabilityDataAwatingValidationData;

    public TraceabilityDataAwatingValidationReturnType(@JsonProperty("uuid") final String uuid,
                                                       @JsonProperty("traceabilityDataAwatingValidationData") final String traceabilityDataAwatingValidationData)
    {
        this.uuid = uuid;
        this.traceabilityDataAwatingValidationData = traceabilityDataAwatingValidationData;
    }
    public TraceabilityDataAwatingValidationReturnType(@JsonProperty("uuid") final String uuid,
                                                       @JsonProperty("traceabilityDataAwatingValidationData") final TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData)
    {
        this.uuid ="";
        this.traceabilityDataAwatingValidationData = "";
    }

}

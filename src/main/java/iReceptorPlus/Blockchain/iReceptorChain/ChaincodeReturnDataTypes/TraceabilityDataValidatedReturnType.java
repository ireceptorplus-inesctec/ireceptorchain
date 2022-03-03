package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TraceabilityDataValidatedReturnType extends TraceabilityDataReturnType
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
    TraceabilityDataValidated traceabilityDataValidatedData;

    public String getUuid()
    {
        return uuid;
    }

    public TraceabilityDataValidated getTraceabilityDataValidatedData()
    {
        return traceabilityDataValidatedData;
    }

    public TraceabilityDataValidatedReturnType(@JsonProperty("uuid") final String uuid,
                                               @JsonProperty("traceabilityDataValidatedData") final TraceabilityDataValidated traceabilityDataValidatedData)
    {
        this.uuid = uuid;
        this.traceabilityDataValidatedData = traceabilityDataValidatedData;
    }
}
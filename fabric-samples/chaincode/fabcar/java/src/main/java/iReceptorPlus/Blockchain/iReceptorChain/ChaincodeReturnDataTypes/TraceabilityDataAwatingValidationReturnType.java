package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import org.hyperledger.fabric.contract.annotation.Property;

public class TraceabilityDataAwatingValidationReturnType extends TraceabilityDataReturnType
{
    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData;

    public TraceabilityDataAwatingValidationReturnType(String uuid, TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData)
    {
        super(uuid);
        this.traceabilityDataAwatingValidationData = traceabilityDataAwatingValidationData;
    }
}

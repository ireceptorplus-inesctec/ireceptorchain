package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import org.hyperledger.fabric.contract.annotation.Property;

public class TraceabilityDataValidatedReturnType extends TraceabilityDataReturnType
{
    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData;

    public TraceabilityDataValidatedReturnType(String uuid, TraceabilityDataAwatingValidation traceabilityDataAwatingValidationData)
    {
        super(uuid);
        this.traceabilityDataAwatingValidationData = traceabilityDataAwatingValidationData;
    }
}

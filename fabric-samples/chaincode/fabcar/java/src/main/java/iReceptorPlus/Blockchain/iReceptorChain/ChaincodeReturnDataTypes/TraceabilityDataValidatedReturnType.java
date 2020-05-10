package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TraceabilityDataValidatedReturnType extends TraceabilityDataReturnType
{
    /**
     * The traceability data just as it is stored on the blockchain.
     */
    @Property()
    TraceabilityDataValidated traceabilityDataValidatedData;

    public TraceabilityDataValidatedReturnType(String uuid, TraceabilityDataValidated traceabilityDataValidatedData)
    {
        super(uuid);
        this.traceabilityDataValidatedData = traceabilityDataValidatedData;
    }
}

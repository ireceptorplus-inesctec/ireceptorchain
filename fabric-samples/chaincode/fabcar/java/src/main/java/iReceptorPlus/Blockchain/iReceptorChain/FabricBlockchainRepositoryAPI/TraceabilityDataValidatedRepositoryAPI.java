package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataAwatingValidation;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import org.hyperledger.fabric.contract.Context;

public class TraceabilityDataValidatedRepositoryAPI extends TraceabilityDataRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     * @param objectTypeIdentifier A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     */
    public TraceabilityDataValidatedRepositoryAPI(Context ctx, String objectTypeIdentifier)
    {
        super(ctx, objectTypeIdentifier);
    }

    @Override
    protected iReceptorChainDataType deserializeData(String serializedData)
    {
        return genson.deserialize(serializedData, TraceabilityDataValidated.class);
    }
}

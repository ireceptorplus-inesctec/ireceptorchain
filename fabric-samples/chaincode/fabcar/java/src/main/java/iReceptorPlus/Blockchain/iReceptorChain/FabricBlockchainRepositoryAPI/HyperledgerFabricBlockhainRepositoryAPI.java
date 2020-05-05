package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import org.hyperledger.fabric.contract.Context;

import java.util.UUID;

/**
 * This class is an API that handles all calls to the hyperledger functions that are necessary for CRUD operations.
 * It is used to abstract database related calls to the hyperledger API from the application logic.
 * The class receives the blockchain context on which it will perform the operations and can then be called to perform CRUD operations on that same context without the need for interacting directly with hyperledger fabric's methods.
 * The class is abstract and defines the methods for CRUD operations.
 * Subclasses will implement the specific logic necessary to handle these operations for each one of the data types but generalization is maintained whenever possible.
 */
public abstract class HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * The blockchain context in which this class will perform the required tasks.
     */
    protected Context ctx;

    /**
     * The type of object that will be serialized in order to be saved in the database.
     * Subclasses should set this attribute accordingly in order for this class to implement the serializing logic necessary for the CRUD operations.
     */
    Class objectType;

    /**
     * A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     * It will be used to build the key for storing the object on the blockchain.
     */
    String objectTypeIdentifier;

    Genson genson = new Genson();

    /**
     * Constructor for this class.
     *
     * @param ctx        The blockchain context in which this class will perform the required tasks.
     * @param objectType The type of object that will be serialized in order to be saved in the database.
     */
    public HyperledgerFabricBlockhainRepositoryAPI(Context ctx, Class objectType)
    {
        this.ctx = ctx;
        this.objectType = objectType;
    }

    public String create(iReceptorChainDataType data)
    {
        UUID newUUID = UUID.randomUUID();
        putEntryToDB(newUUID.toString(), data);

        return newUUID.toString();
    }

    private void putEntryToDB(String key, iReceptorChainDataType data)
    {
        String serializedData = genson.serialize(data);
        ctx.getStub().putStringState(objectTypeIdentifier + "-" + key, serializedData);
    }

    private iReceptorChainDataType getDataTypeFromDB(String key) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        String serializedData = ctx.getStub().getStringState(key);
        if (serializedData.isEmpty())
            throw new ObjectWithGivenKeyNotFoundOnBlockchainDB("The object referenced does not exist on the blockchain database", key);

        return deserializeData(serializedData);
    }

    protected abstract iReceptorChainDataType deserializeData(String serializedData);

    public iReceptorChainDataType read(String key) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        iReceptorChainDataType object = getDataTypeFromDB(key);

        return object;
    }

    public iReceptorChainDataTypeInfo updateTraceabilityInfo(iReceptorChainDataTypeInfo traceabilityDataInfo)
    {
        putEntryToDB(traceabilityDataInfo.getKey(), traceabilityDataInfo.getData());

        return traceabilityDataInfo;
    }


    public void switchTraceabilityInfoStateFromAwaitingValidationToValidated(TraceabilityDataInfo newTraceabilityDataInfo)
    {
        //TODO delete older one and insert new one
    }
}

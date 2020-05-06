package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
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
 * Subclasses implement the specific logic necessary to handle these operations for each one of the data types but generalization is maintained whenever possible.
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

    /**
     * Implements create operations for the data type.
     * @param data An instance of a subclass of iReceptorChainDataType containing the data to be saved on the blockchain.
     * @return The key of the newly created entry on the blockchain.
     */
    public String create(String newUUID, iReceptorChainDataType data) throws GivenIdIsAlreadyAssignedToAnotherObject
    {
        String key = objectTypeIdentifier + "-" + newUUID;
        try
        {
            iReceptorChainDataType stringState = getDataTypeFromDB(key);
            throw new GivenIdIsAlreadyAssignedToAnotherObject("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.", newUUID);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            putEntryToDB(key, data);
            return key;
        }
    }

    /**
     * Auxiliary method used on the create and update operations. This methods puts (create or update) an entry on the blockchain, represented by the parameter data.
     * @param key The key of the object to be put. In case it is a create operation, it is the new key (id) of the entry. In case it is an update operation, it is the id that the entry being updated currently has on the blockchain.
     * @param data An instance of a subclass of iReceptorChainDataType containing the data to be created or updated on the blockchain.
     */
    private void putEntryToDB(String key, iReceptorChainDataType data)
    {
        String serializedData = genson.serialize(data);
        ctx.getStub().putStringState(key, serializedData);
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

    public iReceptorChainDataTypeInfo update(iReceptorChainDataTypeInfo traceabilityDataInfo)
    {
        putEntryToDB(traceabilityDataInfo.getKey(), traceabilityDataInfo.getData());

        return traceabilityDataInfo;
    }

}

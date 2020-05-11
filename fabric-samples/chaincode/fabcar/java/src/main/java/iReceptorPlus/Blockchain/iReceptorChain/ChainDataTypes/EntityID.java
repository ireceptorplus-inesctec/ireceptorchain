package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * This class represents the identifier of an entity.
 * It is used to reference an entity from other data type classes.
 * The information about an entity can be easily retrieved from the blockchain, given its ID.
 */
@DataType()
public class EntityID implements iReceptorChainDataType
{
    /**
     * A string representing the id of the entity.
     */
    @Property()
    private final String id;

    public String getId()
    {
        return id;
    }

    public EntityID(String id)
    {
        this.id = id;
    }
}

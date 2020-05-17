package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

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

    public EntityID(@JsonProperty("id") final String id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityID entityID = (EntityID) o;
        return id.equals(entityID.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import org.hyperledger.fabric.contract.annotation.Property;

public class EntityDataReturnType extends ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data.
     */
    @Property()
    protected String uuid;
    EntityData entityData;

    public EntityDataReturnType(@JsonProperty("uuid") final String uuid,
                                @JsonProperty("entityData") final EntityData entityData)
    {
        this.uuid = uuid;
        this.entityData = entityData;
    }

    public EntityData getEntityData()
    {
        return entityData;
    }

    public String getUuid()
    {
        return uuid;
    }
}

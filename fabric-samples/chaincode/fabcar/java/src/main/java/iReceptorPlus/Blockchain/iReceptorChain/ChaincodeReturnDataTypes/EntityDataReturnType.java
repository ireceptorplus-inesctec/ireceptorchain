package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;

public class EntityDataReturnType extends ChaincodeReturnDataType
{
    EntityData entityData;

    public EntityDataReturnType(@JsonProperty("uuid") final String uuid,
                                @JsonProperty("entityData") final EntityData entityData)
    {
        super(uuid);
        this.entityData = entityData;
    }
}

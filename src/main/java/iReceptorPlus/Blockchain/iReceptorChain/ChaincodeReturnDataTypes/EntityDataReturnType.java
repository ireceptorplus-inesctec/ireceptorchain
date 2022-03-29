package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityData;
import org.hyperledger.fabric.contract.annotation.Property;

public class EntityDataReturnType extends ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the Entity's data.
     */
    @Property()
    protected String uuid;

    /**
     * An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     * This contains the its certificate, an id, an msp id (id of the organization it belongs to) and may contain additional attributes created upon creation of the peers' certificate.
     */
    @Property()
    private String id;

    /**
     * This is a counter for the reputation of the entity.
     * This is used as an incentive for validation mechanism.
     * Each time an entity submits a vote decided as valid for a traceability data entry, it earns reputation.
     * If an entity creates fake traceability entries or makes false votes for other traceability data entries, it is highly penalized.
     */
    @Property()
    private Double reputation;

    /**
     * This is a counter for the reputation of the entity that is currently at stake.
     * Reputation is put at stake when the entity votes for a traceability data entry that is still awaiting validation.
     * This reputation may be lost if the entity is decided to be lying (by majority consensus).
     */
    @Property()
    private final Double reputationAtStake;

    public EntityDataReturnType(String id, Double reputation, Double reputationAtStake)
    {
        this.id = id;
        this.reputation = reputation;
        this.reputationAtStake = reputationAtStake;
    }


}

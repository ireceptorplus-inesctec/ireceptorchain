package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * This class represents an entity.
 * Is used to store information about the entities who have validated a traceability information entry.
 */
@DataType()
public class EntityData implements iReceptorChainDataType
{
    /**
     * An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     * This contains the its certificate, an id, an msp id (id of the organization it belongs to) and may contain additional attributes created upon creation of the peers' certificate.
     */
    @Property()
    private ClientIdentity clientIdentity;

    /**
     * This is a counter for the reputation of the entity.
     * This is used as an incentive for validation mechanism.
     * Each time an entity submits a vote decided as valid for a traceability data entry, it earns reputation.
     * If an entity creates fake traceability entries or makes false votes for other traceability data entries, it is highly penalized.
     */
    @Property()
    private Long reputation;

    /**
     * This is a counter for the reputation of the entity that is currently at stake.
     * Reputation is put at stake when the entity votes for a traceability data entry that is still awating validation.
     * This reputation may be lost if the entity is decided to be lying (by majority consensus).
     */
    @Property()
    private final Long reputationAtStake;

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }

    public Long getReputation()
    {
        return reputation;
    }

    public Long getReputationAtStake()
    {
        return reputationAtStake;
    }

    /**
     * Constructor used to create a new EntityData entry on the blockchain database.
     * Initializes the reputation counter to 0.
     * @param clientIdentity An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     */
    public EntityData(ClientIdentity clientIdentity)
    {
        this.clientIdentity = clientIdentity;
        this.reputation = new Long(0);
        this.reputationAtStake = new Long(0);
    }

    /**
     * Constructor used to create a representation of the entity's data that is already stored in the blockchain database.
     * It receives all attributes of an entity.
     * @param clientIdentity An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     * @param reputation The current reputation of the entity.
     * @param reputationAtStake The current amount of reputation of the entity that is at stake.
     */
    public EntityData(ClientIdentity clientIdentity, Long reputation, Long reputationAtStake)
    {
        this.clientIdentity = clientIdentity;
        this.reputation = reputation;
        this.reputationAtStake = reputationAtStake;
    }
}

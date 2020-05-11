package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import jdk.nashorn.internal.objects.annotations.Property;
import org.hyperledger.fabric.contract.ClientIdentity;

/**
 * This class represents an entity.
 * Is used to store information about the entities who have validated a traceability information entry.
 */
public class EntityData
{
    /**
     * An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     * This contains the its certificate, an id, an msp id (id of the organization it belongs to) and may contain additional attributes created upon creation of the peers' certificate.
     */
    @Property()
    private final ClientIdentity clientIdentity;

    /**
     * This is a counter for the reputation of the entity.
     * This is used as an incentive for validation mechanism.
     * Each time an entity submits a vote decided as valid for a traceability data entry, it earns reputation.
     * If an entity creates fake traceability entries or makes false votes for other traceability data entries, it is highly penalized.
     */
    @Property()
    private final Long reputation;

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }

    public Long getReputation()
    {
        return reputation;
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
    }
    /**
     * Constructor used to create a representation of the entity's data that is already stored in the blockchain database.
     * It receives all attributes of an entity.
     * @param clientIdentity An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     */
    public EntityData(ClientIdentity clientIdentity, Long reputation)
    {
        this.clientIdentity = clientIdentity;
        this.reputation = reputation;
    }
}

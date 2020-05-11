package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.annotation.DataType;

/**
 * This class represents an entity.
 * Is used to store information about the entities who have validated a traceability information entry.
 */
@DataType()
public class Entity implements iReceptorChainDataType
{
    /**
     * An instance of class ClientIdentity that hyperledger fabric uses to represent the identity of a client (peer).
     * This contains the its certificate, an id, an msp id (id of the organization it belongs to) and may contain additional attributes created upon creation of the peers' certificate.
     */
    ClientIdentity clientIdentity;

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }
}

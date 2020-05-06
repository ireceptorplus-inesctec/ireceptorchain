package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions;

public class ObjectWithGivenKeyNotFoundOnBlockchainDB extends ReferenceToIdException
{
    /**
     * The id used to reference the traceability information.
     */
    String id;

    public ObjectWithGivenKeyNotFoundOnBlockchainDB(String message, String id)
    {
        super(message);
        this.id = id;
    }

    /**
     * Getter to the id object.
     * @return the id object.
     */
    public String getId()
    {
        return id;
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions;

public class ObjectWithGivenKeyNotFoundOnBlockchainDB extends ReferenceToIdException
{

    public ObjectWithGivenKeyNotFoundOnBlockchainDB(String message, String id)
    {
        super(message, id);
    }

}

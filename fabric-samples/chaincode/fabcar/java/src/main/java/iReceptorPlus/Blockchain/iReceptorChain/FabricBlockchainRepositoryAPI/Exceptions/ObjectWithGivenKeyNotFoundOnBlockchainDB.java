package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions;

public class ObjectWithGivenKeyNotFoundOnBlockchainDB extends ReferenceToIdException
{

    public ObjectWithGivenKeyNotFoundOnBlockchainDB(String message, String id)
    {
        super(message, id);
    }

}

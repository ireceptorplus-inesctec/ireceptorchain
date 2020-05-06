package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions;

public abstract class ReferenceToIdException extends HyperledgerFabricBlockhainRepositoryAPIException
{
    /**
     * Constructor for this class that receives the message as parameter.
     * Should be called by the subclasses and the message should be customized by them (either built or received as their own constructor parameter).
     *
     * @param message The string message that will be set as the message for the exception that occurred.
     */
    public ReferenceToIdException(String message)
    {
        super(message);
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions;

public abstract class ReferenceToIdException extends HyperledgerFabricBlockhainRepositoryAPIException
{
    /**
     * The id used to reference the traceability information.
     */
    String id;

    /**
     * Constructor for this class that receives the message as parameter.
     * Should be called by the subclasses and the message should be customized by them (either built or received as their own constructor parameter).
     *
     * @param message The string message that will be set as the message for the exception that occurred.
     * @param id The id used to reference the information.
     */
    public ReferenceToIdException(String message, String id)
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

    @Override
    public String getMessage()
    {
        return super.getMessage() + "Id used was: " + id;
    }
}

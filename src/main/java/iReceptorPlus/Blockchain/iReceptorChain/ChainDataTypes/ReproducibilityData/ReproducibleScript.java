package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

public abstract class ReproducibleScript
{
    /**
     * The URL from which the script can be fetched.
     */
    @Property()
    private final DatasetURL url;

    public ReproducibleScript(DatasetURL url)
    {
        this.url = url;
    }
}

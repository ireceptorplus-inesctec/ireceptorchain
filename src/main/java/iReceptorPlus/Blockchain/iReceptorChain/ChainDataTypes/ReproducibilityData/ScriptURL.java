package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

public class ScriptURL implements ScriptSource
{
    /**
     * A string representing the URL from which the script can be fetched.
     */
    @Property()
    private final String url;

    public String getUrl()
    {
        return url;
    }

    public ScriptURL(String url)
    {
        this.url = url;
    }
}

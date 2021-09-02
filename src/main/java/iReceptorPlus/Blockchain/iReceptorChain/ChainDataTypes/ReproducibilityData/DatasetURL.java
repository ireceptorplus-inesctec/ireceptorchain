package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

public class DatasetURL implements DatasetSource
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

    public DatasetURL(String url)
    {
        this.url = url;
    }
}

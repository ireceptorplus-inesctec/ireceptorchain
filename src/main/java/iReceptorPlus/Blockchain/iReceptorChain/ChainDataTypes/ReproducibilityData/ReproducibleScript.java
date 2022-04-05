package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

public class ReproducibleScript
{
    /**
     * The URL from which the script can be fetched.
     */
    @Property()
    private final String url;

    public ReproducibleScript(String url)
    {
        this.url = url;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReproducibleScript that = (ReproducibleScript) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url);
    }
}

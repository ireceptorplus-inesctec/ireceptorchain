package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

public class ReproducibleScript extends DownloadbleFile
{
    public enum ScriptType {NEXTFLOW, BASH}

    /**
     * An enum type that describes the type of script. Can be either NEXTFLOW or BASH.
     */
    @Property()
    private final ScriptType scriptType;

    public ReproducibleScript(String uuid, String url, ScriptType scriptType)
    {
        super(uuid, url);
        this.scriptType = scriptType;
    }

    public ReproducibleScript()
    {
        super("", "");
        this.scriptType = null;
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

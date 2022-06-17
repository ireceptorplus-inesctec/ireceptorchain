package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * This class represents a downloadable file. Can be either a dataset or a script.
 */
public class DownloadbleFile
{
    @Property()
    protected String uuid;

    @Property()
    protected String extension;

    @Property()
    protected String url;

    @Property()
    protected String hashValue;

    public DownloadbleFile(@JsonProperty("uuid") String uuid,
                           @JsonProperty("extension") String extension,
                           @JsonProperty("url") String url,
                           @JsonProperty("hashValue") String hashValue)
    {
        this.uuid = uuid;
        this.extension = extension;
        this.url = url;
        this.hashValue = hashValue;
    }

    public String getHashValue()
    {
        return hashValue;
    }

    public void setHashValue(String hashValue)
    {
        this.hashValue = hashValue;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;

import java.util.Objects;

public abstract class iReceptorChainDataTypeInfo
{
    /**
     * The UUID used to store the data on the blockchain.
     * This does NOT include the prefix that identifier the datatype.
     * It only contains the UUID as string.
     */
    protected String UUID;

    /**
     * The data that this class refers to just as it is stored on chain.
     */
    protected iReceptorChainDataType data;

    public String getUUID()
    {
        return UUID;
    }

    public iReceptorChainDataType getData()
    {
        return data;
    }

    public iReceptorChainDataTypeInfo(String UUID, iReceptorChainDataType data)
    {
        this.UUID = UUID;
        this.data = data;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        iReceptorChainDataTypeInfo that = (iReceptorChainDataTypeInfo) o;
        return UUID.equals(that.UUID) &&
                data.equals(that.data);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(UUID, data);
    }
}

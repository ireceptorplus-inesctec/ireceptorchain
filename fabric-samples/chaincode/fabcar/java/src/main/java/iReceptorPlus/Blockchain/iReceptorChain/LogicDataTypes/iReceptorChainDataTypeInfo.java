package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;

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
}

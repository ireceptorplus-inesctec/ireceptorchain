package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.iReceptorChainDataType;

public abstract class iReceptorChainDataTypeInfo
{
    /**
     * The key used to store the data on the blockchain.
     */
    protected String key;

    /**
     * The data that this class refers to just as it is stored on chain.
     */
    protected iReceptorChainDataType data;

    public String getKey()
    {
        return key;
    }

    public iReceptorChainDataType getData()
    {
        return data;
    }

    public iReceptorChainDataTypeInfo(String key, iReceptorChainDataType data)
    {
        this.key = key;
        this.data = data;
    }
}

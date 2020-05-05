package iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;

public abstract class iReceptorChainDataTypeInfo
{
    /**
     * The key used to store the data on the blockchain.
     */
    protected String key;

    /**
     * The data that this class refers to just as it is stored on chain.
     */
    protected iReceptorChainDataTypeInfo data;

    public String getKey()
    {
        return key;
    }

    public iReceptorChainDataTypeInfo getData()
    {
        return data;
    }
}

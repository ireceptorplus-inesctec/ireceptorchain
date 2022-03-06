package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import java.util.ArrayList;

public class ReproducibilityDataUnvailable extends ReproducibilityData
{
    public ReproducibilityDataUnvailable()
    {
        super(new ArrayList<>(), new ReproducibleScriptUnavailable(), new ArrayList<>());
    }
}

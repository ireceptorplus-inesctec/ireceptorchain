package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;

/**
 * This is the sub class for the state machine for the traceability information.
 * This sub class implements the specific behaviour necessary for when the traceability information is in the Validated state.
 */
public class Validated extends State
{
    public Validated(TraceabilityDataInfo traceabilityData, HyperledgerFabricChainCodeAPI api)
    {
        super(traceabilityData, api);
    }

    @Override
    public void voteYesForTheVeracityOfTraceabilityInfo(Entity voter)
    {

    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(Entity voter)
    {

    }

    @Override
    public void flagTraceabilityInfoAsFalse(Entity whistleblower)
    {

    }
}

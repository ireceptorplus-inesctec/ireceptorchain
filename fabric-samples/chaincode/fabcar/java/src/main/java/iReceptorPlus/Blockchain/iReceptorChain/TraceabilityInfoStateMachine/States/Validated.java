package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;

/**
 * This is the sub class for the state machine for the traceability information.
 * This sub class implements the specific behaviour necessary for when the traceability information is in the Validated state.
 */
public class Validated extends State
{
    public Validated(TraceabilityInfo traceabilityInfo, HyperledgerFabricChainCodeAPI api)
    {
        super(traceabilityInfo, api);
    }

    @Override
    public void voteYesForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter)
    {

    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter)
    {

    }

    @Override
    public void flagTraceabilityInfoAsFalse(TraceabilityInfo traceabilityInfo, Entity whistleblower)
    {

    }
}

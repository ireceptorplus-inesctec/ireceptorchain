package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;

/**
 * This is the sub class for the state machine for the traceability information.
 * This sub class implements the specific behaviour necessary for when the traceability information is in the Validated state.
 */
public class Validated extends State
{
    public Validated(TraceabilityDataInfo traceabilityData, HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(traceabilityData, api);
    }

    @Override
    public void voteYesForTheVeracityOfTraceabilityInfo(Entity voter)
    {
        traceabilityDataInfo.getTraceabilityData().registerYesVoteForValidity(voter);
        api.updateTraceabilityInfo(traceabilityDataInfo);
    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(Entity voter)
    {
        //TODO ver o que fazer quando alguem vota nao a algo q ja foi aprovado
    }

}

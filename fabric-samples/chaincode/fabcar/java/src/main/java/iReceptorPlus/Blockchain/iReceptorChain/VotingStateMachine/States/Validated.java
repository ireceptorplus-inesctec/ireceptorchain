package iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Returns.VotingStateMachineReturn;

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
    public VotingStateMachineReturn voteYesForTheVeracityOfTraceabilityInfo(EntityID voterID) throws IncosistentInfoFoundOnDB
    {
        traceabilityDataInfo.getTraceabilityData().registerYesVoteForValidity(voterID);
        try
        {
            api.update(traceabilityDataInfo);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new IncosistentInfoFoundOnDB("key is already assigned to another object on trying to create new traceability entry in order to switch state");
        }

        return new VotingStateMachineReturn("Traceability data corroborated successfully. It remains on Valited state.", false)
    }

    @Override
    public VotingStateMachineReturn voteNoForTheVeracityOfTraceabilityInfo(EntityID voterID)
    {
        //TODO ver o que fazer quando alguem vota nao a algo q ja foi aprovado
        return null;
    }

}

package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.FabricChainCodeAPI.HyperledgerFabricChainCodeAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;

/**
 * This is the sub class for the state machine for the traceability information.
 * This sub class implements the specific behaviour necessary for when the traceability information is in the AwaitingValidation state.
 */
public class AwaitingValidation extends State
{
    public AwaitingValidation(TraceabilityDataInfo traceabilityData, HyperledgerFabricChainCodeAPI api)
    {
        super(traceabilityData, api);
    }

    @Override
    public void voteYesForTheVeracityOfTraceabilityInfo(TraceabilityData traceabilityData, Entity voter)
    {
        traceabilityData.registerYesVoteForValidity(voter);
        if (traceabilityData.getNumberOfApprovers() >= ChaincodeConfigs.numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid.get())
        {
            api.switchTraceabilityInfoStateFromAwaitingValidationToValidated(traceabilityDataInfo);
        }
    }

    @Override
    public void voteNoForTheVeracityOfTraceabilityInfo(TraceabilityData traceabilityData, Entity voter)
    {
        //TODO ver o q fazer neste caso (shut down the round immediately???)
        traceabilityData.registerNoVoteForValidity(voter);
    }

    @Override
    public void flagTraceabilityInfoAsFalse(TraceabilityData traceabilityData, Entity whistleblower)
    {

    }
}

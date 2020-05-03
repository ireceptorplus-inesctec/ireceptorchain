package iReceptorPlus.Blockchain.iReceptorChain.TraceabilityInfoStateMachine.States;

import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.Entity;
import iReceptorPlus.Blockchain.iReceptorChain.DataTypes.TraceabilityInfo;

public class AwaitingValidation extends State
{
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

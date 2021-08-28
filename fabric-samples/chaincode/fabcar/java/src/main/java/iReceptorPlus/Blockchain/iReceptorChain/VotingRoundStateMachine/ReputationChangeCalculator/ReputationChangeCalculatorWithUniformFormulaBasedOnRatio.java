package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationChangeCalculator;

public abstract class ReputationChangeCalculatorWithUniformFormulaBasedOnRatio implements ReputationChangeCalculator
{
    public Double calculateReputationChangeBasedOnRatio(Double value, Double ratio)
    {
        Double reputationChange = value * ratio;

        return reputationChange;
    }
}

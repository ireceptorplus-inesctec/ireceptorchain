package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationChangeCalculator;

public class ReputationChangeCalculatorWithMorePenaltyThanReward extends ReputationChangeCalculatorWithUniformFormulaBasedOnRatio
{
    public Double reputationStakeRatioNecessaryForCreatingTraceabilityDataEntry = new Double(0.30);

    public Double reputationStakeRatioNecessaryForUpVotingTraceabilityDataEntry = new Double(0.30);

    public Double reputationStakeRatioNecessaryForDownVotingTraceabilityDataEntry = new Double(0.50);

    public Double reputationRewardRatioForCreatingTruthfulTraceabilityDataEntry = new Double(0);

    public Double reputationRewardRatioForUpVotingTruthfulTraceabilityDataEntry = new Double(0.30);

    public Double reputationRewardRatioForDownVotingFakeTraceabilityDataEntry = new Double(0.50);

    public Double reputationPenaltyRatioForCreatingFakeTraceabilityDataEntry = new Double(0.70);

    public Double reputationPenaltyRatioForUpVotingFakeTraceabilityDataEntry = new Double(0.30);

    public Double reputationPenaltyRatioForDownVotingTruthfulTraceabilityDataEntry = new Double(0.50);
    
    @Override
    public Double calculateStakeRatioForCreatingTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationStakeRatioNecessaryForCreatingTraceabilityDataEntry);
    }

    @Override
    public Double calculateStakeRatioForUpVotingTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationStakeRatioNecessaryForUpVotingTraceabilityDataEntry);

    }

    @Override
    public Double calculateStakeRatioForDownVotingTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationStakeRatioNecessaryForDownVotingTraceabilityDataEntry);
    }



    @Override
    public Double calculatePenaltyRatioForCreatingIncorrectTraceabilityData(Double value)
    {

        return calculateReputationChangeBasedOnRatio(value, reputationPenaltyRatioForCreatingFakeTraceabilityDataEntry);
    }

    @Override
    public Double calculatePenaltyRatioForUpVotingIncorrectTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationPenaltyRatioForUpVotingFakeTraceabilityDataEntry);
    }

    @Override
    public Double calculatePenaltyRatioForDownVotingCorrectTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationPenaltyRatioForDownVotingTruthfulTraceabilityDataEntry);
    }



    @Override
    public Double calculateRewardRatioForCreatingCorrectTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationRewardRatioForCreatingTruthfulTraceabilityDataEntry);
    }

    @Override
    public Double calculateRewardRatioForUpVotingCorrectTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationRewardRatioForUpVotingTruthfulTraceabilityDataEntry);
    }

    @Override
    public Double calculateRewardRatioForDownVotingIncorrectTraceabilityData(Double value)
    {
        return calculateReputationChangeBasedOnRatio(value, reputationRewardRatioForDownVotingFakeTraceabilityDataEntry);
    }


}

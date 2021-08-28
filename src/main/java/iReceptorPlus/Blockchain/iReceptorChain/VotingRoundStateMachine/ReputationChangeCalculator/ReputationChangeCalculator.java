package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationChangeCalculator;

public interface ReputationChangeCalculator
{
    Double calculateStakeRatioForCreatingTraceabilityData(Double value);

    Double calculateStakeRatioForUpVotingTraceabilityData(Double value);

    Double calculateStakeRatioForDownVotingTraceabilityData(Double value);

    Double calculatePenaltyRatioForCreatingIncorrectTraceabilityData(Double value);

    Double calculatePenaltyRatioForUpVotingIncorrectTraceabilityData(Double value);

    Double calculatePenaltyRatioForDownVotingCorrectTraceabilityData(Double value);

    Double calculateRewardRatioForCreatingCorrectTraceabilityData(Double value);

    Double calculateRewardRatioForUpVotingCorrectTraceabilityData(Double value);

    Double calculateRewardRatioForDownVotingIncorrectTraceabilityData(Double value);
}

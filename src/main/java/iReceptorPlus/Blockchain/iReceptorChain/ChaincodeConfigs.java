package iReceptorPlus.Blockchain.iReceptorChain;

import com.google.common.util.concurrent.AtomicDouble;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationChangeCalculator.ReputationChangeCalculator;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationChangeCalculator.ReputationChangeCalculatorWithMorePenaltyThanReward;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationDistributer.ReputationDistributor;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationDistributer.ReputationDistributorWithUniformSplit;

public class ChaincodeConfigs
{
    public static AtomicDouble numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid = new AtomicDouble(3);

    public static AtomicDouble ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid = new AtomicDouble(0.7);

    public static AtomicDouble numberOfRejectsNecessaryForTraceabilityInfoToBeInvalid = new AtomicDouble(4);

    public static AtomicDouble ratioBetweenRejectionsAndApprovesNecessaryForTraceabilityInfoToBeInvalid = new AtomicDouble(0.3);

    public static AtomicDouble initialReputationForEntities = new AtomicDouble(30);

    public static Double baseValueOfTraceabilityDataEntry = 100.0;

    /**
     * Defines the strategy for calculating the changes in reputation (stake, reward and penalty amounts).
     */
    public static ReputationChangeCalculator reputationChangeCalculator = new ReputationChangeCalculatorWithMorePenaltyThanReward();

    /**
     * Defines the strategy for distributing the reputation rewards among the entities.
     */
    public static ReputationDistributor rewardDistributor = new ReputationDistributorWithUniformSplit();

    /**
     * Defines the strategy for distributing the reputation rewards among the entities.
     */
    public static ReputationDistributor penaltyDistributor = new ReputationDistributorWithUniformSplit();



    /**
     * A number between 0 and 1 representing the factor amount of the additional value that will be converted in reward for validators who perform correct behavior.
     * The reward came from the additional value is this number multiplied by the additional value.
     */
    public static AtomicDouble rewardFactorFromAdditionalValue = new AtomicDouble(20);

    /**
     * A number between 0 and 1 representing the factor amount of the additional value that will be converted in penalty for validators who perform incorrect behavior.
     * The penalty came from the additional value is this number multiplied by the additional value.
     */
    public static AtomicDouble penaltyFactorFromAdditionalValue = new AtomicDouble(70);



    private static String traceabilityAwaitingValidationKeyPrefix = "TraceabilityInfoAwaitingValidation";

    private static String traceabilityValidatedKeyPrefix = "TraceabilityInfoValidated";

    private static String entityDataKeyPrefix = "Entity";

    public synchronized static String getTraceabilityAwaitingValidationKeyPrefix()
    {
        return new String(traceabilityAwaitingValidationKeyPrefix);
    }

    public synchronized static String getTraceabilityValidatedKeyPrefix()
    {
        return new String(traceabilityValidatedKeyPrefix);
    }

    public synchronized static String getEntityDataKeyPrefix()
    {
        return new String(entityDataKeyPrefix);
    }

    public static synchronized boolean conditionToApproveTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return numberOfApprovers >= ChaincodeConfigs.numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid.get()
                && numberOfApprovers.doubleValue() / numberOfRejecters.doubleValue() >= ChaincodeConfigs.ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid.get();
    }

    public static synchronized boolean conditionToRejectTraceabilityInfo(Long numberOfApprovers, Long numberOfRejecters)
    {
        return numberOfRejecters >= ChaincodeConfigs.numberOfRejectsNecessaryForTraceabilityInfoToBeInvalid.get()
                &&  numberOfRejecters.doubleValue() / numberOfApprovers.doubleValue() >= ChaincodeConfigs.ratioBetweenRejectionsAndApprovesNecessaryForTraceabilityInfoToBeInvalid.get();
    }

}

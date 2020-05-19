package iReceptorPlus.Blockchain.iReceptorChain;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicLong;

public class ChaincodeConfigs
{
    public static AtomicLong numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid = new AtomicLong(3);

    public static AtomicDouble ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid = new AtomicDouble(0.7);

    public static AtomicLong numberOfRejectsNecessaryForTraceabilityInfoToBeInvalid = new AtomicLong(3);

    public static AtomicDouble ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeInvalid = new AtomicDouble(0.3);

    public static AtomicLong initialReputationForEntities = new AtomicLong(100);

    public static AtomicLong reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry = new AtomicLong(30);

    public static AtomicLong reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry = new AtomicLong(30);

    public static AtomicLong reputationStakeAmountNecessaryForDownVotingTraceabilityDataEntry = new AtomicLong(50);

    public static AtomicLong reputationRewardForCreatingTruthfulTraceabilityDataEntry = new AtomicLong(0);

    public static AtomicLong reputationRewardForUpVotingTruthfulTraceabilityDataEntry = new AtomicLong(30);

    public static AtomicLong reputationRewardForDownVotingFakeTraceabilityDataEntry = new AtomicLong(50);

    public static AtomicLong reputationPenaltyForCreatingFakeTraceabilityDataEntry = new AtomicLong(70);

    public static AtomicLong reputationPenaltyForUpVotingFakeTraceabilityDataEntry = new AtomicLong(30);

    public static AtomicLong reputationPenaltyForDownVotingTruthfulTraceabilityDataEntry = new AtomicLong(50);


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
                &&  numberOfRejecters.doubleValue() / numberOfApprovers.doubleValue() >= ChaincodeConfigs.ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeInvalid.get();
    }

}

package iReceptorPlus.Blockchain.iReceptorChain;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicLong;

public class ChaincodeConfigs
{
    public static AtomicLong numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid = new AtomicLong(3);

    public static AtomicDouble racioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid = new AtomicDouble(0.7);

    public static AtomicLong initialReputationForEntities = new AtomicLong(100);

    public static AtomicLong reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry = new AtomicLong(30);

    public static AtomicLong reputationStakeAmountNecessaryForDownVotingTraceabilityDataEntry = new AtomicLong(50);


    private static String traceabilityAwaitingValidationKeyPrefix = "TracreabilityInfoAwaitingValidation";

    private static String traceabilityValidatedKeyPrefix = "TracreabilityInfoValidated";

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
}

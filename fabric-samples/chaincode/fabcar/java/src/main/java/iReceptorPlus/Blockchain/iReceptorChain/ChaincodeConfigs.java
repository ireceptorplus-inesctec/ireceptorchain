package iReceptorPlus.Blockchain.iReceptorChain;

import java.util.concurrent.atomic.AtomicLong;

public class ChaincodeConfigs
{
    public static AtomicLong numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid = new AtomicLong(3);

    private static String traceabilityAwaitingValidationKeyPrefix = "TracreabilityInfoAwaitingValidation";

    private static String traceabilityValidatedKeyPrefix = "TracreabilityInfoValidated";

    public static String getTraceabilityAwaitingValidationKeyPrefix()
    {
        return new String(traceabilityAwaitingValidationKeyPrefix);
    }

    public static String getTraceabilityValidatedKeyPrefix()
    {
        return new String(traceabilityValidatedKeyPrefix);
    }
}

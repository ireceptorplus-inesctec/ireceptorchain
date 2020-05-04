package iReceptorPlus.Blockchain.iReceptorChain;

import java.util.concurrent.atomic.AtomicLong;

public class ChaincodeConfigs
{
    public static AtomicLong numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid = new AtomicLong(3);

    private static String traceabilityAwaitingValidationKeyPrefix = "TracreabilityInfoAwaitingValidation";

    private static String traceabilityValidatedKeyPrefix = "TracreabilityInfoValidated";
}

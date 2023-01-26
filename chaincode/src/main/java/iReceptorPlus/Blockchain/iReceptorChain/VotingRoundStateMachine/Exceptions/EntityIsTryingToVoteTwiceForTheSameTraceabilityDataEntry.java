package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.EntityID;

import java.util.UUID;

public class EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry extends TraceabilityInfoStateMachineException
{
    EntityID voterID;

    String traceabilityDataEntryUUID;

    public EntityIsTryingToVoteTwiceForTheSameTraceabilityDataEntry(EntityID voterID, String traceabilityDataEntryUUID)
    {
        super("Entity " + voterID + " is trying to vote twice for the same traceability data entry with uuid " + traceabilityDataEntryUUID);
        this.voterID = voterID;
        this.traceabilityDataEntryUUID = traceabilityDataEntryUUID;
    }

    public EntityID getVoterID()
    {
        return voterID;
    }

    public void setVoterID(EntityID voterID)
    {
        this.voterID = voterID;
    }

    public String getTraceabilityDataEntryUUID()
    {
        return traceabilityDataEntryUUID;
    }

    public void setTraceabilityDataEntryUUID(String traceabilityDataEntryUUID)
    {
        this.traceabilityDataEntryUUID = traceabilityDataEntryUUID;
    }
}

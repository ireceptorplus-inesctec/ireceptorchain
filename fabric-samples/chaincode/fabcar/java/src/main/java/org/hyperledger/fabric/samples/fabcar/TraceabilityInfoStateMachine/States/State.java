package org.hyperledger.fabric.samples.fabcar.TraceabilityInfoStateMachine.States;

import org.hyperledger.fabric.samples.fabcar.DataTypes.Entity;
import org.hyperledger.fabric.samples.fabcar.DataTypes.TraceabilityInfo;

/**
 * This is the base class for a state machine for the traceability information.
 * Subclasses of this class implement the specific actions for each state of the traceability information.
 * Since all the information must be saved on the blockchain, the data classes are well split from the logic classes.
 * This class is called to perform actions based on the state of the traceability information.
 */
public abstract class State
{

    public abstract void voteYesForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter);

    public abstract void voteNoForTheVeracityOfTraceabilityInfo(TraceabilityInfo traceabilityInfo, Entity voter);

    public abstract void flagTraceabilityInfoAsFalse(TraceabilityInfo traceabilityInfo, Entity whistleblower);

}

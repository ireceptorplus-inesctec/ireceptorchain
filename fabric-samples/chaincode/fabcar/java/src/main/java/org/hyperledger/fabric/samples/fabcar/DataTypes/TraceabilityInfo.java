package org.hyperledger.fabric.samples.fabcar.DataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This class represents an entry of traceability information.
 * This is the base class for traceability information.
 * Sub classes are used where there are necessary additional attributes, depending on the state of validation of the traceability information.
 * Please check the TraceabilityInfoStateMachine package for more information.
 */
@DataType()
public abstract class TraceabilityInfo
{
    /**
     * The hash value of the input dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    @Property()
    protected final String inputDatasetHashValue;

    /**
     * The hash value of the output dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    @Property()
    protected final String outputDatasetHashValue;

    /**
     * This is an instance of the class ProcessingDetails which contains information regarding the steps taken to perform the data transformation.
     * These steps are necessary in order to check the veracity of the traceability information.
     */
    @Property()
    protected final ProcessingDetails processingDetails;

    public TraceabilityInfo(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                            @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                            @JsonProperty("processingDetails") final ProcessingDetails processingDetails)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
    }

    /**
     * This method is called whenever a user votes yes for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    public abstract void registerYesVoteForValidity(Entity entity);

    /**
     * This method is called whenever a user votes no for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entity An instance of class Entity containing information about the entity that has voted yes for the traceability information. For more information, please read the documentation for the Entity class.
     */
    public abstract void registerNoVoteForValidity(Entity entity);
}

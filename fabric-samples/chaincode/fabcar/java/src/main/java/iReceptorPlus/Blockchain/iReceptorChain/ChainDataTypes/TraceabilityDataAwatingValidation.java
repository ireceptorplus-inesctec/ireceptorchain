package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after the entry was first created and submitted to the blockchain.
 */
@DataType()
public class TraceabilityDataAwatingValidation extends TraceabilityData
{


    public TraceabilityDataAwatingValidation(@JsonProperty("inputDatasetHashValue") final String inputDatasetHashValue,
                                             @JsonProperty("outputDatasetHashValue") final String outputDatasetHashValue,
                                             @JsonProperty("processingDetails") final ProcessingDetails processingDetails,
                                             @JsonProperty("creatorID") final EntityID creatorID)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID);
    }

    /**
     * Registers the entityID passed as parameter as an approver of the traceability information.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    @Override
    public void registerYesVoteForValidity(EntityID entityID)
    {
        approvers.add(entityID);
    }

    /**
     * Registers the entityID passed as parameter as a rejecter of the traceability information.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    @Override
    public void registerNoVoteForValidity(EntityID entityID)
    {
        rejecters.add(entityID);
    }

    /**
     * Returns the number of approvers of the traceability information represented by this class.
     * @return the number of approvers of the traceability information represented by this class.
     */
    @Override
    public long getNumberOfApprovers()
    {
        return approvers.size();
    }

    /**
     * Returns the number of rejecters of the traceability information represented by this class.
     * @return the number of rejecters of the traceability information represented by this class.
     */
    public long getNumberOfRejecters()
    {
        return rejecters.size();
    }


}

package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.Command;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData.DownloadbleFile;
import org.hyperledger.fabric.contract.annotation.DataType;

import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after the entry was first created and submitted to the blockchain.
 */
@DataType()
public class TraceabilityDataAwaitingValidation extends TraceabilityData
{

    public TraceabilityDataAwaitingValidation(ArrayList<DownloadbleFile> inputDatasets,
                                              Command command,
                                              ArrayList<DownloadbleFile> outputDatasets,
                                              EntityID creatorID,
                                              Double value)
    {
        super(inputDatasets, command, outputDatasets, creatorID, value);
        approvers = new ArrayList<>();
        rejecters = new ArrayList<>();
        this.value = value;
    }

    public TraceabilityDataAwaitingValidation(@JsonProperty("inputDatasets") final ArrayList<DownloadbleFile> inputDatasets,
                                              @JsonProperty("command") final Command command,
                                              @JsonProperty("outputDatasets") final ArrayList<DownloadbleFile> outputDatasets,
                                              @JsonProperty("creatorID") EntityID creatorID,
                                              @JsonProperty("approvers") ArrayList<EntityID> approvers,
                                              @JsonProperty("rejecters") ArrayList<EntityID> rejecters,
                                              @JsonProperty("value") Double value)
    {
        super(inputDatasets, command, outputDatasets, creatorID, approvers, rejecters, value);
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


}

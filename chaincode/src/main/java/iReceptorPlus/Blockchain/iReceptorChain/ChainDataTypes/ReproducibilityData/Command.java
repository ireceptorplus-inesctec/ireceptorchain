package iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.ReproducibilityData;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

public class Command
{
    /**
     * A String identifying the tool used to run the data processing.
     */
    @Property()
    private final String toolId;

    @Property()
    private final String commandString;

    public Command(@JsonProperty("toolId") final String toolId,
                   @JsonProperty("commandString") final String commandString)
    {
        this.toolId = toolId;
        this.commandString = commandString;
    }

    public String getToolId()
    {
        return toolId;
    }

    public String getCommandString()
    {
        return commandString;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return toolId.equals(command.toolId) && commandString.equals(command.commandString);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(toolId, commandString);
    }
}

package iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class VoteResultReturnType extends ChaincodeReturnDataType
{
    /**
     * A message describing the return.
     */
    @Property()
    String message;

    /**
     * A boolean identifying whether the execution has caused the traceability information to change its state.
     */
    @Property()
    boolean stateChange;

    public VoteResultReturnType(@JsonProperty("message") String message, @JsonProperty("stateChange") boolean stateChange)
    {
        this.message = message;
        this.stateChange = stateChange;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isStateChange()
    {
        return stateChange;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteResultReturnType that = (VoteResultReturnType) o;
        return stateChange == that.stateChange && message.equals(that.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(message, stateChange);
    }
}

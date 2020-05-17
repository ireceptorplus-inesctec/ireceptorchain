/*
 * SPDX-License-Identifier: Apache-2.0
 */

package iReceptorPlus.Blockchain.iReceptorChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.cert.CertificateException;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataAwatingValidationReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class iReceptorChainTest
{
    Genson genson = new Genson();

    private iReceptorChain contract;
    private Context ctx;
    private ChaincodeStub stub;
    private MockClientIdentity mockClientIdentity;
    private ClientIdentity clientIdentity;
    private String entityID;
    private String mockClientIdentityAsJson;
    private EntityData entityData;
    private String entityDataAsJson;
    private MockTraceabilityData mockTraceabilityData;
    private TraceabilityDataAwatingValidation traceabilityData;
    private String entityKeyOnDB;
    private ProcessingDetails processingDetails;


    public iReceptorChainTest() throws CertificateException, IOException
    {
    }

    public void putEntryToDB(Context context, String key, String value)
    {
        when(context.getStub().getStringState(key)).thenReturn(value);
    }


    @BeforeEach
    private void initVariables() throws CertificateException, IOException
    {
        setContract(new iReceptorChain());
        setCtx(mock(Context.class));
        setStub(mock(ChaincodeStub.class));
        setMockClientIdentity(new MockClientIdentity());

        setClientIdentity(getMockClientIdentity().clientIdentity);
        setEntityID(getMockClientIdentity().id);
        setMockClientIdentityAsJson(getMockClientIdentity().asJson);
        setEntityData(new EntityData(getEntityID()));
        setEntityDataAsJson(genson.serialize(getEntityData()));

        setMockTraceabilityData(new MockTraceabilityData());
        setTraceabilityData(getMockTraceabilityData().traceabilityData);
        setProcessingDetails(getTraceabilityData().getProcessingDetails());

        setEntityKeyOnDB(ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID());
    }

    private void putMockEntityToDB(String entityID, long reputation, long reputationAtStake)
    {
        EntityData entityData = new EntityData(entityID, reputation, reputationAtStake);
        String entityDataAsJson = genson.serialize(entityData);

        putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID, entityDataAsJson);
    }

    public iReceptorChain getContract()
    {
        return contract;
    }

    public void setContract(iReceptorChain contract)
    {
        this.contract = contract;
    }

    public Context getCtx()
    {
        return ctx;
    }

    public void setCtx(Context ctx)
    {
        this.ctx = ctx;
    }

    public ChaincodeStub getStub()
    {
        return stub;
    }

    public void setStub(ChaincodeStub stub)
    {
        this.stub = stub;
    }

    public MockClientIdentity getMockClientIdentity()
    {
        return mockClientIdentity;
    }

    public void setMockClientIdentity(MockClientIdentity mockClientIdentity)
    {
        this.mockClientIdentity = mockClientIdentity;
    }

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }

    public void setClientIdentity(ClientIdentity clientIdentity)
    {
        this.clientIdentity = clientIdentity;
    }

    public String getEntityID()
    {
        return entityID;
    }

    public void setEntityID(String entityID)
    {
        this.entityID = entityID;
    }

    public String getMockClientIdentityAsJson()
    {
        return mockClientIdentityAsJson;
    }

    public void setMockClientIdentityAsJson(String mockClientIdentityAsJson)
    {
        this.mockClientIdentityAsJson = mockClientIdentityAsJson;
    }

    public EntityData getEntityData()
    {
        return entityData;
    }

    public void setEntityData(EntityData entityData)
    {
        this.entityData = entityData;
    }

    public String getEntityDataAsJson()
    {
        return entityDataAsJson;
    }

    public void setEntityDataAsJson(String entityDataAsJson)
    {
        this.entityDataAsJson = entityDataAsJson;
    }

    public MockTraceabilityData getMockTraceabilityData()
    {
        return mockTraceabilityData;
    }

    public void setMockTraceabilityData(MockTraceabilityData mockTraceabilityData)
    {
        this.mockTraceabilityData = mockTraceabilityData;
    }

    public TraceabilityDataAwatingValidation getTraceabilityData()
    {
        return traceabilityData;
    }

    public void setTraceabilityData(TraceabilityDataAwatingValidation traceabilityData)
    {
        this.traceabilityData = traceabilityData;
    }

    public String getEntityKeyOnDB()
    {
        return entityKeyOnDB;
    }

    public void setEntityKeyOnDB(String entityKeyOnDB)
    {
        this.entityKeyOnDB = entityKeyOnDB;
    }

    public ProcessingDetails getProcessingDetails()
    {
        return processingDetails;
    }

    public void setProcessingDetails(ProcessingDetails processingDetails)
    {
        this.processingDetails = processingDetails;
    }

    @Nested
    class CreateEntityTransaction
    {
        @Test
        public void whenEntityExists() throws CertificateException, IOException
        {
            String entityKeyOnDB = ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID();

            when(getCtx().getStub()).thenReturn(getStub());
            when(getStub().getStringState(entityKeyOnDB)).thenReturn(getEntityDataAsJson());

            Throwable thrown = catchThrowable(() ->
            {
                getContract().createEntity(getCtx(), getClientIdentity());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Entity with the same id already exists on the blockchain database");

        }

        @Test
        public void whenEntityDoesNotExist() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getStub().getStringState(getEntityKeyOnDB())).thenReturn("");

            EntityDataInfo entityCreated = getContract().createEntity(getCtx(), getClientIdentity());
            EntityDataInfo entityDataInfo = new EntityDataInfo(getEntityID(), getEntityData());
            assertThat(entityCreated).isEqualTo(entityDataInfo);

        }
    }

    @Nested
    class CreateTraceabilityDataEntry
    {

        @Test
        public void whenUuidIsAlreadyAssignedToAnotherTraceabilityData() throws CertificateException, IOException
        {

            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            setEntityReputation(100, 0);
            String serializedTraceabilityData = genson.serialize(getTraceabilityData());

            String uuid = "uuid";
            putEntryToDB(getCtx(), ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix() + "-" + uuid, serializedTraceabilityData);

            Throwable thrown = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.Id used was: " + uuid);
        }

        @Test
        public void whenCreatorDoesNotExist() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            //ensure no entity is returned upon querying the DB
            putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID(), "");


            Throwable thrown = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), "uuid", getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class);

        }

        @Test
        public void whenCreatorExistsButDoesNotHaveEnoughReputation() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            setEntityReputation(0, 0);
            Throwable thrown = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), "uuid", getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            setEntityReputation(0, 100);
            Throwable thrown2 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), "uuid", getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            setEntityReputation(1, 0);
            Throwable thrown3 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), "uuid", getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            long reputationJustBelowLimit = ChaincodeConfigs.reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry.get() - 1;
            setEntityReputation(reputationJustBelowLimit, 0);
            Throwable thrown4 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), "uuid", getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1 and necessary reputation is 30");
            assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is 30");
        }

        private void setEntityReputation(long reputation, long reputationAtStake)
        {
            //override setup entity data and make entity not have enough reputation for this test
            EntityData entityData = new EntityData(getEntityID(), reputation, reputationAtStake);
            String entityDataAsJson = genson.serialize(entityData);

            putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID(), entityDataAsJson);
        }

        @Test
        public void whenAllIsFine() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            setEntityReputation(100, 0);

            String uuid = "uuid";
            TraceabilityDataAwatingValidationReturnType returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());

            TraceabilityDataAwatingValidationReturnType expected = new TraceabilityDataAwatingValidationReturnType(uuid, getTraceabilityData());

            assertThat(returned).isEqualTo(expected);
        }

    }

}

/*
 * SPDX-License-Identifier: Apache-2.0
 */

package iReceptorPlus.Blockchain.iReceptorChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataAwatingValidationReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class iReceptorChainTest
{
    Genson genson = new Genson();

    public iReceptorChainTest() throws CertificateException, IOException
    {
    }

    public void putEntryToDB(Context context, String key, String value)
    {
        when(context.getStub().getStringState(key)).thenReturn(value);
    }

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

    @BeforeEach
    private void initVariables() throws CertificateException, IOException
    {
        contract = new iReceptorChain();
        ctx = mock(Context.class);
        stub = mock(ChaincodeStub.class);
        mockClientIdentity = new MockClientIdentity();

        clientIdentity = mockClientIdentity.clientIdentity;
        entityID = mockClientIdentity.id;
        mockClientIdentityAsJson = mockClientIdentity.asJson;
        entityData = new EntityData(entityID);
        entityDataAsJson = genson.serialize(entityData);

        mockTraceabilityData = new MockTraceabilityData();
        traceabilityData = mockTraceabilityData.traceabilityData;
        processingDetails = traceabilityData.getProcessingDetails();

        entityKeyOnDB = ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID;
    }

    @Nested
    class CreateEntityTransaction
    {
        @Test
        public void whenEntityExists() throws CertificateException, IOException
        {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            MockClientIdentity mockClientIdentity = new MockClientIdentity();

            ClientIdentity clientIdentity = mockClientIdentity.clientIdentity;
            String entityID = mockClientIdentity.id;
            String mockClientIdentityAsJson = mockClientIdentity.asJson;
            EntityData entityData = new EntityData(entityID);
            String entityDataAsJson = genson.serialize(entityData);

            String entityKeyOnDB = ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID;

            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState(entityKeyOnDB)).thenReturn(entityDataAsJson);

            Throwable thrown = catchThrowable(() ->
            {
                contract.createEntity(ctx, clientIdentity);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Entity with the same id already exists on the blockchain database");

        }

        @Test
        public void whenEntityDoesNotExist() throws CertificateException, IOException
        {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            MockClientIdentity mockClientIdentity = new MockClientIdentity();

            ClientIdentity clientIdentity = mockClientIdentity.clientIdentity;
            String entityID = mockClientIdentity.id;
            String mockClientIdentityAsJson = mockClientIdentity.asJson;
            EntityData entityData = new EntityData(entityID);
            String entityDataAsJson = genson.serialize(entityData);

            String entityKeyOnDB = ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID;

            when(ctx.getStub()).thenReturn(stub);

            EntityDataInfo entityCreated = contract.createEntity(ctx, clientIdentity);
            EntityDataInfo entityDataInfo = new EntityDataInfo(entityID, entityData);
            assertThat(entityCreated).isEqualTo(entityDataInfo);

        }
    }

    @Nested
    class CreateTraceabilityDataEntry
    {

        @Test
        public void whenUuidIsAlreadyAssignedToAnotherTraceabilityData() throws CertificateException, IOException
        {

            when(ctx.getStub()).thenReturn(stub);
            when(ctx.getClientIdentity()).thenReturn(mockClientIdentity.clientIdentity);

            putMockEntityToDB(100, 0);
            String serializedTraceabilityData = genson.serialize(traceabilityData);

            String uuid = "uuid";
            putEntryToDB(ctx, ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix() + "-" + uuid, serializedTraceabilityData);

            Throwable thrown = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, uuid, traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.Id used was: " + uuid);
        }

        @Test
        public void whenCreatorDoesNotExist() throws CertificateException, IOException
        {
            when(ctx.getStub()).thenReturn(stub);
            when(ctx.getClientIdentity()).thenReturn(mockClientIdentity.clientIdentity);

            //ensure no entity is returned upon querying the DB
            putEntryToDB(ctx, ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID, "");


            Throwable thrown = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, "uuid", traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class);

        }

        @Test
        public void whenCreatorExistsButDoesNotHaveEnoughReputation() throws CertificateException, IOException
        {
            when(ctx.getStub()).thenReturn(stub);
            when(ctx.getClientIdentity()).thenReturn(mockClientIdentity.clientIdentity);

            putMockEntityToDB(0, 0);
            Throwable thrown = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, "uuid", traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            putMockEntityToDB(0, 100);
            Throwable thrown2 = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, "uuid", traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            putMockEntityToDB(1, 0);
            Throwable thrown3 = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, "uuid", traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            long reputationJustBelowLimit = ChaincodeConfigs.reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry.get() - 1;
            putMockEntityToDB(reputationJustBelowLimit, 0);
            Throwable thrown4 = catchThrowable(() ->
            {
                contract.createTraceabilityDataEntry(ctx, "uuid", traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                        processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                        processingDetails.getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1 and necessary reputation is 30");
            assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is 30");
        }

        private void putMockEntityToDB(long reputation, long reputationAtStake)
        {
            //override setup entity data and make entity not have enough reputation for this test
            EntityData entityData = new EntityData(entityID, reputation, reputationAtStake);
            String entityDataAsJson = genson.serialize(entityData);

            putEntryToDB(ctx, ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID, entityDataAsJson);
        }


        @Test
        public void whenAllIsFine() throws CertificateException, IOException
        {
            when(ctx.getStub()).thenReturn(stub);
            when(ctx.getClientIdentity()).thenReturn(mockClientIdentity.clientIdentity);

            putMockEntityToDB(100, 0);

            String uuid = "uuid";
            TraceabilityDataAwatingValidationReturnType returned = contract.createTraceabilityDataEntry(ctx, uuid, traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                    processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                    processingDetails.getSoftwareConfigParams());

            TraceabilityDataAwatingValidationReturnType expected = new TraceabilityDataAwatingValidationReturnType(uuid, traceabilityData);

            assertThat(returned).isEqualTo(expected);
        }

    }
}

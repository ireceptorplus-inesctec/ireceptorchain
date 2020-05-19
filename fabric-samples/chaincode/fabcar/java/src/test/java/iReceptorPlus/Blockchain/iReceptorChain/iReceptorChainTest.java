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
    private EntityData entityData;
    private MockTraceabilityData mockTraceabilityData;


    public iReceptorChainTest() throws CertificateException, IOException
    {
    }

    @BeforeEach
    private void initVariables() throws CertificateException, IOException
    {
        setContract(new iReceptorChain());
        setCtx(mock(Context.class));
        setStub(mock(ChaincodeStub.class));
        setMockClientIdentity(new MockClientIdentity());

        setClientIdentity(getMockClientIdentity().clientIdentity);
        setEntityData(new EntityData(clientIdentity.getId()));

        setMockTraceabilityData(new MockTraceabilityData());
    }

    private void putMockEntityToDB(String entityID, long reputation, long reputationAtStake)
    {
        EntityData entityData = new EntityData(entityID, reputation, reputationAtStake);
        String entityDataAsJson = genson.serialize(entityData);

        putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + entityID, entityDataAsJson);
    }

    private void putMockTraceabilityDataToDB(String id, String creatorID)
    {
        MockTraceabilityData mockTraceabilityData = new MockTraceabilityData(creatorID);
        String traceabilityDataAsJson = genson.serialize(mockTraceabilityData.traceabilityData);

        putEntryToDB(getCtx(), ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix() + "-" + id, traceabilityDataAsJson);
    }

    private void putTraceabilityDataToDB(String id, TraceabilityData traceabilityData)
    {
        String traceabilityDataAsJson = genson.serialize(traceabilityData);

        putEntryToDB(getCtx(), ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix() + "-" + id, traceabilityDataAsJson);
    }

    public void putEntryToDB(Context context, String key, String value)
    {
        when(context.getStub().getStringState(key)).thenReturn(value);
    }

    private void setEntityReputation(long reputation, long reputationAtStake)
    {
        //override setup entity data and make entity not have enough reputation for this test
        EntityData entityData = new EntityData(getEntityID(), reputation, reputationAtStake);
        String entityDataAsJson = genson.serialize(entityData);

        putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID(), entityDataAsJson);
    }

    public Genson getGenson()
    {
        return genson;
    }

    public iReceptorChain getContract()
    {
        return contract;
    }

    public Context getCtx()
    {
        return ctx;
    }

    public ChaincodeStub getStub()
    {
        return stub;
    }

    public MockClientIdentity getMockClientIdentity()
    {
        return mockClientIdentity;
    }

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }

    public EntityData getEntityData()
    {
        return entityData;
    }

    public String getEntityID()
    {
        return entityData.getId();
    }

    public String getMockClientIdentityAsJson()
    {
        return genson.serialize(mockClientIdentity);
    }

    public String getEntityDataAsJson()
    {
        return genson.serialize(entityData);
    }

    public MockTraceabilityData getMockTraceabilityData()
    {
        return mockTraceabilityData;
    }

    public TraceabilityDataAwatingValidation getTraceabilityData()
    {
        return mockTraceabilityData.traceabilityData;
    }

    public String getEntityKeyOnDB()
    {
        return ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID();
    }

    public ProcessingDetails getProcessingDetails()
    {
        return getTraceabilityData().getProcessingDetails();
    }

    public void setGenson(Genson genson)
    {
        this.genson = genson;
    }

    public void setContract(iReceptorChain contract)
    {
        this.contract = contract;
    }

    public void setCtx(Context ctx)
    {
        this.ctx = ctx;
    }

    public void setStub(ChaincodeStub stub)
    {
        this.stub = stub;
    }

    public void setMockClientIdentity(MockClientIdentity mockClientIdentity)
    {
        this.mockClientIdentity = mockClientIdentity;
    }

    public void setClientIdentity(ClientIdentity clientIdentity)
    {
        this.clientIdentity = clientIdentity;
    }

    public void setEntityData(EntityData entityData)
    {
        this.entityData = entityData;
    }

    public void setMockTraceabilityData(MockTraceabilityData mockTraceabilityData)
    {
        this.mockTraceabilityData = mockTraceabilityData;
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
            String traceabilityDataUUID = "traceabilityDataUUID";
            Throwable thrown = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), traceabilityDataUUID, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            setEntityReputation(0, 100);
            Throwable thrown2 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), traceabilityDataUUID, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            setEntityReputation(1, 0);
            Throwable thrown3 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), traceabilityDataUUID, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            long reputationJustBelowLimit = ChaincodeConfigs.reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry.get() - 1;
            setEntityReputation(reputationJustBelowLimit, 0);
            Throwable thrown4 = catchThrowable(() ->
            {
                getContract().createTraceabilityDataEntry(getCtx(), traceabilityDataUUID, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                        getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                        getProcessingDetails().getSoftwareConfigParams());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 0 and necessary reputation is 30");
            assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 1 and necessary reputation is 30");
            assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is 30");
        }

        @Test
        public void whenAllIsFine() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            String uuid = "uuid";
            long reputationStakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForCreatingTraceabilityDataEntry.get();

            TraceabilityDataAwatingValidationReturnType returned;
            TraceabilityDataAwatingValidationReturnType expected = new TraceabilityDataAwatingValidationReturnType(uuid, getTraceabilityData());

            setEntityReputation(reputationStakeNecessary, 0);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, 0);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, 0);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary * 5, reputationStakeNecessary * 5);
            returned = getContract().createTraceabilityDataEntry(getCtx(), uuid, getTraceabilityData().getInputDatasetHashValue(), getTraceabilityData().getOutputDatasetHashValue(),
                    getProcessingDetails().getSoftwareId(), getProcessingDetails().getSoftwareVersion(), getProcessingDetails().getSoftwareBinaryExecutableHashValue(),
                    getProcessingDetails().getSoftwareConfigParams());
            assertThat(returned).isEqualTo(expected);

        }

    }

    @Nested
    class RegisterVoteForTraceabilityData
    {
        String traceabilityDataUUID = "traceabilityDataUUID";
        TraceabilityData traceabilityData;

        private void whenVoterDoesNotExist()
        {
            traceabilityData = getTraceabilityData();
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            //ensure no entity is returned upon querying the DB
            putEntryToDB(getCtx(), ChaincodeConfigs.getEntityDataKeyPrefix() + "-" + getEntityID(), "");

            putMockTraceabilityDataToDB(traceabilityDataUUID, "creator");
        }

        private void setupVoterIsTheSameAsCreator()
        {
            traceabilityData = getTraceabilityData();
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            putMockEntityToDB(getEntityID(), 100, 0);
            putMockTraceabilityDataToDB(traceabilityDataUUID, getEntityID());
        }

        private void setupVoterExistsAndIsNotTheSameAsCreator()
        {
            traceabilityData = getTraceabilityData();
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            putMockEntityToDB("creator", 100, 100);


            putMockTraceabilityDataToDB(traceabilityDataUUID, "creator");
        }

        @Nested
        class RegisterYesVoteForTraceabilityData
        {

            private long reputationStakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry.get();

            @Test
            public void whenVoterDoesNotExist() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.whenVoterDoesNotExist();

                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Reference to nonexistent entity.Id used was: " + getEntityID());

            }

            @Test
            public void whenVoterIsTheSameAsCreator() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterIsTheSameAsCreator();

                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Creator of traceability data cannot vote for it.");
            }

            @Test
            public void whenVoterExistsButDoesNotHaveEnoughReputationToVote() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                setEntityReputation(0, 0);
                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(0, 100);
                Throwable thrown2 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(1, 0);
                Throwable thrown3 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                long reputationJustBelowLimit = reputationStakeNecessary - 1;
                setEntityReputation(reputationJustBelowLimit, 0);
                Throwable thrown4 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is " + reputationStakeNecessary);
            }

            @Test
            public void whenAllIsFineButNotFinishRound() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                String expected = "Vote submitted successfully. Traceability data remains waiting for validation.";
                String returned;

                setEntityReputation(reputationStakeNecessary, 0);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, 0);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, 0);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, reputationStakeNecessary * 5);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);
            }

        }

        @Nested
        class RegisterNoVoteForTraceabilityData
        {

            private long reputationStakeNecessary = ChaincodeConfigs.reputationStakeAmountNecessaryForDownVotingTraceabilityDataEntry.get();

            @Test
            public void whenVoterDoesNotExist() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.whenVoterDoesNotExist();

                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Reference to nonexistent entity.Id used was: " + getEntityID());

            }

            @Test
            public void whenVoterIsTheSameAsCreator() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterIsTheSameAsCreator();

                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Creator of traceability data cannot vote for it.");
            }

            @Test
            public void whenVoterExistsButDoesNotHaveEnoughReputationToVote() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                setEntityReputation(0, 0);
                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(0, 100);
                Throwable thrown2 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(1, 0);
                Throwable thrown3 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                long reputationJustBelowLimit = reputationStakeNecessary - 1;
                setEntityReputation(reputationJustBelowLimit, 0);
                Throwable thrown4 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is " + reputationStakeNecessary);
            }

            @Test
            public void whenAllIsFine() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                String expected = "Vote submitted successfully. Traceability data remains waiting for validation.";
                String returned;

                setEntityReputation(reputationStakeNecessary, 0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, 0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, 0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, reputationStakeNecessary * 5);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);
            }
        }

        Long reputationStakeNecessaryForUpVote = ChaincodeConfigs.reputationStakeAmountNecessaryForUpVotingTraceabilityDataEntry.get();
        Long reputationStakeNecessaryForDownVote = ChaincodeConfigs.reputationStakeAmountNecessaryForDownVotingTraceabilityDataEntry.get();

        @Test
        public void testRoundFinishLogic() throws CertificateException, IOException
        {
            RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

            String expected = "Vote submitted successfully. Traceability data remains waiting for validation.";
            String returned;

            //test with up votes only
            Long confirmationsJustBelowNecessaryForApproval = ChaincodeConfigs.numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid.get() - 1;

            TraceabilityData traceabilityData = new MockTraceabilityData("creator").traceabilityData;
            upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, confirmationsJustBelowNecessaryForApproval, traceabilityData);

            setEntityReputation(reputationStakeNecessaryForUpVote, 0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

            expected = "Vote submitted successfully. Traceability data was approved.";
            returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);

            //reset and test with down votes only
            Long confirmationsJustBelowNecessaryForRejection = ChaincodeConfigs.numberOfRejectsNecessaryForTraceabilityInfoToBeInvalid.get() - 1;

            traceabilityData = new MockTraceabilityData("creator").traceabilityData;
            expected = "Vote submitted successfully. Traceability data remains waiting for validation.";
            downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, confirmationsJustBelowNecessaryForRejection, traceabilityData);

            setEntityReputation(reputationStakeNecessaryForDownVote, 0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

            expected = "Vote submitted successfully. Traceability data was rejected.";
            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);

            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            //assertThat(returned).isEqualTo(expected);

        }

        private void upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(String expected, Long confirmationsJustBelowNecessaryForApproval, TraceabilityData traceabilityData)
        {
            String returned;
            for (long i = 0; i < confirmationsJustBelowNecessaryForApproval; i++)
            {
                setEntityReputation(reputationStakeNecessaryForUpVote, 0);
                putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                traceabilityData.registerYesVoteForValidity(new EntityID(getEntityID()));

            }
        }

        private void downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(String expected, Long confirmationsJustBelowNecessaryForRejection, TraceabilityData traceabilityData)
        {
            String returned;
            for (long i = 0; i < confirmationsJustBelowNecessaryForRejection; i++)
            {
                setEntityReputation(reputationStakeNecessaryForDownVote, 0);
                putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                traceabilityData.registerNoVoteForValidity(new EntityID(getEntityID()));

            }
        }

    }


}


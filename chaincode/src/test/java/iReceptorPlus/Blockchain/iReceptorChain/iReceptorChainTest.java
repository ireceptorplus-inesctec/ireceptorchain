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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.EntityDataReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.VoteResultReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.DataMappers.EntityDataMapper;
import iReceptorPlus.Blockchain.iReceptorChain.DataMappers.TraceabilityDataMapper;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONException;
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
    private MockTraceabilityDataAwaitingValidation mockTraceabilityDataAwaitingValidation;


    public iReceptorChainTest() throws CertificateException, IOException
    {
    }

    @BeforeEach
    private void initVariables() throws CertificateException, IOException, JSONException {
        setContract(new iReceptorChain());
        setCtx(mock(Context.class));
        setStub(mock(ChaincodeStub.class));
        setMockClientIdentity(new MockClientIdentity());

        setClientIdentity(getMockClientIdentity().clientIdentity);
        setEntityData(new EntityData(clientIdentity.getId()));

        setMockTraceabilityDataAwaitingValidation(new MockTraceabilityDataAwaitingValidation());
    }

    private String getKeyFromPrefixAndUUID(String prefix, String uuid)
    {
        return new CompositeKey(prefix, uuid).toString();
    }

    private void putMockEntityToDB(String entityID, Double reputation, Double reputationAtStake)
    {
        EntityData entityData = new EntityData(entityID, reputation, reputationAtStake);
        String entityDataAsJson = genson.serialize(entityData);

        putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), entityID), entityDataAsJson);
    }

    private void putMockTraceabilityDataToDB(String id, String creatorID)
    {
        MockTraceabilityDataAwaitingValidation mockTraceabilityDataAwaitingValidation = new MockTraceabilityDataAwaitingValidation(creatorID);
        String traceabilityDataAsJson = genson.serialize(mockTraceabilityDataAwaitingValidation.traceabilityData);

        putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix(), id), traceabilityDataAsJson);
    }

    private void putTraceabilityDataToDB(String id, TraceabilityData traceabilityData)
    {
        String traceabilityDataAsJson = genson.serialize(traceabilityData);

        String keyFromPrefixAndUUID = getKeyFromPrefixAndUUID(ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix(), id);
        putEntryToDB(getCtx(), keyFromPrefixAndUUID, traceabilityDataAsJson);
    }

    public void putEntryToDB(Context context, String key, String value)
    {
        when(context.getStub().getStringState(key)).thenReturn(value);
    }

    private void setEntityReputation(Double reputation, Double reputationAtStake)
    {
        //override setup entity data and make entity not have enough reputation for this test
        EntityData entityData = new EntityData(getEntityID(), reputation, reputationAtStake);
        String entityDataAsJson = genson.serialize(entityData);

        putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), getEntityID()), entityDataAsJson);
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

    public MockTraceabilityDataAwaitingValidation getMockTraceabilityDataAwaitingValidation()
    {
        return mockTraceabilityDataAwaitingValidation;
    }

    public TraceabilityDataAwaitingValidation getTraceabilityData()
    {
        return mockTraceabilityDataAwaitingValidation.traceabilityData;
    }

    public String getEntityKeyOnDB()
    {
        return getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), getEntityID());
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

    public void setMockTraceabilityDataAwaitingValidation(MockTraceabilityDataAwaitingValidation mockTraceabilityDataAwaitingValidation)
    {
        System.out.println("setting mock traceability data");
        this.mockTraceabilityDataAwaitingValidation = mockTraceabilityDataAwaitingValidation;
    }

    @Nested
    class CreateEntityTransaction
    {
        @Test
        public void whenEntityExists() throws CertificateException, IOException
        {
            String entityKeyOnDB = getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), getEntityID());

            when(getCtx().getStub()).thenReturn(getStub());
            when(getStub().getStringState(entityKeyOnDB)).thenReturn(getEntityDataAsJson());

            Throwable thrown = catchThrowable(() ->
            {
                getContract().createEntityByClientIdentity(getCtx(), getClientIdentity());
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Entity with the same id already exists on the blockchain database");

        }

        @Test
        public void whenEntityDoesNotExist() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getStub().getStringState(getEntityKeyOnDB())).thenReturn("");

            EntityDataInfo entityCreated = getContract().createEntityByClientIdentity(getCtx(), getClientIdentity());
            EntityDataInfo entityDataInfo = new EntityDataInfo(getEntityID(), getEntityData());
            assertThat(entityCreated).isEqualTo(entityDataInfo);

        }

        @Test
        public void enrollMySelfTest() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            when(getStub().getStringState(getEntityKeyOnDB())).thenReturn("");

            EntityDataReturnType entityCreated = getContract().enrollMyself(getCtx());
            EntityDataReturnType expected = new EntityDataReturnType(getEntityID(), getEntityData().getId(),
                    getEntityData().getReputation(), getEntityData().getReputationAtStake());
            assertThat(entityCreated).isEqualTo(expected);
        }
    }

    @Nested
    class CreateTraceabilityDataEntry
    {
        private Double reputationStakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(ChaincodeConfigs.baseValueOfTraceabilityDataEntry);


        @Test
        public void whenUuidIsAlreadyAssignedToAnotherTraceabilityData() throws CertificateException, IOException
        {

            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            setEntityReputation(100.0, 0.0);
            String serializedTraceabilityData = genson.serialize(getTraceabilityData());

            String uuid = "uuid";
            putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix(), uuid), serializedTraceabilityData);

            Throwable thrown = catchThrowable(() ->
            {
                callChaincodeSmartContract(uuid);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.Id used was: " + uuid);
        }

        private TraceabilityDataReturnType callChaincodeSmartContract(String uuid)
        {
            String serializedTraceabilityData = genson.serialize(getTraceabilityData());
            return getContract().createTraceabilityDataEntry(getCtx(), uuid, serializedTraceabilityData);
        }

        private TraceabilityDataReturnType callChaincodeSmartContract(String uuid, Double additionalValue)
        {
            String serializedTraceabilityData = genson.serialize(getTraceabilityData());
            return getContract().createTraceabilityDataEntry(getCtx(), uuid, serializedTraceabilityData);
        }

        @Test
        public void whenCreatorDoesNotExist() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            //ensure no entity is returned upon querying the DB
            putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), getEntityID()), "");


            Throwable thrown = catchThrowable(() ->
            {
                String uuid = "uuid";
                callChaincodeSmartContract(uuid);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class);

        }

        @Test
        public void whenCreatorExistsButDoesNotHaveEnoughReputation() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            setEntityReputation(0.0, 0.0);
            String traceabilityDataUUID = "traceabilityDataUUID";
            Throwable thrown = catchThrowable(() ->
            {
                callChaincodeSmartContract(traceabilityDataUUID, 0.0);
            });

            setEntityReputation(0.0, 100.0);
            Throwable thrown2 = catchThrowable(() ->
            {
                callChaincodeSmartContract(traceabilityDataUUID, 0.0);
            });

            setEntityReputation(1.0, 0.0);
            Throwable thrown3 = catchThrowable(() ->
            {
                callChaincodeSmartContract(traceabilityDataUUID, 0.0);
            });

            Double reputationJustBelowLimit = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForCreatingTraceabilityData(getTraceabilityData().getValue()) - 1;
            setEntityReputation(reputationJustBelowLimit, 0.0);
            Throwable thrown4 = catchThrowable(() ->
            {
                callChaincodeSmartContract(traceabilityDataUUID, 0.0);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
            assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
            assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is 1.0 and necessary reputation is " + reputationStakeNecessary);
            assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to create traceability data entry. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is " + reputationStakeNecessary);
        }

        @Test
        public void whenAllIsFine() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            String uuid = "uuid";
            Double reputationStakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForCreatingTraceabilityData(getTraceabilityData().getValue());

            TraceabilityDataReturnType returned;
            TraceabilityDataMapper mapper = new TraceabilityDataMapper();
            TraceabilityDataReturnType expected = mapper.getReturnTypeForTraceabilityData(uuid, getTraceabilityData());

            setEntityReputation(reputationStakeNecessary, 0.0);
            returned = callChaincodeSmartContract(uuid, 0.0);
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
            returned = callChaincodeSmartContract(uuid, 0.0);
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, 0.0);
            returned = callChaincodeSmartContract(uuid, 0.0);
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
            returned = callChaincodeSmartContract(uuid, 0.0);
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary, 0.0);
            returned = callChaincodeSmartContract(uuid, 0.0);
            assertThat(returned).isEqualTo(expected);

            setEntityReputation(reputationStakeNecessary * 5, reputationStakeNecessary * 5);
            returned = callChaincodeSmartContract(uuid, 0.0);
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
            putEntryToDB(getCtx(), getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), getEntityID()), "");

            putMockTraceabilityDataToDB(traceabilityDataUUID, "creator");
        }

        private void setupVoterIsTheSameAsCreator()
        {
            traceabilityData = getTraceabilityData();
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);

            putMockEntityToDB(getEntityID(), 100.0, 0.0);
            putMockTraceabilityDataToDB(traceabilityDataUUID, getEntityID());
        }

        private void setupVoterExistsAndIsNotTheSameAsCreator()
        {
            traceabilityData = getTraceabilityData();
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            putMockEntityToDB("creator", 100.0, 100.0);


            putMockTraceabilityDataToDB(traceabilityDataUUID, "creator");
        }

        @Nested
        class RegisterYesVoteForTraceabilityData
        {

            private Double reputationStakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(ChaincodeConfigs.baseValueOfTraceabilityDataEntry);

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

                setEntityReputation(0.0, 0.0);
                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(0.0, 100.0);
                Throwable thrown2 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(1.0, 0.0);
                Throwable thrown3 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                Double reputationJustBelowLimit = reputationStakeNecessary - 1;
                setEntityReputation(reputationJustBelowLimit, 0.0);
                Throwable thrown4 = catchThrowable(() ->
                {
                    getContract().registerYesVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is " + reputationStakeNecessary);
            }

            @Test
            public void whenAllIsFineButNotFinishRound() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                String expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
                boolean expectedStateChange = false;
                VoteResultReturnType expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
                VoteResultReturnType returned;

                setEntityReputation(reputationStakeNecessary, 0.0);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, 0.0);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, 0.0);
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

            private Double reputationStakeNecessary = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(ChaincodeConfigs.baseValueOfTraceabilityDataEntry);

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

                setEntityReputation(0.0, 0.0);
                Throwable thrown = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(0.0, 100.0);
                Throwable thrown2 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                setEntityReputation(1.0, 0.0);
                Throwable thrown3 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                Double reputationJustBelowLimit = reputationStakeNecessary - 1;
                setEntityReputation(reputationJustBelowLimit, 0.0);
                Throwable thrown4 = catchThrowable(() ->
                {
                    getContract().registerNoVoteForTraceabilityEntryInVotingRound(ctx, traceabilityDataUUID);
                });

                assertThat(thrown).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown2).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 0.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown3).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is 1.0 and necessary reputation is " + reputationStakeNecessary);
                assertThat(thrown4).isInstanceOf(ChaincodeException.class).hasMessage("Entity does not have enough reputation to place vote. Reputation of entity is " + reputationJustBelowLimit + " and necessary reputation is " + reputationStakeNecessary);
            }

            @Test
            public void whenAllIsFine() throws CertificateException, IOException
            {
                RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

                String expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
                boolean expectedStateChange = false;
                VoteResultReturnType expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
                VoteResultReturnType returned;

                setEntityReputation(reputationStakeNecessary, 0.0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary, reputationStakeNecessary);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, 0.0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary + 1, reputationStakeNecessary + 1);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, 0.0);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                setEntityReputation(reputationStakeNecessary * 5, reputationStakeNecessary * 5);
                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);
            }
        }

        Double reputationStakeNecessaryForUpVote = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
        Double reputationStakeNecessaryForDownVote = ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(ChaincodeConfigs.baseValueOfTraceabilityDataEntry);
        @Test
        public void testRoundFinishLogic() throws CertificateException, IOException, ObjectWithGivenKeyNotFoundOnBlockchainDB
        {
            RegisterVoteForTraceabilityData.this.setupVoterExistsAndIsNotTheSameAsCreator();

            String expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
            boolean expectedStateChange = false;
            VoteResultReturnType expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            VoteResultReturnType returned;

            //test with up votes only
            Double confirmationsJustBelowNecessaryForApproval = ChaincodeConfigs.numberOfConfirmationsNecessaryForTraceabilityInfoToBeValid.get() - 1;
            Double ratioNecessaryForApproval = ChaincodeConfigs.ratioBetweenApprovesAndRejectionsNecessaryForTraceabilityInfoToBeValid.get();

            TraceabilityData traceabilityData = new MockTraceabilityDataAwaitingValidation("creator").traceabilityData;
            upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, confirmationsJustBelowNecessaryForApproval, ratioNecessaryForApproval, traceabilityData);

            setEntityReputation(reputationStakeNecessaryForUpVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

            expectedMessage = "Vote submitted successfully. Traceability data was approved.";
            expectedStateChange = true;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);

            returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);

            //reset and test with down votes only
            Double rejectsJustBelowNecessaryForRejection = ChaincodeConfigs.numberOfRejectsNecessaryForTraceabilityInfoToBeInvalid.get() - 1;
            Double ratioNecessaryForRejection = ChaincodeConfigs.ratioBetweenRejectionsAndApprovesNecessaryForTraceabilityInfoToBeInvalid.get();

            traceabilityData = new MockTraceabilityDataAwaitingValidation("creator").traceabilityData;
            expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
            expectedStateChange = false;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, rejectsJustBelowNecessaryForRejection, ratioNecessaryForRejection, traceabilityData);

            setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

            expectedMessage = "Vote submitted successfully. Traceability data was rejected.";
            expectedStateChange = true;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);

            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            //assertThat(returned).isEqualTo(expected);


            //reset and test with up votes and down votes, final decision should be approval
            traceabilityData = new MockTraceabilityDataAwaitingValidation("creator").traceabilityData;
            expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
            expectedStateChange = false;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, confirmationsJustBelowNecessaryForApproval, ratioNecessaryForApproval, traceabilityData);

            //ensure that a no vote makes the traceability data remain in awaiting validation state
            setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);
            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            traceabilityData.registerNoVoteForValidity(new EntityID(getEntityID()));
            assertThat(returned).isEqualTo(expected);

            //make the traceability data be just below approval
            upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, confirmationsJustBelowNecessaryForApproval, ratioNecessaryForApproval, traceabilityData);

            //make the last (decisive) yes vote and check if it is approved, with also having rejections (to test the ratio logic)
            expectedMessage = "Vote submitted successfully. Traceability data was approved.";
            expectedStateChange = true;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);
            returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);



            //reset and test with down votes and up votes, final decision should be rejection
            traceabilityData = new MockTraceabilityDataAwaitingValidation("creator").traceabilityData;
            expectedMessage = "Vote submitted successfully. Traceability data remains waiting for validation.";
            expectedStateChange = false;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, rejectsJustBelowNecessaryForRejection, ratioNecessaryForRejection, traceabilityData);

            //ensure that a yes vote makes the traceability data remain in awaiting validation state
            setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);
            returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            traceabilityData.registerYesVoteForValidity(new EntityID(getEntityID()));
            assertThat(returned).isEqualTo(expected);

            //make the traceability data be just below approval
            downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(expected, rejectsJustBelowNecessaryForRejection, ratioNecessaryForRejection, traceabilityData);

            //make the last (decisive) yes vote and check if it is rejected, with also having approvals (to test the ratio logic)
            expectedMessage = "Vote submitted successfully. Traceability data was rejected.";
            expectedStateChange = true;
            expected = new VoteResultReturnType(expectedMessage, expectedStateChange);
            setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
            putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);
            returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
            assertThat(returned).isEqualTo(expected);
        }

        private void upVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(VoteResultReturnType expected, Double confirmationsJustBelowNecessaryForApproval, Double ratioNecessaryForApproval, TraceabilityData traceabilityData)
        {
            VoteResultReturnType returned;
            Long approvals = traceabilityData.getNumberOfApprovers();
            Long rejections = traceabilityData.getNumberOfRejecters();
            double nextRatio =  (double) (approvals + 1) / (approvals + rejections);
            while (approvals < confirmationsJustBelowNecessaryForApproval || nextRatio < ratioNecessaryForApproval)
            {

                setEntityReputation(reputationStakeNecessaryForUpVote, 0.0);
                putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

                returned = getContract().registerYesVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                traceabilityData.registerYesVoteForValidity(new EntityID(getEntityID()));

                approvals = traceabilityData.getNumberOfApprovers();
                rejections = traceabilityData.getNumberOfRejecters();
                nextRatio =  (double) (approvals + 1) / (approvals + rejections);
            }
        }

        private void downVoteTraceabilityDataUntilJustBelowApprovalAndVerifyReturns(VoteResultReturnType expected, Double confirmationsJustBelowNecessaryForRejection, double ratioNecessaryForRejection,TraceabilityData traceabilityData)
        {
            VoteResultReturnType returned;
            Long approvals = traceabilityData.getNumberOfApprovers();
            Long rejections = traceabilityData.getNumberOfRejecters();
            double nextRatio =  (double) (rejections + 1) / (approvals + rejections);
            while (rejections < confirmationsJustBelowNecessaryForRejection || nextRatio < ratioNecessaryForRejection)
            {
                setEntityReputation(reputationStakeNecessaryForDownVote, 0.0);
                putTraceabilityDataToDB(traceabilityDataUUID, traceabilityData);

                returned = getContract().registerNoVoteForTraceabilityEntryInVotingRound(getCtx(), traceabilityDataUUID);
                assertThat(returned).isEqualTo(expected);

                traceabilityData.registerNoVoteForValidity(new EntityID(getEntityID()));

                approvals = traceabilityData.getNumberOfApprovers();
                rejections = traceabilityData.getNumberOfRejecters();
                nextRatio =  (double) (rejections + 1) / (approvals + rejections);

            }
        }

    }

    @Nested
    class GetTraceabilityData
    {
        private ArrayList<TraceabilityDataInfo> getMockTraceabilityDataAwaitingValidation()
        {
            ArrayList<TraceabilityDataInfo> traceabilityDataArrayList = new ArrayList<>();

            TraceabilityDataAwaitingValidation data;
            TraceabilityDataInfo dataInfo;

            data = new MockTraceabilityDataAwaitingValidation("creator1").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity1"));
            dataInfo = new TraceabilityDataInfo("data1", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataAwaitingValidation("creator2").traceabilityData;
            data.registerNoVoteForValidity(new EntityID("entity2"));
            dataInfo = new TraceabilityDataInfo("data2", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataAwaitingValidation("creator3").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity24"));
            data.registerNoVoteForValidity(new EntityID("entity22"));
            dataInfo = new TraceabilityDataInfo("data3", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataAwaitingValidation("creator4").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity24"));
            data.registerNoVoteForValidity(new EntityID("entity22"));
            dataInfo = new TraceabilityDataInfo("data4", data);
            traceabilityDataArrayList.add(dataInfo);

            return traceabilityDataArrayList;
        }

        private ArrayList<TraceabilityDataInfo> getMockTraceabilityDataValidated()
        {
            ArrayList<TraceabilityDataInfo> traceabilityDataArrayList = new ArrayList<>();

            TraceabilityDataValidated data;
            TraceabilityDataInfo dataInfo;

            data = new MockTraceabilityDataValidated("creator1").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity1"));
            dataInfo = new TraceabilityDataInfo("data1", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataValidated("creator2").traceabilityData;
            data.registerNoVoteForValidity(new EntityID("entity2"));
            dataInfo = new TraceabilityDataInfo("data2", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataValidated("creator3").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity24"));
            data.registerNoVoteForValidity(new EntityID("entity22"));
            dataInfo = new TraceabilityDataInfo("data3", data);
            traceabilityDataArrayList.add(dataInfo);

            data = new MockTraceabilityDataValidated("creator4").traceabilityData;
            data.registerYesVoteForValidity(new EntityID("entity24"));
            data.registerNoVoteForValidity(new EntityID("entity22"));
            dataInfo = new TraceabilityDataInfo("data4", data);
            traceabilityDataArrayList.add(dataInfo);

            return traceabilityDataArrayList;
        }

        private ArrayList<EntityData> getMockEntityData()
        {
            ArrayList<EntityData> entities = new ArrayList<>();

            EntityData entity;

            entity = new EntityData("entity1", new Double(100), new Double(100));
            entities.add(entity);
            entity = new EntityData("entity2", new Double(100), new Double(100));
            entities.add(entity);
            entity = new EntityData("entity3", new Double(100), new Double(100));
            entities.add(entity);
            entity = new EntityData("entity4", new Double(100), new Double(100));
            entities.add(entity);

            return entities;
        }

        private final class MockKeyValue implements KeyValue {

            private final String key;
            private final String value;

            MockKeyValue(final String key, final String value) {
                super();
                this.key = key;
                this.value = value;
            }

            @Override
            public String getKey() {
                return this.key;
            }

            @Override
            public String getStringValue() {
                return this.value;
            }

            @Override
            public byte[] getValue() {
                return this.value.getBytes();
            }

        }

        public abstract class MockTraceabilityDataResultsIterator implements QueryResultsIterator<KeyValue>
        {
            protected final List<KeyValue> traceabilityDataArrayList;

            public MockTraceabilityDataResultsIterator()
            {
                super();
                traceabilityDataArrayList = new ArrayList<>();
            }

            protected KeyValue getTraceabilityDataKeyValue(String uuid, String keyPrefix, TraceabilityData traceabilityData)
            {
                String key = getKeyFromPrefixAndUUID(keyPrefix, uuid);
                String traceabilityDataAsJson = genson.serialize(traceabilityData);
                return new iReceptorChainTest.GetTraceabilityData.MockKeyValue(key, traceabilityDataAsJson);
            }

            protected KeyValue getTraceabilityDataAwaitingValidationKeyValue(String uuid, TraceabilityData traceabilityData)
            {
                String key = getKeyFromPrefixAndUUID(ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix(), uuid);
                String traceabilityDataAsJson = genson.serialize(traceabilityData);
                return new iReceptorChainTest.GetTraceabilityData.MockKeyValue(key, traceabilityDataAsJson);
            }

            protected KeyValue getTraceabilityDataValidatedKeyValue(String uuid, TraceabilityData traceabilityData)
            {
                String key = getKeyFromPrefixAndUUID(ChaincodeConfigs.getTraceabilityValidatedKeyPrefix(), uuid);
                String traceabilityDataAsJson = genson.serialize(traceabilityData);
                return new iReceptorChainTest.GetTraceabilityData.MockKeyValue(key, traceabilityDataAsJson);
            }

            @Override
            public Iterator<KeyValue> iterator() {
                return traceabilityDataArrayList.iterator();
            }

            @Override
            public void close() throws Exception {
                // do nothing
            }
        }

        private final class MockTraceabilityDataAwaitingValidationResultsIterator extends MockTraceabilityDataResultsIterator
        {

            MockTraceabilityDataAwaitingValidationResultsIterator() {
                super();

                ArrayList<TraceabilityDataInfo> mockData = getMockTraceabilityDataAwaitingValidation();
                for (TraceabilityDataInfo dataInfo : mockData)
                {
                    traceabilityDataArrayList.add(getTraceabilityDataAwaitingValidationKeyValue(dataInfo.getUUID(), dataInfo.getTraceabilityData()));
                }
            }

        }

        private final class MockTraceabilityValidatedResultsIterator extends MockTraceabilityDataResultsIterator
        {

            MockTraceabilityValidatedResultsIterator() {
                super();

                ArrayList<TraceabilityDataInfo> mockData = getMockTraceabilityDataValidated();
                for (TraceabilityDataInfo dataInfo : mockData)
                {
                    traceabilityDataArrayList.add(getTraceabilityDataValidatedKeyValue(dataInfo.getUUID(), dataInfo.getTraceabilityData()));
                }
            }

        }


        public class MockEntityDataResultsIterator implements QueryResultsIterator<KeyValue>
        {
            protected final List<KeyValue> entityData;

            public MockEntityDataResultsIterator()
            {
                super();
                entityData = new ArrayList<>();

                entityData.add(getEntityDataKeyValue(new EntityData("entity1", new Double(100), new Double(100))));
                entityData.add(getEntityDataKeyValue(new EntityData("entity2", new Double(100), new Double(100))));
                entityData.add(getEntityDataKeyValue(new EntityData("entity3", new Double(100), new Double(100))));
                entityData.add(getEntityDataKeyValue(new EntityData("entity4", new Double(100), new Double(100))));

            }

            protected KeyValue getEntityDataKeyValue(EntityData entityData)
            {
                String key = getKeyFromPrefixAndUUID(ChaincodeConfigs.getEntityDataKeyPrefix(), entityData.getId());
                String traceabilityDataAsJson = genson.serialize(entityData);
                return new iReceptorChainTest.GetTraceabilityData.MockKeyValue(key, traceabilityDataAsJson);
            }

            @Override
            public Iterator<KeyValue> iterator() {
                return entityData.iterator();
            }

            @Override
            public void close() throws Exception {
                // do nothing
            }
        }


        @Test
        public void testGetTraceabilityDataAwaitingValidation() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            MockTraceabilityDataAwaitingValidationResultsIterator iterator = new MockTraceabilityDataAwaitingValidationResultsIterator();
            String traceabilityAwaitingValidationKeyPrefix = ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix();
            when(getCtx().getStub().getStateByPartialCompositeKey(new CompositeKey(traceabilityAwaitingValidationKeyPrefix).toString())).thenReturn(iterator);

            ArrayList<TraceabilityDataInfo> traceabilityDataArrayList = getMockTraceabilityDataAwaitingValidation();

            TraceabilityDataReturnType[] results = contract.getAllAwaitingValidationTraceabilityDataEntries(ctx);
            for (int i = 0; i < traceabilityDataArrayList.size(); i++)
            {
                TraceabilityDataInfo dataInfo = traceabilityDataArrayList.get(i);
                TraceabilityDataMapper mapper = new TraceabilityDataMapper();
                TraceabilityDataReturnType expectedReturnType = mapper.getReturnTypeForTraceabilityData(dataInfo.getUUID(),
                        dataInfo.getTraceabilityData());
                TraceabilityDataReturnType returned = results[i];
                assertThat(returned).isEqualTo(expectedReturnType);
            }
        }


        @Test
        public void testGetTraceabilityDataValidated() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            MockTraceabilityValidatedResultsIterator iterator = new MockTraceabilityValidatedResultsIterator();
            String traceabilityValidatedKeyPrefix = ChaincodeConfigs.getTraceabilityValidatedKeyPrefix();
            when(getCtx().getStub().getStateByPartialCompositeKey(new CompositeKey(traceabilityValidatedKeyPrefix).toString())).thenReturn(iterator);

            ArrayList<TraceabilityDataInfo> traceabilityDataArrayList = getMockTraceabilityDataValidated();

            TraceabilityDataReturnType[] results = contract.getAllValidatedTraceabilityDataEntries(ctx);
            for (int i = 0; i < traceabilityDataArrayList.size(); i++)
            {
                TraceabilityDataInfo dataInfo = traceabilityDataArrayList.get(i);
                TraceabilityDataMapper mapper = new TraceabilityDataMapper();
                TraceabilityDataReturnType expectedReturnType = mapper.getReturnTypeForTraceabilityData(dataInfo.getUUID(),
                        dataInfo.getTraceabilityData());
                TraceabilityDataReturnType returned = results[i];
                assertThat(returned).isEqualTo(expectedReturnType);
            }
        }


        @Test
        public void testGetAllEntities() throws CertificateException, IOException
        {
            when(getCtx().getStub()).thenReturn(getStub());
            when(getCtx().getClientIdentity()).thenReturn(getMockClientIdentity().clientIdentity);
            MockEntityDataResultsIterator iterator = new MockEntityDataResultsIterator();
            String entityDataKeyPrefix = ChaincodeConfigs.getEntityDataKeyPrefix();
           when(getCtx().getStub().getStateByPartialCompositeKey(new CompositeKey(entityDataKeyPrefix).toString())).thenReturn(iterator);

            ArrayList<EntityData> traceabilityDataArrayList = getMockEntityData();

            EntityDataReturnType[] results = contract.getAllEntities(ctx);
            for (int i = 0; i < traceabilityDataArrayList.size(); i++)
            {
                EntityData expectedData = traceabilityDataArrayList.get(i);
                EntityDataMapper mapper = new EntityDataMapper();
                EntityDataReturnType expectedReturnType = mapper.getEntityDataReturnTypeFromEntityData(expectedData);
                EntityDataReturnType returned = results[i];
                assertThat(returned).isEqualTo(expectedReturnType);
            }
        }
    }

}


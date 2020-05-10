/*
 * SPDX-License-Identifier: Apache-2.0
 */

package iReceptorPlus.Blockchain.iReceptorChain;

import java.util.ArrayList;
import java.util.List;

import iReceptorPlus.Blockchain.iReceptorChain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataAwatingValidationReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeReturnDataTypes.TraceabilityDataValidatedReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPI.TraceabilityDataAwatingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.IncosistentInfoFoundOnDB;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.Exceptions.UnsupportedTypeOfTraceabilityInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingStateMachine.TraceabilityInfoStateMachine;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

/**
 * Java implementation of the Fabric Car Contract described in the Writing Your
 * First Application tutorial
 */
@Contract(
        name = "iReceptorChain",
        info = @Info(
                title = "iReceptorChain contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))
@Default
public final class iReceptorChain implements ContractInterface {

    private final Genson genson = new Genson();

    private enum FabCarErrors {
        CAR_NOT_FOUND,
        CAR_ALREADY_EXISTS
    }

    /**
     * Retrieves a car with the specified UUID from the ledger.
     *
     * @param ctx the transaction context
     * @param key the UUID
     * @return the Car found on the ledger if there was one
     */
    @Transaction()
    public Car queryCar(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String carState = stub.getStringState(key);

        if (carState.isEmpty()) {
            String errorMessage = String.format("Car %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Car car = genson.deserialize(carState, Car.class);

        return car;
    }

    /**
     * Creates some initial Cars on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        String[] carData = {
                "{ \"make\": \"Toyota\", \"model\": \"Prius\", \"color\": \"blue\", \"owner\": \"Tomoko\" }",
                "{ \"make\": \"Ford\", \"model\": \"Mustang\", \"color\": \"red\", \"owner\": \"Brad\" }",
                "{ \"make\": \"Hyundai\", \"model\": \"Tucson\", \"color\": \"green\", \"owner\": \"Jin Soo\" }",
                "{ \"make\": \"Volkswagen\", \"model\": \"Passat\", \"color\": \"yellow\", \"owner\": \"Max\" }",
                "{ \"make\": \"Tesla\", \"model\": \"S\", \"color\": \"black\", \"owner\": \"Adrian\" }",
                "{ \"make\": \"Peugeot\", \"model\": \"205\", \"color\": \"purple\", \"owner\": \"Michel\" }",
                "{ \"make\": \"Chery\", \"model\": \"S22L\", \"color\": \"white\", \"owner\": \"Aarav\" }",
                "{ \"make\": \"Fiat\", \"model\": \"Punto\", \"color\": \"violet\", \"owner\": \"Pari\" }",
                "{ \"make\": \"Tata\", \"model\": \"nano\", \"color\": \"indigo\", \"owner\": \"Valeria\" }",
                "{ \"make\": \"Holden\", \"model\": \"Barina\", \"color\": \"brown\", \"owner\": \"Shotaro\" }"
        };

        for (int i = 0; i < carData.length; i++) {
            String key = String.format("CAR%d", i);

            Car car = genson.deserialize(carData[i], Car.class);
            String carState = genson.serialize(car);
            stub.putStringState(key, carState);
        }

        createTraceabilityDataEntry(ctx,
                "uuid",
                "inputDatasetHashValue",
                "outputDatasetHashValue",
                "softwareId",
                "softwareVersion",
                "softwareBinaryExecutableHashValue",
                "softwareConfigParams"
                );
    }

    /**
     * Creates a new car on the ledger.
     *
     * @param ctx the transaction context
     * @param key the UUID for the new car
     * @param make the make of the new car
     * @param model the model of the new car
     * @param color the color of the new car
     * @param owner the owner of the new car
     * @return the created Car
     */
    @Transaction()
    public Car createCar(final Context ctx, final String key, final String make, final String model,
            final String color, final String owner) {
        ChaincodeStub stub = ctx.getStub();

        String carState = stub.getStringState(key);
        if (!carState.isEmpty()) {
            String errorMessage = String.format("Car %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_ALREADY_EXISTS.toString());
        }

        Car car = new Car(make, model, color, owner);
        carState = genson.serialize(car);
        stub.putStringState(key, carState);

        return car;
    }

    /**
     * Retrieves every car between CAR0 and CAR999 from the ledger.
     *
     * @param ctx the transaction context
     * @return array of Cars found on the ledger
     */
    @Transaction()
    public CarQueryResult[] queryAllCars(final Context ctx) {

        ChaincodeStub stub = ctx.getStub();

        final String startKey = "CAR0";
        final String endKey = "CAR999";
        List<CarQueryResult> queryResults = new ArrayList<CarQueryResult>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue result: results) {
            Car car = genson.deserialize(result.getStringValue(), Car.class);
            queryResults.add(new CarQueryResult(result.getKey(), car));
        }

        CarQueryResult[] response = queryResults.toArray(new CarQueryResult[queryResults.size()]);


        return response;
    }

    @Transaction()
    public TraceabilityData createTraceabilityEntries(final Context ctx) {
        iReceptorChainDataType traceabilityDataAwatingValidation = new TraceabilityDataAwatingValidation("a","b", new ProcessingDetails("", "", "", ""));
        iReceptorChainDataType traceabilityDataValidated = new TraceabilityDataValidated("c","d", new ProcessingDetails("", "", "", ""), new ArrayList<>());

        ChaincodeStub stub = ctx.getStub();

        String traceabilityInfoAwatingValidationState = genson.serialize(traceabilityDataAwatingValidation);
        String traceabilityInfoValidatedState = genson.serialize(traceabilityDataValidated);
        stub.putStringState("traceabilityInfoAwatingValidation5", traceabilityInfoAwatingValidationState);
        stub.putStringState("traceabilityInfoValidated5", traceabilityInfoValidatedState);

        return null;

    }

    @Transaction()
    public void testQueryTraceability(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<TraceabilityData> queryResults = new ArrayList<TraceabilityData>();

        String traceabilityInfoAwatingValidation = stub.getStringState("traceabilityInfoAwatingValidation5");
        System.err.println("*******traceabilityInfoAwatingValidation******");
        System.err.println(traceabilityInfoAwatingValidation);
        if (traceabilityInfoAwatingValidation.isEmpty())
            System.err.println("does not exist1");
        else
            System.err.println("exists1");
        TraceabilityData resultsAwatingValidation = genson.deserialize(traceabilityInfoAwatingValidation, TraceabilityDataAwatingValidation.class);
        String traceabilityInfoValidated = stub.getStringState("traceabilityInfoValidated5");
        System.err.println("******traceabilityInfoValidated********");
        System.err.println(traceabilityInfoValidated);
        if (traceabilityInfoValidated.isEmpty())
            System.err.println("does not exist2");
        TraceabilityData resultsValidated = genson.deserialize(traceabilityInfoValidated, TraceabilityDataValidated.class);


        resultsAwatingValidation.registerYesVoteForValidity(new Entity());
        resultsValidated.registerYesVoteForValidity(new Entity());
        System.err.println("stuff4");


    }


    @Transaction()
    public TraceabilityData test(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

/*
        Car car = new Car("makeeee", "modefl", "colfdor", "owndfer");
        String carState = genson.serialize(car);
        System.err.println("carState");
        System.err.println("carState");
        System.err.println("carState");
        System.err.println("carState");
        System.err.println("carState");
        System.err.println(carState);
        System.err.println("carState end");
        System.err.println("carState end");
        System.err.println("carState end");
        System.err.println("carState end");
        System.err.println("carState end");
        stub.putStringState("carr1", car);
*/

        stub.putStringState("carr1", "stuffffff");

        String carStateQueryResult = stub.getStringState("carr1");
        if (carStateQueryResult.isEmpty())
            System.err.println("does not exist0");
        else
            System.err.println("exists0 and result is: " + carStateQueryResult);

        String traceabilityInfoAwatingValidation = stub.getStringState("traceabilityInfoAwatingValidation5");
        System.err.println("traceabilityInfoAwatingValidation");
        System.err.println(traceabilityInfoAwatingValidation);
        if (traceabilityInfoAwatingValidation.isEmpty())
            System.err.println("does not exist1");
        else
            System.err.println("exists1");
        iReceptorChainDataType resultsAwatingValidation = genson.deserialize(traceabilityInfoAwatingValidation, TraceabilityDataAwatingValidation.class);
        String traceabilityInfoValidated = stub.getStringState("traceabilityInfoValidated5");
        System.err.println("traceabilityInfoValidated");
        System.err.println(traceabilityInfoValidated);
        if (traceabilityInfoValidated.isEmpty())
            System.err.println("does not exist2");
        iReceptorChainDataType resultsValidated = genson.deserialize(traceabilityInfoValidated, TraceabilityDataAwatingValidation.class);

        return (TraceabilityData) resultsValidated;
/*
        resultsAwatingValidation.registerYesVoteForValidity(new Entity());
        resultsValidated.registerYesVoteForValidity(new Entity());
        System.err.println("stuff4");
*/

    }


    /**
     * Changes the owner of a car on the ledger.
     *
     * @param ctx the transaction context
     * @param key the UUID
     * @param newOwner the new owner
     * @return the updated Car
     */
    @Transaction()
    public Car changeCarOwner(final Context ctx, final String key, final String newOwner) {
        ChaincodeStub stub = ctx.getStub();

        String carState = stub.getStringState(key);

        if (carState.isEmpty()) {
            String errorMessage = String.format("Car %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Car car = genson.deserialize(carState, Car.class);

        Car newCar = new Car(car.getMake(), car.getModel(), car.getColor(), newOwner);
        String newCarState = genson.serialize(newCar);
        stub.putStringState(key, newCarState);

        return newCar;
    }

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     *
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param inputDatasetHashValue the hash value of the input dataset used to perform the data transformation.
     * @param outputDatasetHashValue the hash value of the output dataset used to perform the data transformation.
     * @param softwareId an unique identifier of the software used to perform the data transformation.
     * @param softwareVersion the version of the software used to perform the data transformation.
     * @param softwareBinaryExecutableHashValue the hash value of the binary executable used to perform the data transformation.
     * @param softwareConfigParams the configuration parameters of the software used to perform the data transformation.
     * @return the traceability entry and the UUID used to reference the traceability information.
     */
    @Transaction()
    public TraceabilityDataAwatingValidationReturnType createTraceabilityDataEntry(final Context ctx, final String newUUID, final String inputDatasetHashValue,
                                                            final String outputDatasetHashValue, final String softwareId,
                                                            final String softwareVersion, final String softwareBinaryExecutableHashValue,
                                                            final String softwareConfigParams) {
        logDebugMsg("createTraceabilityDataEntry");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityDataAwatingValidation traceabilityData = new TraceabilityDataAwatingValidation(inputDatasetHashValue, outputDatasetHashValue,
                new ProcessingDetails(softwareId, softwareVersion, softwareBinaryExecutableHashValue, softwareConfigParams));

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        String key = null;
        try
        {
            key = api.create(newUUID, traceabilityData);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new ChaincodeException(givenIdIsAlreadyAssignedToAnotherObject.getMessage());
        }

        TraceabilityDataAwatingValidationReturnType traceabilityDataInfo = new TraceabilityDataAwatingValidationReturnType(newUUID, traceabilityData);

        logDebugMsg("createTraceabilityDataEntry END");

        return traceabilityDataInfo;
    }

    /**
     * Allows a node to vote yes for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote yes for.
     * @return a string identifying the success of the operation.
     */
    @Transaction()
    public String registerYesVoteForTraceabilityEntryInVotingRound(final Context ctx, final String uuid) {

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound");

        TraceabilityInfoStateMachine traceabilityInfoStateMachine = getInfoFromDBAndBuildVotingStateMachine(ctx, uuid);



        //TODO fix this aldrabation of the entity
        try
        {
            traceabilityInfoStateMachine.voteYesForTheVeracityOfTraceabilityInfo(new Entity());
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        }

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound END");

        return "Vote submitted Successfully";
    }

    /**
     * Allows a node to vote no for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote no for.
     * @return a string identifying the success of the operation.
     */
    @Transaction()
    public String registerNoVoteForTraceabilityEntryInVotingRound(final Context ctx, final String uuid) {
        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound");

        TraceabilityInfoStateMachine traceabilityInfoStateMachine = getInfoFromDBAndBuildVotingStateMachine(ctx, uuid);

        //TODO fix this aldrabation of the entity
        try
        {
            traceabilityInfoStateMachine.voteNoForTheVeracityOfTraceabilityInfo(new Entity());
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        }
        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound END");

        return "Vote submitted Successfully";
    }

    /**
     * Auxiliary method for the voting methods.
     * Validates the info on the database and builds the state machine that implements the voting rounds logic.
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to support voting for.
     * @return a string identifying the success of the operation.
     */
    private TraceabilityInfoStateMachine getInfoFromDBAndBuildVotingStateMachine(Context ctx, String uuid)
    {
        logDebugMsg("getInfoFromDBAndBuildVotingStateMachine");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityData traceabilityData;

        TraceabilityDataAwatingValidationRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        try
        {
            traceabilityData = (TraceabilityData) api.read(uuid);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ChaincodeException(objectWithGivenKeyNotFoundOnBlockchainDB.getMessage());
        }

        TraceabilityDataInfo traceabilityDataInfo = new TraceabilityDataInfo(uuid, traceabilityData);
        TraceabilityInfoStateMachine traceabilityInfoStateMachine;
        try
        {
            traceabilityInfoStateMachine = new TraceabilityInfoStateMachine(traceabilityDataInfo, api);
        } catch (UnsupportedTypeOfTraceabilityInfo unsupportedTypeOfTraceabilityInfo)
        {
            throw new ChaincodeException("Voting on information with this type is not supported");
        }
        logDebugMsg("getInfoFromDBAndBuildVotingStateMachine END");

        return traceabilityInfoStateMachine;
    }

    /**
     * Retrieves all traceability data that is in state awating validation.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */
    @Transaction()
    public TraceabilityDataAwatingValidationReturnType[] getAllAwaitingValidationTraceabilityDataEntries(final Context ctx) {
        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries");
        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataAwatingValidationReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            TraceabilityDataAwatingValidationReturnType dataReturnType = new TraceabilityDataAwatingValidationReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataAwatingValidation) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
    }

        TraceabilityDataAwatingValidationReturnType[] response = resultsToReturn.toArray(new TraceabilityDataAwatingValidationReturnType[resultsToReturn.size()]);
        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries END");

        return response;
    }

    /**
     * Retrieves all traceability data that is in state validated.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */
    @Transaction()
    public TraceabilityDataValidatedReturnType[] getAllValidatedTraceabilityDataEntries(final Context ctx) {
        logDebugMsg("getAllValidatedTraceabilityDataEntries");

        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwatingValidationRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            TraceabilityDataValidatedReturnType dataReturnType = new TraceabilityDataValidatedReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataValidated) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
    }

        TraceabilityDataValidatedReturnType[] response = resultsToReturn.toArray(new TraceabilityDataValidatedReturnType[resultsToReturn.size()]);
        logDebugMsg("getAllValidatedTraceabilityDataEntries END");

        return response;
    }

    private void logDebugMsg(String msg)
    {
        System.err.println("************************** " + msg + " **************************");
    }
}

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
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import com.owlike.genson.Genson;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class FabCarTest {
    Genson genson = new Genson();
    //ClientIdentity cl = genson.deserialize("{\"id\":\"x509::CN=appUser, OU=client + OU=org1 + OU=department1::CN=fabric-ca-server, OU=Fabric, O=Hyperledger, ST=North Carolina, C=US\",\"mSPID\":\"Org1MSP\",\"x509Certificate\":{\"extendedKeyUsage\":null,\"issuerAlternativeNames\":null,\"issuerX500Principal\":{\"encoded\":\"MGgxCzAJBgNVBAYTAlVTMRcwFQYDVQQIEw5Ob3J0aCBDYXJvbGluYTEUMBIGA1UEChMLSHlwZXJsZWRnZXIxDzANBgNVBAsTBkZhYnJpYzEZMBcGA1UEAxMQZmFicmljLWNhLXNlcnZlcg==\",\"name\":\"CN=fabric-ca-server,OU=Fabric,O=Hyperledger,ST=North Carolina,C=US\"},\"subjectAlternativeNames\":null,\"subjectX500Principal\":{\"encoded\":\"MEQxMDALBgNVBAsTBG9yZzEwDQYDVQQLEwZjbGllbnQwEgYDVQQLEwtkZXBhcnRtZW50MTEQMA4GA1UEAxMHYXBwVXNlcg==\",\"name\":\"CN=appUser,OU=client+OU=org1+OU=department1\"},\"type\":\"X.509\"}}", ClientIdentity.class);

    private ClientIdentity getMockClientIdentity() throws CertificateException, IOException
    {
        String mockClientIdentityEncodedBase64 = "CgdPcmcxTVNQEpwHLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNmRENDQWlLZ0F3SUJBZ0lVVVI1YXlZank5RzFxOFJwaDNpb1o4RVZYeDlnd0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCbVlXSnlhV010ClkyRXRjMlZ5ZG1WeU1CNFhEVEl3TURVeE5USXdNakl3TUZvWERUSXhNRFV4TlRJd01qY3dNRm93UkRFd01BMEcKQTFVRUN4TUdZMnhwWlc1ME1Bc0dBMVVFQ3hNRWIzSm5NVEFTQmdOVkJBc1RDMlJsY0dGeWRHMWxiblF4TVJBdwpEZ1lEVlFRREV3ZGhjSEJWYzJWeU1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRWtNMWhoYXdnCkhTeFd3K0xVVUE3WkVQTWNDT1NJSllVTU1aZFcwL3FPak5PVGZSOXQzclBFYllvSE1QTWkyaGtxcVJQY29sZWcKM3BlUE43ajFNZEs1ZGFPQnpUQ0J5akFPQmdOVkhROEJBZjhFQkFNQ0I0QXdEQVlEVlIwVEFRSC9CQUl3QURBZApCZ05WSFE0RUZnUVVQV1NUSmZrR1J3T3IvTlRqL0FldkZhR3pVYkV3SHdZRFZSMGpCQmd3Rm9BVUo2VTAydjQzCmJKbmV3citpc3JubnltY3RpUG93YWdZSUtnTUVCUVlIQ0FFRVhuc2lZWFIwY25NaU9uc2lhR1l1UVdabWFXeHAKWVhScGIyNGlPaUp2Y21jeExtUmxjR0Z5ZEcxbGJuUXhJaXdpYUdZdVJXNXliMnhzYldWdWRFbEVJam9pWVhCdwpWWE5sY2lJc0ltaG1MbFI1Y0dVaU9pSmpiR2xsYm5RaWZYMHdDZ1lJS29aSXpqMEVBd0lEU0FBd1JRSWhBS1FPCnBwdmlhTGY0VXZzaU0zOWVsYlNjYzdvcHdVRnRkU25MVkkwc3NUNHhBaUJXWUwxejFVNkhJdTQ2clMvaThOS3gKMTVUVENMQUNxd01jYjNPOE9OaVE2UT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
        byte[] mockClientIdentityBytes = Base64.getDecoder().decode(mockClientIdentityEncodedBase64);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(stub.getCreator()).thenReturn(mockClientIdentityBytes);
        ClientIdentity clientIdentity = new ClientIdentity(stub);

        return clientIdentity;
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

    private final class MockCarResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> carList;

        MockCarResultsIterator() {
            super();

            carList = new ArrayList<KeyValue>();

            carList.add(new MockKeyValue("CAR0",
                    "{\"color\":\"blue\",\"make\":\"Toyota\",\"model\":\"Prius\",\"owner\":\"Tomoko\"}"));
            carList.add(new MockKeyValue("CAR1",
                    "{\"color\":\"red\",\"make\":\"Ford\",\"model\":\"Mustang\",\"owner\":\"Brad\"}"));
            carList.add(new MockKeyValue("CAR2",
                    "{\"color\":\"green\",\"make\":\"Hyundai\",\"model\":\"Tucson\",\"owner\":\"Jin Soo\"}"));
            carList.add(new MockKeyValue("CAR7",
                    "{\"color\":\"violet\",\"make\":\"Fiat\",\"model\":\"Punto\",\"owner\":\"Pari\"}"));
            carList.add(new MockKeyValue("CAR9",
                    "{\"color\":\"brown\",\"make\":\"Holden\",\"model\":\"Barina\",\"owner\":\"Shotaro\"}"));
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return carList.iterator();
        }

        @Override
        public void close() throws Exception {
            // do nothing
        }

    }

    @Test
    public void invokeUnknownTransaction() {
        iReceptorChain contract = new iReceptorChain();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyZeroInteractions(ctx);
    }

    @Nested
    class InvokeQueryCarTransaction {

        @Test
        public void whenCarExists() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0"))
                    .thenReturn("{\"color\":\"blue\",\"make\":\"Toyota\",\"model\":\"Prius\",\"owner\":\"Tomoko\"}");

            Car car = contract.queryCar(ctx, "CAR0");

            assertThat(car).isEqualTo(new Car("Toyota", "Prius", "blue", "Tomoko"));
        }

        @Test
        public void whenCarDoesNotExist() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.queryCar(ctx, "CAR0");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Car CAR0 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CAR_NOT_FOUND".getBytes());
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        iReceptorChain contract = new iReceptorChain();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.initLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("CAR0",
                "{\"color\":\"blue\",\"make\":\"Toyota\",\"model\":\"Prius\",\"owner\":\"Tomoko\"}");
        inOrder.verify(stub).putStringState("CAR1",
                "{\"color\":\"red\",\"make\":\"Ford\",\"model\":\"Mustang\",\"owner\":\"Brad\"}");
        inOrder.verify(stub).putStringState("CAR2",
                "{\"color\":\"green\",\"make\":\"Hyundai\",\"model\":\"Tucson\",\"owner\":\"Jin Soo\"}");
        inOrder.verify(stub).putStringState("CAR3",
                "{\"color\":\"yellow\",\"make\":\"Volkswagen\",\"model\":\"Passat\",\"owner\":\"Max\"}");
        inOrder.verify(stub).putStringState("CAR4",
                "{\"color\":\"black\",\"make\":\"Tesla\",\"model\":\"S\",\"owner\":\"Adrian\"}");
        inOrder.verify(stub).putStringState("CAR5",
                "{\"color\":\"purple\",\"make\":\"Peugeot\",\"model\":\"205\",\"owner\":\"Michel\"}");
        inOrder.verify(stub).putStringState("CAR6",
                "{\"color\":\"white\",\"make\":\"Chery\",\"model\":\"S22L\",\"owner\":\"Aarav\"}");
        inOrder.verify(stub).putStringState("CAR7",
                "{\"color\":\"violet\",\"make\":\"Fiat\",\"model\":\"Punto\",\"owner\":\"Pari\"}");
        inOrder.verify(stub).putStringState("CAR8",
                "{\"color\":\"indigo\",\"make\":\"Tata\",\"model\":\"nano\",\"owner\":\"Valeria\"}");
        inOrder.verify(stub).putStringState("CAR9",
                "{\"color\":\"brown\",\"make\":\"Holden\",\"model\":\"Barina\",\"owner\":\"Shotaro\"}");
    }

    @Nested
    class InvokeCreateCarTransaction {

        @Test
        public void whenCarExists() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0"))
                    .thenReturn("{\"color\":\"blue\",\"make\":\"Toyota\",\"model\":\"Prius\",\"owner\":\"Tomoko\"}");

            Throwable thrown = catchThrowable(() -> {
                contract.createCar(ctx, "CAR0", "Nissan", "Leaf", "green", "Siobhán");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Car CAR0 already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CAR_ALREADY_EXISTS".getBytes());
        }

        @Test
        public void whenCarDoesNotExist() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0")).thenReturn("");

            Car car = contract.createCar(ctx, "CAR0", "Nissan", "Leaf", "green", "Siobhán");

            assertThat(car).isEqualTo(new Car("Nissan", "Leaf", "green", "Siobhán"));
        }
    }

    @Test
    void invokeQueryAllCarsTransaction() {
        iReceptorChain contract = new iReceptorChain();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("CAR0", "CAR999")).thenReturn(new MockCarResultsIterator());

        CarQueryResult[] cars = contract.queryAllCars(ctx);

        final List<CarQueryResult> expectedCars = new ArrayList<CarQueryResult>();
        expectedCars.add(new CarQueryResult("CAR0", new Car("Toyota", "Prius", "blue", "Tomoko")));
        expectedCars.add(new CarQueryResult("CAR1", new Car("Ford", "Mustang", "red", "Brad")));
        expectedCars.add(new CarQueryResult("CAR2", new Car("Hyundai", "Tucson", "green", "Jin Soo")));
        expectedCars.add(new CarQueryResult("CAR7", new Car("Fiat", "Punto", "violet", "Pari")));
        expectedCars.add(new CarQueryResult("CAR9", new Car("Holden", "Barina", "brown", "Shotaro")));

        assertThat(cars).containsExactlyElementsOf(expectedCars);
    }

    @Nested
    class ChangeCarOwnerTransaction {

        @Test
        public void whenCarExists() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0"))
                    .thenReturn("{\"color\":\"blue\",\"make\":\"Toyota\",\"model\":\"Prius\",\"owner\":\"Tomoko\"}");

            Car car = contract.changeCarOwner(ctx, "CAR0", "Dr Evil");

            assertThat(car).isEqualTo(new CarQueryResult("CAR0", new Car("Toyota", "Prius", "blue", "Dr Evil")));
        }

        @Test
        public void whenCarDoesNotExist() {
            iReceptorChain contract = new iReceptorChain();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CAR0")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.changeCarOwner(ctx, "CAR0", "Dr Evil");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Car CAR0 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CAR_NOT_FOUND".getBytes());
        }
    }
}

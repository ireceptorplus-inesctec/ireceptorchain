package iReceptorPlus.Blockchain.iReceptorChain;

import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.JSONException;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Base64;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockClientIdentity
{
    public ClientIdentity clientIdentity;
    public String id;
    public String asJson;

    public MockClientIdentity() throws CertificateException, IOException, JSONException {
        this.clientIdentity = getMockClientIdentity();
        this.id = "x509::CN=appUser, OU=client + OU=org1 + OU=department1::CN=fabric-ca-server, OU=Fabric, O=Hyperledger, ST=North Carolina, C=US";
        this.asJson = "{\"id\":\"x509::CN=appUser, OU=client + OU=org1 + OU=department1::CN=fabric-ca-server, OU=Fabric, O=Hyperledger, ST=North Carolina, C=US\",\"mSPID\":\"Org1MSP\",\"x509Certificate\":{\"extendedKeyUsage\":null,\"issuerAlternativeNames\":null,\"issuerX500Principal\":{\"encoded\":\"MGgxCzAJBgNVBAYTAlVTMRcwFQYDVQQIEw5Ob3J0aCBDYXJvbGluYTEUMBIGA1UEChMLSHlwZXJsZWRnZXIxDzANBgNVBAsTBkZhYnJpYzEZMBcGA1UEAxMQZmFicmljLWNhLXNlcnZlcg==\",\"name\":\"CN=fabric-ca-server,OU=Fabric,O=Hyperledger,ST=North Carolina,C=US\"},\"subjectAlternativeNames\":null,\"subjectX500Principal\":{\"encoded\":\"MEQxMDALBgNVBAsTBG9yZzEwDQYDVQQLEwZjbGllbnQwEgYDVQQLEwtkZXBhcnRtZW50MTEQMA4GA1UEAxMHYXBwVXNlcg==\",\"name\":\"CN=appUser,OU=client+OU=org1+OU=department1\"},\"type\":\"X.509\"}}";
    }

    private ClientIdentity getMockClientIdentity() throws CertificateException, IOException, JSONException {
        String mockClientIdentityEncodedBase64 = "CgdPcmcxTVNQEpwHLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNmRENDQWlLZ0F3SUJBZ0lVVVI1YXlZank5RzFxOFJwaDNpb1o4RVZYeDlnd0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCbVlXSnlhV010ClkyRXRjMlZ5ZG1WeU1CNFhEVEl3TURVeE5USXdNakl3TUZvWERUSXhNRFV4TlRJd01qY3dNRm93UkRFd01BMEcKQTFVRUN4TUdZMnhwWlc1ME1Bc0dBMVVFQ3hNRWIzSm5NVEFTQmdOVkJBc1RDMlJsY0dGeWRHMWxiblF4TVJBdwpEZ1lEVlFRREV3ZGhjSEJWYzJWeU1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRWtNMWhoYXdnCkhTeFd3K0xVVUE3WkVQTWNDT1NJSllVTU1aZFcwL3FPak5PVGZSOXQzclBFYllvSE1QTWkyaGtxcVJQY29sZWcKM3BlUE43ajFNZEs1ZGFPQnpUQ0J5akFPQmdOVkhROEJBZjhFQkFNQ0I0QXdEQVlEVlIwVEFRSC9CQUl3QURBZApCZ05WSFE0RUZnUVVQV1NUSmZrR1J3T3IvTlRqL0FldkZhR3pVYkV3SHdZRFZSMGpCQmd3Rm9BVUo2VTAydjQzCmJKbmV3citpc3JubnltY3RpUG93YWdZSUtnTUVCUVlIQ0FFRVhuc2lZWFIwY25NaU9uc2lhR1l1UVdabWFXeHAKWVhScGIyNGlPaUp2Y21jeExtUmxjR0Z5ZEcxbGJuUXhJaXdpYUdZdVJXNXliMnhzYldWdWRFbEVJam9pWVhCdwpWWE5sY2lJc0ltaG1MbFI1Y0dVaU9pSmpiR2xsYm5RaWZYMHdDZ1lJS29aSXpqMEVBd0lEU0FBd1JRSWhBS1FPCnBwdmlhTGY0VXZzaU0zOWVsYlNjYzdvcHdVRnRkU25MVkkwc3NUNHhBaUJXWUwxejFVNkhJdTQ2clMvaThOS3gKMTVUVENMQUNxd01jYjNPOE9OaVE2UT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
        byte[] mockClientIdentityBytes = Base64.getDecoder().decode(mockClientIdentityEncodedBase64);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(stub.getCreator()).thenReturn(mockClientIdentityBytes);
        ClientIdentity clientIdentity = new ClientIdentity(stub);

        return clientIdentity;
    }
}

package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

public class RegisterResponseTest {

    //TODO: Add test to ensure passing NULL fails + Is XML tag a mandate?
    @Test
    public void toXML() throws Exception {
        RegisterResponse registerResponse = new RegisterResponse("txn123", "reg123", 0, "Success");
        String responseXml = registerResponse.toXML();

        Assert.assertEquals(expectedXML(), responseXml);
    }

    private String expectedXML() {
        return "<registerResponse txnId=\"txn123\" registrationId=\"reg123\" errCode=\"0\" errInfo=\"Success\"/>";
    }
}
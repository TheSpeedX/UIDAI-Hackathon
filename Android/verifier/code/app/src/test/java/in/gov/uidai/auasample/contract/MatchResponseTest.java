package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

public class MatchResponseTest {

    //TODO: Add test to ensure passing NULL fails + Is XML tag a mandate?
    @Test
    public void toXML() throws Exception {
        MatchResponse registerResponse = new MatchResponse("txn123", "200", "sha123", "202004108T014437", 0, "Success");

        String responseXml = registerResponse.toXML();

        Assert.assertEquals(expectedXML(), responseXml);
    }

    private String expectedXML() {
        return "<matchResponse txnId=\"txn123\" responseCode=\"200\" matchedUserIdHash=\"sha123\" dateTime=\"202004108T014437\" errCode=\"0\" errInfo=\"Success\"/>";
    }
}
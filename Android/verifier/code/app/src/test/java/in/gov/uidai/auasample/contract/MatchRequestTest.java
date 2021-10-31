package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

public class MatchRequestTest {

    @Test
    public void fromXML() throws Exception {
        String matchRequestXML = getmatchRequestXML();

        MatchRequest matchRequest = MatchRequest.fromXML(matchRequestXML);

        Assert.assertEquals("uuid26363", matchRequest.getTxnId());
        Assert.assertEquals("43562", matchRequest.getUserId());
        Assert.assertEquals("PDS", matchRequest.getDomainName());
        Assert.assertEquals("7777", matchRequest.getLast4DigitsOfAadhaar());
    }

    private String getmatchRequestXML() {
        return "<matchRequest txnId=\"uuid26363\" domainName=\"PDS\" userId=\"43562\" last4DigitsOfAadhaar=\"7777\"/>";
    }

}
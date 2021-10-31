package in.gov.uidai.auasample.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import in.gov.uidai.auasample.input.contract.RegisterRequest;
import in.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc;

public class RegisterRequestTest {

    @Test
    public void ShouldReturnRegisterRequestFromGivenXML() throws Exception {
        String registerRequestXML = getRegisterRequestXML();

        RegisterRequest registerRequest = RegisterRequest.fromXML(registerRequestXML);

        Assert.assertEquals("uuid26363", registerRequest.getTxnId());
        Assert.assertEquals("43562", registerRequest.getUserId());
        Assert.assertEquals("PDS", registerRequest.getDomainName());
        Assert.assertEquals("John Doe", registerRequest.getUserName());
        Assert.assertEquals("7777", registerRequest.getLast4DigitsOfAadhaar());
    }

    @Test
    public void ShouldReturnRegisterRequestFromToXML() throws Exception {
        String registerRequestXML = getRegisterRequestXML();

        RegisterRequest registerRequest = RegisterRequest.fromXML(registerRequestXML);

        Assert.assertEquals(getRegisterRequestXML(), registerRequest.toXML());

    }

//    @Test
//    public void ShouldReturnOfflineKycObject() throws Exception {
//        String registerRequestXML = getRegisterRequestXML();
//
//        RegisterRequest registerRequest = RegisterRequest.fromXML(registerRequestXML);
//
//        OfflineEkyc offlineEkyc = registerRequest.getOfflineEkyc();
//        Assert.assertEquals("4426", offlineEkyc.getLast4DigitUid());
//        Assert.assertEquals("PhotoString", offlineEkyc.uidData.getPht());
//    }

    private String getRegisterRequestXML() {
        String s = "&lt;OfflineEkyc referenceId=&quot;442620200417143053086&quot;&gt;&lt;UidData&gt;&lt;Pht&gt;PhotoString&lt;/Pht&gt;&lt;/UidData&gt;&lt;/OfflineEkyc&gt;";
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<registerRequest txnId=\"uuid26363\" domainName=\"PDS\" userId=\"43562\" userName=\"John Doe\" last4DigitsOfAadhaar=\"7777\" eKycSignedDoc=\"" + s + "\" />";
    }
}
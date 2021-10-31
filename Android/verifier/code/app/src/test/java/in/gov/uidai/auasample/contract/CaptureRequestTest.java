package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Assert;
import org.junit.Test;

public class CaptureRequestTest {
    @Test
    public void ShouldCreateCaptureRequestFromRequestXML() throws Exception {
        String pidRequestXML = GetPidRequestXML();

        CaptureRequest captureRequest = CaptureRequest.fromXML(pidRequestXML);

        Assert.assertEquals("Demographic Attributes as specified in authentication API", captureRequest.demo);
        Assert.assertEquals(2, captureRequest.custOpts.nameValues.size());
    }

    private String GetPidRequestXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PidOptions ver=\"1.0\" env=\"p\">\n" +
                "   <Opts fCount=\"\" fType=\"1\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"\" timeout=\"\" otp=\"\" wadh=\"\" posh=\"\" />\n" +
                "   <Demo>Demographic Attributes as specified in authentication API</Demo>\n" +
                "   <CustOpts>\n" +
                "      <Param name=\"txnId\" value=\"uuid value to correlate between Capture &amp; Register intent calls\"/>\n" +
                "      <Param name=\"txnId\" value=\"uuid value to correlate between Capture &amp; Register intent calls\"/>\n" +
                "   </CustOpts>\n" +
                "   <BioData>\n" +
                "      <PidData>\n" +
                "         <Resp errCode=\"\" errInfo=\"\" fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" nmPoints=\"\" qScore=\"\" />\n" +
                "       <DeviceInfo dpId=\"\" rdsId=\"\" rdsVer=\"\" dc=\"\" mi=\"\" mc=\"\">\n" +
                "          <Additional_Info>" +
                "               <Info name=\"hi\" value=\"\" />\n" +
                "               <Info name=\"bye\" value=\"what?\" />\n" +
                "           </Additional_Info>\n" +
                "       </DeviceInfo>" +
                "         <Skey ci=\"\">encrypted and encoded session key</Skey>\n" +
                "         <Hmac>SHA-256 Hash of Pid block, encrypted and then encoded</Hmac>\n" +
                "         <Data type=\"X|P\">base-64 encoded encrypted pid block</Data>\n" +
                "         <CustOpts>\n" +
                "            <Param name=\"\" value=\"\" />\n" +
                "         </CustOpts>\n" +
                "      </PidData>\n" +
                "   </BioData>\n" +
                "</PidOptions>";
    }
}
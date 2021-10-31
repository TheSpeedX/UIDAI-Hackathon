package in.gov.uidai.auasample.input.contract;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaptureRdResponseTest {

    @Test
    public void ShouldConstructCaptureRdResponseFromGivenJson() {
        String responseJSON = GetRdCaptureResponseJSON();

        CaptureRdResponse captureRdResponse = CaptureRdResponse.fromJson(responseJSON);
        PidData pidData = captureRdResponse.getPidData();

        assertEquals("string", captureRdResponse.getTxnId());
        assertEquals(2, captureRdResponse.getBestImageIndex());
        assertEquals(1, pidData.custOpts.nameValues.size());
        assertEquals("hma1", pidData.hmac);
        assertEquals("X", pidData.data.type);
        assertEquals("20220331", pidData.skey.ci);
        assertEquals(3, pidData.resp.pCount);
        assertEquals("string", pidData.deviceInfo.dc);
        assertEquals(1, pidData.deviceInfo.additional_info.nameValues.size());
    }

    private String GetRdCaptureResponseJSON() {
        return "{\n" +
                "  \"txnId\": \"string\",\n" +
                "  \"bestImageIndex\": 2,\n" +
                "  \"pidData\": {\n" +
                "    \"resp\": {\n" +
                "      \"errCode\": 0,\n" +
                "      \"errInfo\": \"string\",\n" +
                "      \"fCount\": 0,\n" +
                "      \"fType\": 0,\n" +
                "      \"iCount\": 0,\n" +
                "      \"iType\": 0,\n" +
                "      \"pCount\": 3,\n" +
                "      \"pType\": \"JPEG2000\",\n" +
                "      \"nmPoints\": 0,\n" +
                "      \"qScore\": \"1.0\"\n" +
                "    },\n" +
                "    \"sKey\": {\n" +
                "      \"ci\": \"20220331\",\n" +
                "      \"value\": \"string\"\n" +
                "    },\n" +
                "    \"hmac\": \"hma1\",\n" +
                "    \"data\": {\n" +
                "      \"type\": \"X\",\n" +
                "      \"value\": \"string\"\n" +
                "    },\n" +
                "    \"custOpts\": {\n" +
                "      \"param\": [\n" +
                "        {\n" +
                "          \"name\": \"string\",\n" +
                "          \"value\": \"string\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"deviceInfo\": {\n" +
                "      \"dpId\": \"string\",\n" +
                "      \"rdsId\": \"string\",\n" +
                "      \"rdsVer\": \"string\",\n" +
                "      \"dc\": \"string\",\n" +
                "      \"mi\": \"string\",\n" +
                "      \"mc\": \"string\",\n" +
                "      \"additionalInfo\": {\n" +
                "        \"info\": [\n" +
                "          {\n" +
                "            \"name\": \"string\",\n" +
                "            \"value\": \"string\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;
import org.junit.Test;

public class CaptureResponseTest {

    @Test
    public void ShouldCreateCaptureResultFromPidResponse() {
        CaptureRdResponse captureRdResponse = BuildPidResponse("\"custOpts\": {\n" +
                "      \"param\": [\n" +
                "        {\n" +
                "          \"name\": \"string\",\n" +
                "          \"value\": \"string\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n");
        String txnStatus = "PID_CREATED";

        CaptureResponse captureResponse = CaptureResponse.fromCaptureRdResponse(captureRdResponse, txnStatus);

        NameValue txnStatusOpt = captureResponse.custOpts.nameValues.get(2);
        NameValue txnIdOpt = captureResponse.custOpts.nameValues.get(1);

        Assert.assertEquals(3, captureResponse.custOpts.nameValues.size());
        Assert.assertEquals("txnId", txnIdOpt.getName());
        Assert.assertEquals(captureRdResponse.getTxnId(), txnIdOpt.getValue());
        Assert.assertEquals("txnStatus", txnStatusOpt.getName());
        Assert.assertEquals(txnStatus, txnStatusOpt.getValue());
    }

    @Test
    public void ShouldNotFailIfCustOptsNotPassed() {
        CaptureRdResponse captureRdResponse = BuildPidResponse("");
        String txnStatus = "PID_CREATED";

        CaptureResponse captureResponse = CaptureResponse.fromCaptureRdResponse(captureRdResponse, txnStatus);


        Assert.assertEquals(2, captureResponse.custOpts.nameValues.size());
    }

    @Test
    public void ShouldNotFailIfCustOptsHasNoNameValues() {
        CaptureRdResponse captureRdResponse = BuildPidResponse("\"custOpts\": {\n" +
                "    },\n");
        String txnStatus = "PID_CREATED";

        CaptureResponse captureResponse = CaptureResponse.fromCaptureRdResponse(captureRdResponse, txnStatus);


        Assert.assertEquals(2, captureResponse.custOpts.nameValues.size());
    }

    @Test
    public void ShouldIgnoreTxnStatusIfNotPassed() {
        CaptureRdResponse captureRdResponse = BuildPidResponse("\"custOpts\": {\n" +
                "      \"param\": [\n" +
                "        {\n" +
                "          \"name\": \"string\",\n" +
                "          \"value\": \"string\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n");
        String txnStatus = "PID_CREATED";

        CaptureResponse captureResponse = CaptureResponse.fromCaptureRdResponse(captureRdResponse, "");


        NameValue txnIdOpt = captureResponse.custOpts.nameValues.get(1);
        NameValue stringOpt = captureResponse.custOpts.nameValues.get(0);

        Assert.assertEquals(2, captureResponse.custOpts.nameValues.size());
        Assert.assertEquals("txnId", txnIdOpt.getName());
        Assert.assertEquals(captureRdResponse.getTxnId(), txnIdOpt.getValue());
        Assert.assertEquals("string", stringOpt.getName());
        Assert.assertEquals("string", stringOpt.getValue());
    }

    @Test
    public void ShouldGenerateXMLString() throws Exception {
        CaptureResponse captureResponse = GetCaptureResult();
        String actualXmlString = captureResponse.toXML();

        Assert.assertEquals(ExpectedXmlString(), actualXmlString);
    }


    private String ExpectedXmlString() {
        return "<PidData><Resp errCode=\"0\" errInfo=\"string\" fCount=\"0\" fType=\"0\" iCount=\"0\" iType=\"0\" pCount=\"0\" pType=\"0\" nmPoints=\"0\" qScore=\"0\"/><DeviceInfo dpId=\"string\" rdsId=\"string\" rdsVer=\"string\" dc=\"string\" mi=\"string\" mc=\"string\"><Additional_Info><Info name=\"string\" value=\"string\"/></Additional_Info></DeviceInfo><Skey ci=\"20220331\">string</Skey><Hmac>string</Hmac><Data type=\"X\">string</Data><CustOpts><Param name=\"string\" value=\"string\"/><Param name=\"txnId\" value=\"123\"/><Param name=\"txnStatus\" value=\"PID_CREATED\"/></CustOpts></PidData>";
    }

    private CaptureResponse GetCaptureResult() {
        CaptureRdResponse captureRdResponse = BuildPidResponse("\"custOpts\": {\n" +
                "      \"param\": [\n" +
                "        {\n" +
                "          \"name\": \"string\",\n" +
                "          \"value\": \"string\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n");
        String txnStatus = "PID_CREATED";

        return CaptureResponse.fromCaptureRdResponse(captureRdResponse, txnStatus);
    }


    private CaptureRdResponse BuildPidResponse(String custOpts) {
        String pidResponseJson = GetPidResponseJSON(custOpts);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(pidResponseJson, CaptureRdResponse.class);
    }

    private String GetPidResponseJSON(String custOpts) {
        return "{\n" +
                "  \"txnId\": \"123\",\n" +
                "  \"bestImageIndex\": \"2\",\n" +
                "  \"pidData\": {\n" +
                "    \"resp\": {\n" +
                "      \"errCode\": 0,\n" +
                "      \"errInfo\": \"string\",\n" +
                "      \"fCount\": 0,\n" +
                "      \"fType\": 0,\n" +
                "      \"iCount\": 0,\n" +
                "      \"iType\": 0,\n" +
                "      \"pCount\": 0,\n" +
                "      \"pType\": 0,\n" +
                "      \"nmPoints\": 0,\n" +
                "      \"qScore\": 0\n" +
                "    },\n" +
                "    \"sKey\": {\n" +
                "      \"ci\": \"20220331\",\n" +
                "      \"value\": \"string\"\n" +
                "    },\n" +
                "    \"hmac\": \"string\",\n" +
                "    \"data\": {\n" +
                "      \"type\": \"X\",\n" +
                "      \"value\": \"string\"\n" +
                "    },\n" + custOpts +
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
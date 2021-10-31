package in.gov.uidai.auasample.contract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import in.gov.uidai.auasample.input.contract.CaptureRdRequest;

public class CaptureRdRequestTest {

    @Test
    public void ShouldParseCaptureRdRequestToJsonString() {
        CaptureRdRequest captureRdRequest = getCaptureRdRequest();

        String captureRequestJson = captureRdRequest.toJson();

        Assert.assertEquals(expectedJson(), captureRequestJson);
    }

    @NotNull
    private CaptureRdRequest getCaptureRdRequest() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(expectedJson(), CaptureRdRequest.class);
    }

    private String expectedJson() {
        return "{\"header\":{\"appId\":\"uidai_face_auth_ver_1_0_3\",\"deviceId\":\"99000086247185334\"},\"faceData\":{\"images\":[\"image1\"]},\"pidOptions\":{\"ver\":\"1.0\",\"env\":\"p\",\"opts\":{\"fCount\":0,\"fType\":1,\"iCount\":0,\"iType\":0,\"pCount\":0,\"pType\":\"0\",\"format\":0,\"pidVer\":\"\",\"timeout\":\"\",\"otp\":\"\",\"wadh\":\"\",\"posh\":\"\"},\"demo\":\"Demographic Attributes as specified in authentication API\",\"custOpts\":{\"param\":[{\"name\":\"txnId\",\"value\":\"uuid value to correlate between Capture & Register intent calls\"},{\"name\":\"txnId\",\"value\":\"uuid value to correlate between Capture & Register intent calls\"}]},\"bioData\":{\"pidData\":[{\"resp\":{\"errCode\":0,\"errInfo\":\"\",\"fCount\":0,\"fType\":0,\"iCount\":0,\"iType\":0,\"pCount\":0,\"pType\":\"0\",\"nmPoints\":0,\"qScore\":\"0\"},\"deviceInfo\":{\"dpId\":\"\",\"rdsId\":\"\",\"rdsVer\":\"\",\"dc\":\"\",\"mi\":\"\",\"mc\":\"\",\"additionalInfo\":{\"info\":[{\"name\":\"hi\",\"value\":\"\"},{\"name\":\"bye\",\"value\":\"what?\"}]}},\"sKey\":{\"ci\":\"\",\"value\":\"encrypted and encoded session key\"},\"hmac\":\"SHA-256 Hash of Pid block, encrypted and then encoded\",\"data\":{\"type\":\"X|P\",\"value\":\"base-64 encoded encrypted pid block\"},\"custOpts\":{\"param\":[{\"name\":\"\",\"value\":\"\"}]}}]}}}";
    }

}
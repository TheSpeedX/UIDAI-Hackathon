package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;

@JacksonXmlRootElement(localName = "PidData")
public class CaptureResponse extends PidData {
    public CaptureResponse(Resp resp, DeviceInfo deviceInfo, Skey skey, String hmac, Data data, CustOpts custOpts) {
        super(resp, deviceInfo, skey, hmac, data, custOpts);
    }

    public String toXML() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(this);
    }

    public static CaptureResponse fromCaptureRdResponse(CaptureRdResponse rdResponse, String txnStatus) {
        PidData pd = rdResponse.getPidData();
        CaptureResponse captureResponse = new CaptureResponse(pd.resp, pd.deviceInfo, pd.skey, pd.hmac, pd.data, pd.custOpts);

        if (captureResponse.custOpts == null) {
            captureResponse.custOpts = new CustOpts();
        }

        NameValue txnIdOpt = new NameValue("txnId", rdResponse.getTxnId());
        captureResponse.custOpts.nameValues.add(txnIdOpt);

        if (!txnStatus.isEmpty()) {
            NameValue txnStatusOpt = new NameValue("txnStatus", txnStatus);
            captureResponse.custOpts.nameValues.add(txnStatusOpt);
        }

        return captureResponse;
    }

    public static CaptureResponse forError(int errorCode, String errorInfo, String txnId) {
        PidData pd = new PidData();
        pd.resp = new Resp();
        pd.resp.errCode = errorCode;
        pd.resp.errInfo = errorInfo;

        CaptureResponse captureResponse = new CaptureResponse(pd.resp, pd.deviceInfo, pd.skey, pd.hmac, pd.data, pd.custOpts);

        captureResponse.custOpts = new CustOpts();
        captureResponse.custOpts.nameValues = new ArrayList<>();

        NameValue txnIdOpt = new NameValue("txnId", txnId);
        captureResponse.custOpts.nameValues.add(txnIdOpt);

        return captureResponse;
    }

    public CaptureResponse() {
    }

    public static CaptureResponse fromXML(String inputXML) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(inputXML, CaptureResponse.class);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return resp.errCode == 0;
    }

    @JsonIgnore
    public String getErrInfo() {
        return resp.errInfo;
    }

    @JsonIgnore
    public int getErrCode() {
        return resp.errCode;
    }

    @JsonIgnore
    public String getTxnID() {
        if (custOpts == null) {
            return "";
        }

        for (NameValue nv : custOpts.nameValues) {
            if (nv.getName().equals("txnId")) {
                return nv.getValue();
            }
        }
        return "";
    }
}

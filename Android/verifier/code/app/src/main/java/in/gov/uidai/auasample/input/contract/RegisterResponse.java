package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "registerResponse")
public class RegisterResponse {
    @JacksonXmlProperty(isAttribute = true)
    private String txnId;
    @JacksonXmlProperty(isAttribute = true)
    private String registrationId;
    @JacksonXmlProperty(isAttribute = true)
    private int errCode;
    @JacksonXmlProperty(isAttribute = true)
    private String errInfo;

    public RegisterResponse(String txnId, String registrationId, int errCode, String errInfo) {
        this.txnId = txnId;
        this.registrationId = registrationId;
        this.errCode = errCode;
        this.errInfo = errInfo;
    }

    public String getTxnId() {
        return txnId;
    }

    public String toXML() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(this);
    }

    public RegisterResponse() {
    }

    public static RegisterResponse fromXML(String inputXML) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(inputXML, RegisterResponse.class);
    }

    public boolean isSuccess() {
        return errCode == 0;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public int getErrCode() {
        return errCode;
    }
}

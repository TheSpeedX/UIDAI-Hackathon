package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import in.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc;

@JacksonXmlRootElement(localName = "registerRequest")
public class RegisterRequest {
    @JacksonXmlProperty(isAttribute = true)
    private String txnId;
    @JacksonXmlProperty(isAttribute = true)
    private String domainName;
    @JacksonXmlProperty(isAttribute = true)
    private String userId;
    @JacksonXmlProperty(isAttribute = true)
    private String userName;
    @JacksonXmlProperty(isAttribute = true)
    private String last4DigitsOfAadhaar;
    @JacksonXmlProperty(isAttribute = true)
    private String eKycSignedDoc;

    public String getTxnId() {
        return txnId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getLast4DigitsOfAadhaar() {
        return last4DigitsOfAadhaar;
    }

    public static RegisterRequest fromXML(String registerRequestXML) throws Exception {
        return new XmlMapper().readValue(registerRequestXML, RegisterRequest.class);
    }

    public String toXML() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(this);
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLast4DigitsOfAadhaar(String last4DigitsOfAadhaar) {
        this.last4DigitsOfAadhaar = last4DigitsOfAadhaar;
    }

    public void seteKycSignedDoc(String eKycSignedDoc) {
        this.eKycSignedDoc = eKycSignedDoc;
    }
}

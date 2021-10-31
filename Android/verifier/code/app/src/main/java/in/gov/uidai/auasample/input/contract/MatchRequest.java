package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "matchRequest")
public class MatchRequest {
    @JacksonXmlProperty(isAttribute = true)
    private String txnId;
    @JacksonXmlProperty(isAttribute = true)
    private String domainName;
    @JacksonXmlProperty(isAttribute = true)
    private String userId;
    @JacksonXmlProperty(isAttribute = true)
    private String last4DigitsOfAadhaar;

    public String getTxnId() {
        return txnId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getUserId() {
        return userId;
    }

    public String getLast4DigitsOfAadhaar() {
        return last4DigitsOfAadhaar;
    }

    public static MatchRequest fromXML(String matchRequestXML) throws Exception {
        return new XmlMapper().readValue(matchRequestXML, MatchRequest.class);
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

    public void setLast4DigitsOfAadhaar(String last4DigitsOfAadhaar) {
        this.last4DigitsOfAadhaar = last4DigitsOfAadhaar;
    }
}
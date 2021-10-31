package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "matchResponse")
public class MatchResponse {
    @JacksonXmlProperty(isAttribute = true)
    private String txnId;
    @JacksonXmlProperty(isAttribute = true)
    private String responseCode;
    @JacksonXmlProperty(isAttribute = true)
    private String matchedUserIdHash;
    @JacksonXmlProperty(isAttribute = true)
    private String dateTime;
    @JacksonXmlProperty(isAttribute = true)
    private int errCode;
    @JacksonXmlProperty(isAttribute = true)
    private String errInfo;


    public MatchResponse(String txnId, String responseCode, String matchedUserIdHash, String dateTime, int errCode, String errInfo) {
        this.txnId = txnId;
        this.responseCode = responseCode;
        this.matchedUserIdHash = matchedUserIdHash;
        this.dateTime = dateTime;
        this.errCode = errCode;
        this.errInfo = errInfo;
    }

    public MatchResponse(String txnId, String dateTime, int errCode, String errInfo) {
        this.txnId = txnId;
        this.dateTime = dateTime;
        this.errCode = errCode;
        this.errInfo = errInfo;
    }

    public MatchResponse() {
    }

    public String toXML() throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(this);
    }

    public static MatchResponse fromXML(String inputXML) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(inputXML, MatchResponse.class);
    }


    public boolean isSuccess() {
        return errCode == 0;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getMatchedUserIdHash() {
        return matchedUserIdHash;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getErrInfo() {
        return errInfo;
    }
}

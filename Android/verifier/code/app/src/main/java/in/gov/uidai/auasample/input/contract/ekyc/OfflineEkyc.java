package in.gov.uidai.auasample.input.contract.ekyc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "registerRequest")
public class OfflineEkyc {
    @JacksonXmlProperty(localName = "UidData")
    public UidData uidData;

    @JacksonXmlProperty(isAttribute = true)
    private String referenceId;

    public OfflineEkyc() {
    }

    public OfflineEkyc(UidData uidData, String referenceId) {
        this.referenceId = referenceId;
        this.uidData = uidData;
    }

    public static OfflineEkyc fromXML(String ekycXML) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return xmlMapper.readValue(ekycXML, OfflineEkyc.class);
    }

    public String getLast4DigitUid() {
        if (referenceId == null || referenceId.length() < 4) {
            return "";
        }
        return referenceId.substring(0, 4);
    }

    public boolean isValid() {
        return !getLast4DigitUid().isEmpty() && !uidData.getPht().isEmpty();
    }
}

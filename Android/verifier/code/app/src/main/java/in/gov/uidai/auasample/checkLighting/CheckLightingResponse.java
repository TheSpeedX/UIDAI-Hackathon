package in.gov.uidai.auasample.checkLighting;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "checkLighteningResponse")
public class CheckLightingResponse {
    @JacksonXmlProperty(isAttribute = true)
    public String txnId;
    @JacksonXmlProperty(isAttribute = true)
    public String responseCode;
    @JacksonXmlProperty(isAttribute = true)
    public String dateTime;
    @JacksonXmlProperty(isAttribute = true)
    public int errCode;
    @JacksonXmlProperty(isAttribute = true)
    public String errInfo;

    public boolean isSuccess() {
        return errCode == 0;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public static CheckLightingResponse fromXML(String inputXML) throws Exception {
        return new XmlMapper().readValue(inputXML, CheckLightingResponse.class);
    }
}

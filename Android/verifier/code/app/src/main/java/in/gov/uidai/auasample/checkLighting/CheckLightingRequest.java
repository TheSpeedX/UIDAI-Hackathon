package in.gov.uidai.auasample.checkLighting;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "checkLightingRequest")
public class CheckLightingRequest {
    @JacksonXmlProperty(isAttribute = true)
    public String txnId;

    @JacksonXmlProperty(isAttribute = true)
    public String language;

    public String toXml() throws Exception {
        return new XmlMapper().writeValueAsString(this);
    }
}
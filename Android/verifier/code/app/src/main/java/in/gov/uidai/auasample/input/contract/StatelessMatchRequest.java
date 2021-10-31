package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "statelessMatchRequest")
public class StatelessMatchRequest {
    @JacksonXmlProperty(isAttribute = true)
    public String requestId;

    @JacksonXmlProperty(isAttribute = true)
    public String signedDocument;

    @JacksonXmlProperty(isAttribute = true)
    public String language;

    @JacksonXmlProperty(isAttribute = true)
    public String enableAutoCapture;

    public String toXml() throws Exception {
        return new XmlMapper().writeValueAsString(this);
    }
}
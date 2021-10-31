package in.gov.uidai.auasample.deviceCheck;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "checkDeviceRequest")
public class CheckDeviceRequest {
    @JacksonXmlProperty(isAttribute = true)
    public String txnId;

    @JacksonXmlProperty(isAttribute = true)
    public String env;

    @JacksonXmlProperty(isAttribute = true)
    public String language;

    public String toXml() throws Exception {
        return new XmlMapper().writeValueAsString(this);
    }
}

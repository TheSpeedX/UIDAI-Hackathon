package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "statelessMatchResponse")
public class StatelessMatchResponse {
    @JacksonXmlProperty(isAttribute = true)
    public String requestId;
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

    public static StatelessMatchResponse fromXML(String inputXML) throws Exception {
        return new XmlMapper().readValue(inputXML, StatelessMatchResponse.class);
    }
}

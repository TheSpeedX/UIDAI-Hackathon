package in.gov.uidai.auasample.deviceCheck;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "checkDeviceResponse")
public class CheckDeviceResponse {
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
    @JacksonXmlProperty(isAttribute = true)
    public String totalTime;

    @JacksonXmlProperty(localName = "success")
    public String success;

    public boolean isSuccess() {
        return errCode == 0;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public static CheckDeviceResponse fromXML(String inputXML) throws Exception {
        return new XmlMapper().readValue(inputXML, CheckDeviceResponse.class);
    }
}

package in.gov.uidai.auasample.deviceInfo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.gson.annotations.SerializedName;

@JacksonXmlRootElement(localName = "DeviceInfo")
public class DeviceInfoResponse {
    @JacksonXmlProperty(isAttribute = true)
    private String dpId;
    @JacksonXmlProperty(isAttribute = true)
    private String rdsId;
    @JacksonXmlProperty(isAttribute = true)
    private String rdsVer;
    @JacksonXmlProperty(isAttribute = true)
    private String dc;
    @JacksonXmlProperty(isAttribute = true)
    private String mi;
    @JacksonXmlProperty(isAttribute = true)
    private String mc;
    @SerializedName("additionalInfo")
    @JacksonXmlProperty(localName = "Additional_Info")
    private AdditionalInfo additionalInfo;

    public static DeviceInfoResponse fromXML(String inputXML) throws Exception {
        return new XmlMapper().readValue(inputXML, DeviceInfoResponse.class);
    }

    public String getDpId() {
        return dpId;
    }

    public String getRdsId() {
        return rdsId;
    }

    public String getRdsVer() {
        return rdsVer;
    }

    public String getDc() {
        return dc;
    }

    public String getMi() {
        return mi;
    }

    public String getMc() {
        return mc;
    }

    public String getErrorCode() {
        return fetchInfoTag("errorCode");
    }

    public String getErrorMsg() {
        return fetchInfoTag("errorMsg");
    }

    public String getApkVersion() {
        return fetchInfoTag("apkVersion");
    }

    public String getAppId() {
        return fetchInfoTag("appId");
    }

    public String getenv() {
        return fetchInfoTag("environment");
    }

    public String getLanguages() {
        return fetchInfoTag("languages");
    }

    private String fetchInfoTag(String infoTag) {
        if (additionalInfo == null) {
            return "";
        }

        for (NameValue nv : additionalInfo.nameValues) {
            if (nv.getName().equals(infoTag)) {
                return nv.getValue();
            }
        }
        return "";
    }
}
package in.gov.uidai.auasample.deviceInfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class NameValue {
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private String value;

    public NameValue() {

    }

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
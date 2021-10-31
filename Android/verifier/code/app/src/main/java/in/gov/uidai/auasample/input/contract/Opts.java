package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Opts {
    @JacksonXmlProperty(isAttribute = true)
    public int fCount;
    @JacksonXmlProperty(isAttribute = true)
    public int fType;
    @JacksonXmlProperty(isAttribute = true)
    public int iCount;
    @JacksonXmlProperty(isAttribute = true)
    public int iType;
    @JacksonXmlProperty(isAttribute = true)
    public int pCount;
    @JacksonXmlProperty(isAttribute = true)
    public String pType;
    @JacksonXmlProperty(isAttribute = true)
    public int format;
    @JacksonXmlProperty(isAttribute = true)
    public String pidVer;
    @JacksonXmlProperty(isAttribute = true)
    public String timeout;
    @JacksonXmlProperty(isAttribute = true)
    public String otp;
    @JacksonXmlProperty(isAttribute = true)
    public String wadh;
    @JacksonXmlProperty(isAttribute = true)
    public String posh;
}


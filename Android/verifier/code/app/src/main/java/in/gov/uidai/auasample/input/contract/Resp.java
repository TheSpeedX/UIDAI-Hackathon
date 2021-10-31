package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Resp {
    @JacksonXmlProperty(isAttribute = true)
    public int errCode;
    @JacksonXmlProperty(isAttribute = true)
    public String errInfo;
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
    public int nmPoints;
    @JacksonXmlProperty(isAttribute = true)
    public String qScore;
}

package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PidOptions {
    @JacksonXmlProperty(isAttribute = true)
    private String ver;

    @JacksonXmlProperty(isAttribute = true)
    private String env;

    @JacksonXmlProperty(localName = "Opts")
    public Opts opts;

    @JacksonXmlProperty(localName = "Demo")
    public String demo;

    @JacksonXmlProperty(localName = "CustOpts")
    public CustOpts custOpts;

    @JacksonXmlProperty(localName = "BioData")
    public BioData bioData;

    public String getVer() {
        return ver;
    }

    public String getEnv() {
        return env;
    }
}


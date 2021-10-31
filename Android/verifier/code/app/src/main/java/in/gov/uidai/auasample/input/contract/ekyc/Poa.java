package in.gov.uidai.auasample.input.contract.ekyc;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

class Poa {
    @JacksonXmlProperty(isAttribute = true)
    private String careof;
    @JacksonXmlProperty(isAttribute = true)
    private String country;
    @JacksonXmlProperty(isAttribute = true)
    private String dist;
    @JacksonXmlProperty(isAttribute = true)
    private String house;
    @JacksonXmlProperty(isAttribute = true)
    private String landmark;
    @JacksonXmlProperty(isAttribute = true)
    private String loc;
    @JacksonXmlProperty(isAttribute = true)
    private String pc;
    @JacksonXmlProperty(isAttribute = true)
    private String po;
    @JacksonXmlProperty(isAttribute = true)
    private String state;
    @JacksonXmlProperty(isAttribute = true)
    private String street;
    @JacksonXmlProperty(isAttribute = true)
    private String subdist;
    @JacksonXmlProperty(isAttribute = true)
    private String vtc;

}

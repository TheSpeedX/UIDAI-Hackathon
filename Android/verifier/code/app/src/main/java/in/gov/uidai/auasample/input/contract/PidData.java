package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.gson.annotations.SerializedName;

public class PidData {

    @JacksonXmlProperty(localName = "Resp")
    public Resp resp;

    @JacksonXmlProperty(localName = "DeviceInfo")
    public DeviceInfo deviceInfo;

    @SerializedName("sKey")
    @JacksonXmlProperty(localName = "Skey")
    public Skey skey;

    @JacksonXmlProperty(localName = "Hmac")
    public String hmac;

    @JacksonXmlProperty(localName = "Data")
    public Data data;

    @JacksonXmlProperty(localName = "CustOpts")
    public CustOpts custOpts;

    public PidData() {

    }

    public PidData(Resp resp, DeviceInfo deviceInfo, Skey skey, String hmac, Data data, CustOpts custOpts) {

        this.resp = resp;
        this.deviceInfo = deviceInfo;
        this.skey = skey;
        this.hmac = hmac;
        this.data = data;
        this.custOpts = custOpts;
    }


}

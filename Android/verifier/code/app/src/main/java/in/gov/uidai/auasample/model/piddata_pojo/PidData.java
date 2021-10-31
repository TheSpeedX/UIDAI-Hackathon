package in.gov.uidai.auasample.model.piddata_pojo;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@XStreamInclude({Data.class,Skey.class,Resp.class,CustOpts.class})
@XStreamAlias("PidData")
public class PidData
{
    private String Hmac = "";

    private Resp Resp;

    private DeviceInfo DeviceInfo;

    private Data Data;

    private Skey Skey;

    private CustOpts CustOpts;

    public String getHmac ()
    {
        return Hmac;
    }

    public void setHmac (String Hmac)
    {
        this.Hmac = Hmac;
    }

    public Resp getResp ()
    {
        return Resp;
    }

    public void setResp (Resp Resp)
    {
        this.Resp = Resp;
    }

    public in.gov.uidai.auasample.model.piddata_pojo.DeviceInfo getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(in.gov.uidai.auasample.model.piddata_pojo.DeviceInfo deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public Data getData ()
    {
        return Data;
    }

    public void setData (Data Data)
    {
        this.Data = Data;
    }

    public Skey getSkey ()
    {
        return Skey;
    }

    public void setSkey (Skey Skey)
    {
        this.Skey = Skey;
    }

    public CustOpts getCustOpts ()
    {
        return CustOpts;
    }

    public void setCustOpts (CustOpts CustOpts)
    {
        this.CustOpts = CustOpts;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Hmac = "+Hmac+", Resp = "+Resp+", DeviceInfo = "+DeviceInfo+", Data = "+Data+", Skey = "+Skey+", CustOpts = "+CustOpts+"]";
    }
}

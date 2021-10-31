package in.gov.uidai.auasample.model.authxml_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Meta")
public class Meta
{
    @XStreamAsAttribute
    private String mc = "";
    @XStreamAsAttribute
    private String dpId = "";
    @XStreamAsAttribute
    private String rdsId = "";
    @XStreamAsAttribute
    private String mi = "";
    @XStreamAsAttribute
    private String rdsVer = "";
    @XStreamAsAttribute
    private String dc = "";

    public String getMc ()
    {
        return mc;
    }

    public void setMc (String mc)
    {
        this.mc = mc;
    }

    public String getDpId ()
    {
        return dpId;
    }

    public void setDpId (String dpId)
    {
        this.dpId = dpId;
    }

    public String getRdsId ()
    {
        return rdsId;
    }

    public void setRdsId (String rdsId)
    {
        this.rdsId = rdsId;
    }

    public String getMi ()
    {
        return mi;
    }

    public void setMi (String mi)
    {
        this.mi = mi;
    }

    public String getRdsVer ()
    {
        return rdsVer;
    }

    public void setRdsVer (String rdsVer)
    {
        this.rdsVer = rdsVer;
    }

    public String getDc ()
    {
        return dc;
    }

    public void setDc (String dc)
    {
        this.dc = dc;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [mc = "+mc+", dpId = "+dpId+", rdsId = "+rdsId+", mi = "+mi+", rdsVer = "+rdsVer+", dc = "+dc+"]";
    }
}

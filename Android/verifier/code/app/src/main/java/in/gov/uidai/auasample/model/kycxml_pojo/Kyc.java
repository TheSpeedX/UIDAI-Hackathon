package in.gov.uidai.auasample.model.kycxml_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Kyc")
public class Kyc
{
    @XStreamAsAttribute
    private String rc= "";
    @XStreamAsAttribute
    private String de= "";
    @XStreamAsAttribute
    private String ver= "";

    private String Rad= "";
    @XStreamAsAttribute
    private String lr= "";
    @XStreamAsAttribute
    private String pfr= "";
    @XStreamAsAttribute
    private String ra= "";

    public String getRc ()
    {
        return rc;
    }

    public void setRc (String rc)
    {
        this.rc = rc;
    }

    public String getDe ()
    {
        return de;
    }

    public void setDe (String de)
    {
        this.de = de;
    }

    public String getVer ()
    {
        return ver;
    }

    public void setVer (String ver)
    {
        this.ver = ver;
    }

    public String getRad ()
    {
        return Rad;
    }

    public void setRad (String Rad)
    {
        this.Rad = Rad;
    }

    public String getLr ()
    {
        return lr;
    }

    public void setLr (String lr)
    {
        this.lr = lr;
    }

    public String getPfr ()
    {
        return pfr;
    }

    public void setPfr (String pfr)
    {
        this.pfr = pfr;
    }

    public String getRa ()
    {
        return ra;
    }

    public void setRa (String ra)
    {
        this.ra = ra;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rc = "+rc+", de = "+de+", ver = "+ver+", Rad = "+Rad+", lr = "+lr+", pfr = "+pfr+", ra = "+ra+"]";
    }
}

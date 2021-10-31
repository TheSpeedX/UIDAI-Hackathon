package in.gov.uidai.auasample.model.authxml_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Uses")
public class Uses
{
    @XStreamAsAttribute
    private String pa = "";
    @XStreamAsAttribute
    private String bt = "";
    @XStreamAsAttribute
    private String pin = "";
    @XStreamAsAttribute
    private String pi = "";
    @XStreamAsAttribute
    private String bio = "";
    @XStreamAsAttribute
    private String pfa = "";
    @XStreamAsAttribute
    private String otp = "";

    public String getPa ()
    {
        return pa;
    }

    public void setPa (String pa)
    {
        this.pa = pa;
    }

    public String getBt ()
    {
        return bt;
    }

    public void setBt (String bt)
    {
        this.bt = bt;
    }

    public String getPin ()
    {
        return pin;
    }

    public void setPin (String pin)
    {
        this.pin = pin;
    }

    public String getPi ()
    {
        return pi;
    }

    public void setPi (String pi)
    {
        this.pi = pi;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getPfa ()
    {
        return pfa;
    }

    public void setPfa (String pfa)
    {
        this.pfa = pfa;
    }

    public String getOtp ()
    {
        return otp;
    }

    public void setOtp (String otp)
    {
        this.otp = otp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pa = "+pa+", bt = "+bt+", pin = "+pin+", pi = "+pi+", bio = "+bio+", pfa = "+pfa+", otp = "+otp+"]";
    }
}

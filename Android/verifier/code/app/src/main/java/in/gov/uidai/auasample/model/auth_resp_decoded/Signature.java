package in.gov.uidai.auasample.model.auth_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Signature")
public class Signature
{
    @XStreamAsAttribute
    private String xmlns;

    private String SignatureValue;

    private SignedInfo SignedInfo;

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }

    public String getSignatureValue ()
    {
        return SignatureValue;
    }

    public void setSignatureValue (String SignatureValue)
    {
        this.SignatureValue = SignatureValue;
    }

    public SignedInfo getSignedInfo ()
    {
        return SignedInfo;
    }

    public void setSignedInfo (SignedInfo SignedInfo)
    {
        this.SignedInfo = SignedInfo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [xmlns = "+xmlns+", SignatureValue = "+SignatureValue+", SignedInfo = "+SignedInfo+"]";
    }
}



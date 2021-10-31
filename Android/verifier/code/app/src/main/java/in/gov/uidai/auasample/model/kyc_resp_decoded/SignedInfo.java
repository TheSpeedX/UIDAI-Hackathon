package in.gov.uidai.auasample.model.kyc_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SignedInfo")
public class SignedInfo
{
    private Reference Reference;

    private CanonicalizationMethod CanonicalizationMethod;

    private SignatureMethod SignatureMethod;

    public Reference getReference ()
    {
        return Reference;
    }

    public void setReference (Reference Reference)
    {
        this.Reference = Reference;
    }

    public CanonicalizationMethod getCanonicalizationMethod ()
    {
        return CanonicalizationMethod;
    }

    public void setCanonicalizationMethod (CanonicalizationMethod CanonicalizationMethod)
    {
        this.CanonicalizationMethod = CanonicalizationMethod;
    }

    public SignatureMethod getSignatureMethod ()
    {
        return SignatureMethod;
    }

    public void setSignatureMethod (SignatureMethod SignatureMethod)
    {
        this.SignatureMethod = SignatureMethod;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Reference = "+Reference+", CanonicalizationMethod = "+CanonicalizationMethod+", SignatureMethod = "+SignatureMethod+"]";
    }
}


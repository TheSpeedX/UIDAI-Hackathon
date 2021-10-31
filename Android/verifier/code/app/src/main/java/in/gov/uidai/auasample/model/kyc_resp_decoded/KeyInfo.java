package in.gov.uidai.auasample.model.kyc_resp_decoded;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("KeyInfo")
public class KeyInfo
{
    private X509Data X509Data;

    public X509Data getX509Data ()
    {
        return X509Data;
    }

    public void setX509Data (X509Data X509Data)
    {
        this.X509Data = X509Data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [X509Data = "+X509Data+"]";
    }
}

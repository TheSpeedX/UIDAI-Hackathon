package in.gov.uidai.auasample.model.kyc_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("X509Data")
public class X509Data
{
    private String X509SubjectName;

    private String X509Certificate;

    public String getX509SubjectName ()
    {
        return X509SubjectName;
    }

    public void setX509SubjectName (String X509SubjectName)
    {
        this.X509SubjectName = X509SubjectName;
    }

    public String getX509Certificate ()
    {
        return X509Certificate;
    }

    public void setX509Certificate (String X509Certificate)
    {
        this.X509Certificate = X509Certificate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [X509SubjectName = "+X509SubjectName+", X509Certificate = "+X509Certificate+"]";
    }
}


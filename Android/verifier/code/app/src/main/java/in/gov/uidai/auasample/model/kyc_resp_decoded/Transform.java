package in.gov.uidai.auasample.model.kyc_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Transform")
public class Transform
{
    @XStreamAsAttribute
    private String Algorithm;

    public String getAlgorithm ()
    {
        return Algorithm;
    }

    public void setAlgorithm (String Algorithm)
    {
        this.Algorithm = Algorithm;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Algorithm = "+Algorithm+"]";
    }
}


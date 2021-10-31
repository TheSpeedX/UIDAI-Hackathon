package in.gov.uidai.auasample.model.kyc_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Transforms")
public class Transforms
{
    private Transform Transform;

    public Transform getTransform ()
    {
        return Transform;
    }

    public void setTransform (Transform Transform)
    {
        this.Transform = Transform;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Transform = "+Transform+"]";
    }
}


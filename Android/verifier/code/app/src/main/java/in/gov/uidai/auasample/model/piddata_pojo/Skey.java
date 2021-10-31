package in.gov.uidai.auasample.model.piddata_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("Skey")
@XStreamConverter(value= ToAttributedValueConverter.class, strings={"content"})
public class Skey
{
    @XStreamAsAttribute
    private String ci = "";

String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCi ()
    {
        return ci;
    }

    public void setCi (String ci)
    {
        this.ci = ci;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ci = "+ci+"]";
    }
}
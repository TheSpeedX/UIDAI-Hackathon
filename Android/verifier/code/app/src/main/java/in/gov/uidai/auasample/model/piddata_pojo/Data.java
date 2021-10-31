package in.gov.uidai.auasample.model.piddata_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("Data")
@XStreamConverter(value= ToAttributedValueConverter.class, strings={"content"})
public class Data
{
    @XStreamAsAttribute
    private String type = "";

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }



    @Override
    public String toString()
    {
        return "ClassPojo [type = "+type+"]";
    }
}

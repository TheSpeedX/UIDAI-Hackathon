package in.gov.uidai.auasample.model.piddata_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("CustOpts")
@XStreamInclude({Param.class})
public class CustOpts
{
    @XStreamImplicit(itemFieldName = "Param")
    private List<Param> list=new ArrayList<Param>();

    public List<Param> getList() {
        return list;
    }

    public void setList(List<Param> list) {
        this.list = list;
    }
}

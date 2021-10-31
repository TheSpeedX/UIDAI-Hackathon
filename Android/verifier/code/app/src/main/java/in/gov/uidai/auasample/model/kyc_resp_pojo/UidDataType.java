package in.gov.uidai.auasample.model.kyc_resp_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@XStreamInclude({PoiType.class})
@XStreamAlias("UidData")
public class UidDataType {

    protected PoiType poi;
    protected byte[] pht;
    @XStreamAsAttribute
    protected String uid = "";


    public PoiType getPoi() {
        return poi;
    }

    public void setPoi(PoiType poi) {
        this.poi = poi;
    }

    public byte[] getPht() {
        return pht;
    }

    public void setPht(byte[] pht) {
        this.pht = pht;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

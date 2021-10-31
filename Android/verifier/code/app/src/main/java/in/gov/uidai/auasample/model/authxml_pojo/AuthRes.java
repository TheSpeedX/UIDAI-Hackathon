package in.gov.uidai.auasample.model.authxml_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("AuthRes")
public class AuthRes {
    @XStreamAsAttribute
    protected String ret;
    @XStreamAsAttribute
    protected String code;
    @XStreamAsAttribute
    protected String txn;
    @XStreamAsAttribute
    protected String err;
    @XStreamAsAttribute
    protected String info;


    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

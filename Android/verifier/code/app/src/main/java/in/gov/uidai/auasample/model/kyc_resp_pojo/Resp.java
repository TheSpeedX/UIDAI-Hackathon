package in.gov.uidai.auasample.model.kyc_resp_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Resp")
public class Resp {
    @XStreamAsAttribute
    private String ret = "";
    @XStreamAsAttribute
    private String code = "";
    @XStreamAsAttribute
    private String err = "";

    private String kycRes = "";
    @XStreamAsAttribute
    private String txn = "";
    @XStreamAsAttribute
    private String status = "";
    @XStreamAsAttribute
    private String ts = "";

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

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getKycRes() {
        return kycRes;
    }

    public void setKycRes(String kycRes) {
        this.kycRes = kycRes;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public boolean isSuccess() {
        return ret.equalsIgnoreCase("Y");
    }

    @Override
    public String toString() {
        return "ClassPojo [ret = " + ret + ", code = " + code + ", err = " + err + ", kycRes = " + kycRes + ", txn = " + txn + ", status = " + status + ", ts = " + ts + "]";
    }
}

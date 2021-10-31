package in.gov.uidai.auasample.model.kyc_resp_decoded;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("KycRes")
public class KycRes
{
    @XStreamAsAttribute
    private String ret;
    private String Rar;
    @XStreamAsAttribute
    private String code;
    @XStreamAsAttribute
    private String err;
    @XStreamAsAttribute
    private Signature Signature;
    @XStreamAsAttribute
    private String txn;
    @XStreamAsAttribute
    private String ts;

    public String getRet ()
    {
        return ret;
    }

    public void setRet (String ret)
    {
        this.ret = ret;
    }

    public String getRar ()
    {
        return Rar;
    }

    public void setRar (String Rar)
    {
        this.Rar = Rar;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getErr ()
    {
        return err;
    }

    public void setErr (String err)
    {
        this.err = err;
    }

    public Signature getSignature ()
    {
        return Signature;
    }

    public void setSignature (Signature Signature)
    {
        this.Signature = Signature;
    }

    public String getTxn ()
    {
        return txn;
    }

    public void setTxn (String txn)
    {
        this.txn = txn;
    }

    public String getTs ()
    {
        return ts;
    }

    public void setTs (String ts)
    {
        this.ts = ts;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ret = "+ret+", Rar = "+Rar+", code = "+code+", err = "+err+", Signature = "+Signature+", txn = "+txn+", ts = "+ts+"]";
    }
}


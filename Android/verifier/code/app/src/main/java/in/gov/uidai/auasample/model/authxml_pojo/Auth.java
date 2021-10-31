package in.gov.uidai.auasample.model.authxml_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import in.gov.uidai.auasample.model.piddata_pojo.Data;
import in.gov.uidai.auasample.model.piddata_pojo.Skey;

@XStreamInclude({Data.class, Skey.class, Meta.class, Uses.class})
@XStreamAlias("Auth")
public class Auth
{
    @XStreamAsAttribute
    private String ac = "";
    @XStreamAsAttribute
    private String ver = "";

    private Uses Uses;
    private String Hmac = "";

    private Meta Meta;

    private in.gov.uidai.auasample.model.piddata_pojo.Data Data;
    @XStreamAsAttribute
    private String txn = "";

    private in.gov.uidai.auasample.model.piddata_pojo.Skey Skey;
    @XStreamAsAttribute
    private String tid = "";
    @XStreamAsAttribute
    private String sa = "";
    @XStreamAsAttribute
    private String uid = "";
    @XStreamAsAttribute
    private String rc = "";

    @XStreamAsAttribute
    private String lk = "";

    public String getAc ()
    {
        return ac;
    }

    public void setAc (String ac)
    {
        this.ac = ac;
    }

    public String getVer ()
    {
        return ver;
    }

    public void setVer (String ver)
    {
        this.ver = ver;
    }

    public Uses getUses ()
    {
        return Uses;
    }

    public void setUses (Uses Uses)
    {
        this.Uses = Uses;
    }

    public String getHmac ()
    {
        return Hmac;
    }

    public void setHmac (String Hmac)
    {
        this.Hmac = Hmac;
    }

    public Meta getMeta()
    {
        return Meta;
    }

    public void setMeta(Meta Meta)
    {
        this.Meta = Meta;
    }

    public Data getData ()
    {
        return Data;
    }

    public void setData (Data Data)
    {
        this.Data = Data;
    }

    public String getTxn ()
    {
        return txn;
    }

    public void setTxn (String txn)
    {
        this.txn = txn;
    }

    public in.gov.uidai.auasample.model.piddata_pojo.Skey getSkey ()
    {
        return Skey;
    }

    public void setSkey (in.gov.uidai.auasample.model.piddata_pojo.Skey Skey)
    {
        this.Skey = Skey;
    }

    public String getTid ()
    {
        return tid;
    }

    public void setTid (String tid)
    {
        this.tid = tid;
    }

    public String getSa ()
    {
        return sa;
    }

    public void setSa (String sa)
    {
        this.sa = sa;
    }

    public String getUid ()
    {
        return uid;
    }

    public void setUid (String uid)
    {
        this.uid = uid;
    }

    public String getRc ()
    {
        return rc;
    }

    public void setRc (String rc)
    {
        this.rc = rc;
    }


    public String getLk ()
    {
        return lk;
    }

    public void setLk (String lk)
    {
        this.lk = lk;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ac = "+ac+", ver = "+ver+", Uses = "+Uses+", Hmac = "+Hmac+", Meta = "+ Meta +", Data = "+Data+", txn = "+txn+", Skey = "+Skey+", tid = "+tid+", sa = "+sa+", uid = "+uid+", rc = "+rc+", lk = "+lk+"]";
    }
}
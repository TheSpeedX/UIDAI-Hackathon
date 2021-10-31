package in.gov.uidai.auasample.model.piddata_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Resp")
public class Resp
{
    @XStreamAsAttribute
    private String qScore = "";
    @XStreamAsAttribute
    private String errInfo = "";
    @XStreamAsAttribute
    private String fType = "";
    @XStreamAsAttribute
    private String errCode = "";
    @XStreamAsAttribute
    private String iCount = "";
    @XStreamAsAttribute
    private String pType = "";
    @XStreamAsAttribute
    private String fCount = "";
    @XStreamAsAttribute
    private String nmPoints = "";
    @XStreamAsAttribute
    private String iType = "";
    @XStreamAsAttribute
    private String pCount = "";

    public String getQScore ()
    {
        return qScore;
    }

    public void setQScore (String qScore)
    {
        this.qScore = qScore;
    }

    public String getErrInfo ()
    {
        return errInfo;
    }

    public void setErrInfo (String errInfo)
    {
        this.errInfo = errInfo;
    }

    public String getFType ()
    {
        return fType;
    }

    public void setFType (String fType)
    {
        this.fType = fType;
    }

    public String getErrCode ()
    {
        return errCode;
    }

    public void setErrCode (String errCode)
    {
        this.errCode = errCode;
    }

    public String getICount ()
    {
        return iCount;
    }

    public void setICount (String iCount)
    {
        this.iCount = iCount;
    }

    public String getPType ()
    {
        return pType;
    }

    public void setPType (String pType)
    {
        this.pType = pType;
    }

    public String getFCount ()
    {
        return fCount;
    }

    public void setFCount (String fCount)
    {
        this.fCount = fCount;
    }

    public String getNmPoints ()
    {
        return nmPoints;
    }

    public void setNmPoints (String nmPoints)
    {
        this.nmPoints = nmPoints;
    }

    public String getIType ()
    {
        return iType;
    }

    public void setIType (String iType)
    {
        this.iType = iType;
    }

    public String getPCount ()
    {
        return pCount;
    }

    public void setPCount (String pCount)
    {
        this.pCount = pCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [qScore = "+qScore+", errInfo = "+errInfo+", fType = "+fType+", errCode = "+errCode+", iCount = "+iCount+", pType = "+pType+", fCount = "+fCount+", nmPoints = "+nmPoints+", iType = "+iType+", pCount = "+pCount+"]";
    }
}
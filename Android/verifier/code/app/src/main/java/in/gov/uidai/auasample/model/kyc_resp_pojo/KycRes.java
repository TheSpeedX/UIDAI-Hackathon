package in.gov.uidai.auasample.model.kyc_resp_pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * <p>Java class for KycRes complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="KycRes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Rar" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="UidData" type="{http://www.uidai.gov.in/kyc/uid-kyc-response/1.0}UidDataType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ret" type="{http://www.uidai.gov.in/kyc/common/types/1.0}YesNoType" />
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="txn" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ts" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="ttl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="err" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XStreamInclude({UidDataType.class})
@XStreamAlias("KycRes")
public class KycRes {

    protected String rar;

    protected UidDataType uidData;
    @XStreamAsAttribute
    protected String ret = "";
    @XStreamAsAttribute
    protected String code = "";
    @XStreamAsAttribute
    protected String txn = "";
    @XStreamAsAttribute
    protected String err = "";

    /**
     * Gets the value of the rar property.
     *
     * @return possible object is
     * byte[]
     */
    public String getRar() {
        return rar;
    }

    /**
     * Sets the value of the rar property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setRar(String value) {
        this.rar = value;
    }

    /**
     * Gets the value of the uidData property.
     *
     * @return possible object is
     * {@link UidDataType }
     */
    public UidDataType getUidData() {
        return uidData;
    }

    /**
     * Sets the value of the uidData property.
     *
     * @param value allowed object is
     *              {@link UidDataType }
     */
    public void setUidData(UidDataType value) {
        this.uidData = value;
    }


    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the txn property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTxn() {
        return txn;
    }

    /**
     * Sets the value of the txn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTxn(String value) {
        this.txn = value;
    }

    /**
     * Gets the value of the err property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getErr() {
        return err;
    }

    /**
     * Sets the value of the err property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setErr(String value) {
        this.err = value;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }
}

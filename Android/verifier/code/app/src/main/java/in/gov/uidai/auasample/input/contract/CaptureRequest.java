package in.gov.uidai.auasample.input.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CaptureRequest extends PidOptions {
    public static CaptureRequest fromXML(String requestXML) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(requestXML, CaptureRequest.class);
    }

    public String getTxnID() {
        if (custOpts == null) {
            return "";
        }

        for (NameValue nv : custOpts.nameValues) {
            if (nv.getName().equals("txnId")) {
                return nv.getValue();
            }
        }
        return "";
    }
}

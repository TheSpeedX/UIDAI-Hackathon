package in.gov.uidai.auasample.javamethods;

import android.content.Context;
import android.util.Base64;

import com.thoughtworks.xstream.XStream;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

import in.gov.uidai.auasample.model.authxml_pojo.Auth;
import in.gov.uidai.auasample.model.authxml_pojo.Meta;
import in.gov.uidai.auasample.model.authxml_pojo.Uses;
import in.gov.uidai.auasample.model.kyc_resp_pojo.Resp;
import in.gov.uidai.auasample.model.kycxml_pojo.Kyc;
import in.gov.uidai.auasample.model.piddata_pojo.CustOpts;
import in.gov.uidai.auasample.model.piddata_pojo.Data;
import in.gov.uidai.auasample.model.piddata_pojo.Param;
import in.gov.uidai.auasample.model.piddata_pojo.PidData;
import in.gov.uidai.auasample.model.piddata_pojo.Skey;
import in.gov.uidai.auasample.settings.auaConfig.ConfigParams;
import in.gov.uidai.auasample.settings.auaConfig.ConfigUtils;

public class XstreamCommonMethos {


    public static PidData pidXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(PidData.class);
        xstream.processAnnotations(Param.class);
        xstream.processAnnotations(Resp.class);
        xstream.processAnnotations(Skey.class);
        xstream.processAnnotations(CustOpts.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        PidData data = (PidData) xstream.fromXML(receivedXml);
        return data;

    }

    public static Resp  respXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(Resp.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        Resp  data = (Resp ) xstream.fromXML(receivedXml);
        return data;

    }

    public static in.gov.uidai.auasample.model.kyc_resp_decoded.KycRes respDecodedXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(in.gov.uidai.auasample.model.kyc_resp_decoded.KycRes.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        in.gov.uidai.auasample.model.kyc_resp_decoded.KycRes data = (in.gov.uidai.auasample.model.kyc_resp_decoded.KycRes) xstream.fromXML(receivedXml);
        return data;

    }

    public static in.gov.uidai.auasample.model.auth_resp_decoded.AuthRes rarDecodedXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(in.gov.uidai.auasample.model.auth_resp_decoded.AuthRes.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        in.gov.uidai.auasample.model.auth_resp_decoded.AuthRes data = (in.gov.uidai.auasample.model.auth_resp_decoded.AuthRes) xstream.fromXML(receivedXml);
        return data;
    }

    public static String processPidBlock(String pidXml, String uid, Boolean isOtpUsed, Context context) throws Exception{

        AssetsPropertyReader assetsPropertyReader;
        Properties face_auth_properties;
        // Parsing received pid block xml
        assetsPropertyReader = new AssetsPropertyReader(context);
        face_auth_properties = assetsPropertyReader.getProperties("face_auth.properties");

        PidData pidDataObject = pidXmlToPojo(pidXml);

        ConfigParams configParams = ConfigUtils.Companion.getConfigData(ConfigUtils.Companion.getSelectedConfigEnv());
        // Constructing Auth xml

        XStream xtremeForAuthXml = new XStream();
        xtremeForAuthXml.processAnnotations(Auth.class);
        Auth authxml = new Auth();
        authxml.setUid(uid);
        authxml.setVer(face_auth_properties.getProperty("AUTH_VERSION"));
        authxml.setTid(face_auth_properties.getProperty("AUTH_TID"));
        authxml.setRc(face_auth_properties.getProperty("RESIDENT_CONCENT"));
        authxml.setAc(configParams.getAuaCode());
        authxml.setSa(configParams.getSubAUACode());
        authxml.setLk(configParams.getAuaLicenceKey());
        Skey skey = new Skey();
        skey.setCi(pidDataObject.getSkey().getCi());
        skey.setContent(pidDataObject.getSkey().getContent());
        authxml.setSkey(skey);
        Meta meta = new Meta();
        meta.setDc(pidDataObject.getDeviceInfo().getDc());
        meta.setDpId(pidDataObject.getDeviceInfo().getDpId());
        meta.setRdsVer(pidDataObject.getDeviceInfo().getRdsVer());
        meta.setRdsId(pidDataObject.getDeviceInfo().getRdsId());
        meta.setMi(pidDataObject.getDeviceInfo().getMi());
        meta.setMc(pidDataObject.getDeviceInfo().getMc());
        Data data = new Data();
        data.setContent(pidDataObject.getData().getContent());
        data.setType(pidDataObject.getData().getType());
        authxml.setHmac(pidDataObject.getHmac());



        Uses uses = new Uses();
        uses.setBio("y");
        uses.setBt("FID");
        if (isOtpUsed) {
            uses.setOtp("y");
        } else {
            uses.setOtp("n");
        }
        uses.setPa("n");
        uses.setPfa("n");
        uses.setPi("n");
        uses.setPin("n");

        authxml.setUses(uses);

        authxml.setTxn("UKC:" + UUID.randomUUID().toString());


        authxml.setData(data);
        authxml.setMeta(meta);

        String authXml = xtremeForAuthXml.toXML(authxml);


        DigitalSigner ds = new DigitalSigner(context);


        authXml = ds.signXML(authXml, true);

        XStream xtremeForKycXml = new XStream();
        xtremeForKycXml.processAnnotations(Kyc.class);
        xtremeForKycXml.autodetectAnnotations(true);
        Kyc kycxml = new Kyc();
        kycxml.setRc("Y");
        kycxml.setVer(face_auth_properties.getProperty("AUTH_VERSION"));
        kycxml.setLr("N");
        kycxml.setRa("P");
        kycxml.setDe("N");
        kycxml.setPfr("N");
        String rad = "";
        try {
            rad = Base64.encodeToString(authXml.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        kycxml.setRad(rad);

        String kycXml = xtremeForKycXml.toXML(kycxml);


        return kycXml;
    }


}

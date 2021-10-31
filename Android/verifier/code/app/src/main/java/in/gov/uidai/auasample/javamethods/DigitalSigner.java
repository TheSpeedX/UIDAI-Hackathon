package in.gov.uidai.auasample.javamethods;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import in.gov.uidai.auasample.settings.auaConfig.ConfigUtils;

/**
 * <code>DigitalSigner</code> class provides a utility method to digitally sign
 * XML document. This implementation uses .p12 file as a source of signer's
 * digital certificate. In production environments, a hardware security module
 * (HSM) should be used for digitally signing.
 *
 * @author UIDAI
 */
public class DigitalSigner {

    private static final String MEC_TYPE = "DOM";
    private static final String WHOLE_DOC_URI = "";
    private static final String KEY_STORE_TYPE = "PKCS12";

    private KeyStore.PrivateKeyEntry keyEntry;

    // used for dongle
    private Provider provider;
    private static final String KEY_STORE_TYPE_DONGLE = "PKCS11";


    public DigitalSigner(Context context) throws RuntimeException {
        this.keyEntry = getKeyFromKeyStore(context);

        if (keyEntry == null) {
            throw new RuntimeException(
                    "Key could not be read for digital signature. Please check value of signature "
                            + "alias and signature password, and restart the Auth Client");
        }
    }

    public DigitalSigner(char[] keyStorePassword,
                         String alias, Context context) {
        this.keyEntry = getKeyFromKeyStore(keyStorePassword,
                alias, context);

        if (keyEntry == null) {
            throw new RuntimeException(
                    "Key could not be read for digital signature. Please check value of signature "
                            + "alias and signature password, and restart the Auth Client");
        }
    }


    /**
     * Method to digitally sign an XML document.
     *
     * @param xmlDocument - Input XML Document.
     * @return Signed XML document
     */
    public String signXML(String xmlDocument, boolean includeKeyInfo) throws Exception {
        if (this.provider == null) {
            this.provider = new BouncyCastleProvider();
        }
        Security.addProvider(this.provider);

        try {
            // Parse the input XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document inputDocument = dbf.newDocumentBuilder().parse(
                    new InputSource(new StringReader(xmlDocument)));

            // Sign the input XML's DOM document
            Document signedDocument = sign(inputDocument, includeKeyInfo);

            // Convert the signedDocument to XML String
            StringWriter stringWriter = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(signedDocument), new StreamResult(
                    stringWriter));

            return stringWriter.getBuffer().toString();
        } catch (Exception e) {
            Log.d("error:->", e.getMessage());
            throw new RuntimeException(
                    "Error while digitally signing the XML document", e);
        }
    }

    private Document sign(Document xmlDoc, boolean includeKeyInfo)
            throws Exception {

        if (System.getenv("SKIP_DIGITAL_SIGNATURE") != null) {
            return xmlDoc;
        }

        // Creating the XMLSignature factory.

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
        //XMLSignatureFactory fac = XMLSignatureFactory.getInstance(MEC_TYPE);
        // XMLSignatureFactory fac = XMLSignatureFactory.getInstance("Signature","SHA1WithRSA");
        // Creating the reference object, reading the whole document for
        // signing.
        Reference ref = fac.newReference(WHOLE_DOC_URI, fac.newDigestMethod(
                DigestMethod.SHA1, null), Collections.singletonList(fac
                .newTransform(Transform.ENVELOPED,
                        (TransformParameterSpec) null)), null, null);

        // Create the SignedInfo.8
        SignedInfo sInfo = fac
                .newSignedInfo(fac.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE,
                        (C14NMethodParameterSpec) null), fac
                                .newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                        Collections.singletonList(ref));

        if (keyEntry == null) {
            throw new RuntimeException(
                    "Key could not be read for digital signature. Please check value of signature alias and signature password, and restart the Auth Client");
        }

        X509Certificate x509Cert = (X509Certificate) keyEntry.getCertificate();

        KeyInfo kInfo = getKeyInfo(x509Cert, fac);
        DOMSignContext dsc = new DOMSignContext(this.keyEntry.getPrivateKey(),
                xmlDoc.getDocumentElement());
        XMLSignature signature = fac.newXMLSignature(sInfo,
                includeKeyInfo ? kInfo : null);
        signature.sign(dsc);

        Node node = dsc.getParent();
        return node.getOwnerDocument();

    }

    @SuppressWarnings("unchecked")
    private KeyInfo getKeyInfo(X509Certificate cert, XMLSignatureFactory fac) {
        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data xd = kif.newX509Data(x509Content);
        return kif.newKeyInfo(Collections.singletonList(xd));
    }

    private KeyStore.PrivateKeyEntry getKeyFromKeyStore(Context context) throws RuntimeException {

        AssetsPropertyReader assetsPropertyReader;
        Properties face_auth_properties;
        // Parsing received pid block xml
        assetsPropertyReader = new AssetsPropertyReader(context);
        face_auth_properties = assetsPropertyReader.getProperties("face_auth.properties");

        // Load the KeyStore and get the signing key and certificate.
        KeyStore.PrivateKeyEntry pkEntry = null;
        InputStream cert = null;
        char[] password = null;
        String alias;

        try {
            String filename = ConfigUtils.Companion.getSelectedConfigEnv() + ConfigUtils.certFileSuffix;
            cert = context.openFileInput(filename);
            password = ConfigUtils.Companion.getConfigData(ConfigUtils.Companion.getSelectedConfigEnv()).getP12Password().toCharArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cert == null) {
            try {
                cert = context.getAssets().open(face_auth_properties.getProperty("P12_FILE_NAME"));
                password = face_auth_properties.getProperty("P12_PASSWORD").toCharArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        KeyStore keystore = null;
        Enumeration<String> aliases;
        //open the file
        try {
            keystore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keystore.load(cert, password);
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Get the alias

        try {
            aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                System.out.println(alias);
                if (keystore.isKeyEntry(alias)) {
                    try {
                        pkEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, new KeyStore.PasswordProtection(password));
                    } catch (NoSuchAlgorithmException | UnrecoverableEntryException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException("CATCH", e);
        }

        return pkEntry;
    }

    private KeyStore.PrivateKeyEntry getKeyFromKeyStore(char[] keyStorePassword, String alias, Context context) {
        AssetsPropertyReader assetsPropertyReader;
        Properties face_auth_properties;
        // Parsing received pid block xml
        assetsPropertyReader = new AssetsPropertyReader(context);
        face_auth_properties = assetsPropertyReader.getProperties("face_auth.properties");

        KeyStore.PrivateKeyEntry pkEntry = null;
        InputStream cert = null;
        char[] password = null;
        try {
            String filename = ConfigUtils.Companion.getSelectedConfigEnv() + ConfigUtils.certFileSuffix;
            cert = context.openFileInput(filename);
            password = ConfigUtils.Companion.getConfigData(ConfigUtils.Companion.getSelectedConfigEnv()).getP12Password().toCharArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cert == null) {
            try {
                cert = context.getAssets().open(face_auth_properties.getProperty("P12_FILE_NAME"));
                password = face_auth_properties.getProperty("P12_PASSWORD").toCharArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        KeyStore keystore = null;
        Enumeration<String> aliases;
        //open the file
        try {
            keystore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keystore.load(cert, password);
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Get the alias

        try {
            aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                System.out.println(alias);
                if (keystore.isKeyEntry(alias)) {
                    try {
                        pkEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, new KeyStore.PasswordProtection(password));
                    } catch (NoSuchAlgorithmException | UnrecoverableEntryException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException("CATCH", e);
        }

        return pkEntry;
    }

    private static KeyStore.PrivateKeyEntry getPrivateKeyFromDongle(
            char[] keyStorePassword) {
        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KEY_STORE_TYPE_DONGLE);
            ks.load(null, keyStorePassword);
            Enumeration<String> alias = ks.aliases();
            String signAlias = "";

            while (alias.hasMoreElements()) {
                String aliasName = alias.nextElement();
                X509Certificate cert = (X509Certificate) ks
                        .getCertificate(aliasName);
                boolean[] keyUsage = cert.getKeyUsage();
                for (int i = 0; i < keyUsage.length; i++) {
                    if ((i == 0 || i == 1) && keyUsage[i]) {
                        signAlias = aliasName;
                        break;
                    }
                }
            }
            return (KeyStore.PrivateKeyEntry) ks.getEntry(signAlias,
                    new KeyStore.PasswordProtection(keyStorePassword));

        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public PrivateKey getPrivateKey(String privatekeyString, Context context) {
        PrivateKey pk = null;
        //KeyFactory fact = null;
        KeyFactory fact2 = null;
        KeyFactory fact = null;
        EncodedKeySpec private_KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privatekeyString, Base64.DEFAULT));
        try {
            fact = KeyFactory.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        try {
            pk = fact.generatePrivate(private_KeySpec);
        } catch (InvalidKeySpecException e) {
            try {
                fact2 = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            try {
                pk = fact2.generatePrivate(private_KeySpec);
            } catch (InvalidKeySpecException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return pk;



        /*PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(context.getResources().getString(R.string.private_key_string),Base64.DEFAULT));
        PrivateKey pk = null;
        //KeyFactory fact = null;
        KeyFactory fact2 = null;
        try {
            fact = KeyFactory.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        try {
            pk = fact.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            try {
                fact2 = KeyFactory.getInstance("DER");
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            try {
                pk = fact2.generatePrivate(spec);
            } catch (InvalidKeySpecException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }*/


        //   return pk;

    }

}


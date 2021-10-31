/**
 * DISCLAIMER: The sample code or utility or tool described herein
 * is provided on an "as is" basis, without warranty of any kind.
 * UIDAI does not warrant or guarantee the individual success
 * developers may have in implementing the sample code on their
 * environment.
 * <p>
 * UIDAI does not warrant, guarantee or make any representations
 * of any kind with respect to the sample code and does not make
 * any representations or warranties regarding the use, results
 * of use, accuracy, timeliness or completeness of any data or
 * information relating to the sample code. UIDAI disclaims all
 * warranties, express or implied, and in particular, disclaims
 * all warranties of merchantability, fitness for a particular
 * purpose, and warranties related to the code, or any service
 * or software related thereto.
 * <p>
 * UIDAI is not responsible for and shall not be liable directly
 * or indirectly for any direct, indirect damages or costs of any
 * type arising out of use or any action taken by you or others
 * related to the sample code.
 * <p>
 * THIS IS NOT A SUPPORTED SOFTWARE.
 */

package in.gov.uidai.auasample.javamethods;

import android.content.Context;

import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import in.gov.uidai.auasample.settings.auaConfig.ConfigParams;
import in.gov.uidai.auasample.settings.auaConfig.ConfigUtils;


public class DataDecryptor {

    private static final int PUBLIC_KEY_SIZE = 294;
    private static final int EID_SIZE = 32;
    private static final int SECRET_KEY_SIZE = 256;
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    private static final String SECURITY_PROVIDER = "BC";
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String MASKING_FUNCTION = "MGF1";
    private static final int VECTOR_SIZE = 16;
    private static final int HMAC_SIZE = 32;
    private static final int BLOCK_SIZE = 128;
    private static final byte[] HEADER_DATA = "VERSION_1.0".getBytes();
    private static final String SIGNATURE_TAG = "Signature";
    private static final String MEC_TYPE = "DOM";

    private KeyStore.PrivateKeyEntry privateKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    public DataDecryptor(Context context) {
        this.privateKey = getKeyFromKeyStore(context);

        if (privateKey == null) {
            throw new RuntimeException("Key could not be read for digital signature. Please check value of signature "
                    + "alias and signature password, and restart the Auth Client");
        }
    }


    private KeyStore.PrivateKeyEntry getKeyFromKeyStore(Context context) {
        AssetsPropertyReader assetsPropertyReader;
        Properties face_auth_properties;
        assetsPropertyReader = new AssetsPropertyReader(context);
        face_auth_properties = assetsPropertyReader.getProperties("face_auth.properties");

        KeyStore.PrivateKeyEntry pkEntry = null;
        InputStream cert = null;
        char[] password = null;
        String alias = null;

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
        try {
            keystore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            assert keystore != null;
            keystore.load(cert, password);
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

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


    public byte[] decrypt(byte[] data) throws Exception {
        if (data == null || data.length == 0)
            throw new Exception("byte array data can not be null or blank array.");


        ByteArraySpliter arrSpliter = new ByteArraySpliter(data);

        byte[] secretKey = decryptSecretKeyData(arrSpliter.getEncryptedSecretKey(), arrSpliter.getIv(), privateKey.getPrivateKey());

        byte[] plainData = decryptData(arrSpliter.getEncryptedData(), arrSpliter.getIv(), secretKey);

		/*boolean result = validateHash(plainData);
		if(!result)
			throw new Exception( "Integrity Validation Failed : " +
					"The original data at client side and the decrypted data at server side is not identical");

*/
        return trimHMAC(plainData);

    }


    private Document getDomObject(String string) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(string)));
    }


    private PublicKey getPublicKey() throws Exception {
        ConfigParams configParams = ConfigUtils.Companion.getConfigData(ConfigUtils.Companion.getSelectedConfigEnv());
        InputStream cert = new ByteArrayInputStream(configParams.getSigningCert().getBytes(StandardCharsets.UTF_8));
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        try {
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(cert);
            return certificate.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] decryptSecretKeyData(byte[] encryptedSecretKey, byte[] iv, PrivateKey privateKey) throws Exception {

        try {
            Cipher rsaCipher = Cipher.getInstance(TRANSFORMATION, SECURITY_PROVIDER);

            PSource pSrc = (new PSource.PSpecified(iv));

            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey,
                    new OAEPParameterSpec(DIGEST_ALGORITHM, MASKING_FUNCTION,
                            MGF1ParameterSpec.SHA256, pSrc));

            return rsaCipher.doFinal(encryptedSecretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new Exception("Failed to decrypt AES secret key using RSA.", e);
        }
    }

    private byte[] decryptData(byte[] encryptedData, byte[] eid, byte[] secretKey) throws Exception {
        try {
            byte[][] iv = split(eid, VECTOR_SIZE);

            BufferedBlockCipher cipher = new BufferedBlockCipher(new CFBBlockCipher(new AESEngine(), BLOCK_SIZE));
            KeyParameter key = new KeyParameter(secretKey);

            cipher.init(false, new ParametersWithIV(key, iv[0]));

            int outputSize = cipher.getOutputSize(encryptedData.length);

            byte[] result = new byte[outputSize];
            int processLen = cipher.processBytes(encryptedData, 0, encryptedData.length, result, 0);
            cipher.doFinal(result, processLen);
            return result;
        } catch (InvalidCipherTextException txtExp) {
            throw new Exception("Decrypting data using AES failed", txtExp);
        }
    }

    private boolean validateHash(byte[] decryptedText) throws Exception {
        byte[][] hs = split(decryptedText, HMAC_SIZE);
        try {
            byte[] actualHash = generateHash(hs[1]);
            if (new String(hs[0], "UTF-8").equals(new String(actualHash, "UTF-8"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception he) {
            throw new Exception("Not able to compute hash.", he);
        }
    }

    private byte[] trimHMAC(byte[] decryptedText) {
        byte[] actualText;
        if (decryptedText == null || decryptedText.length <= HMAC_SIZE) {
            actualText = new byte[0];
        } else {
            actualText = new byte[decryptedText.length - HMAC_SIZE];
            System.arraycopy(decryptedText, HMAC_SIZE, actualText, 0,
                    actualText.length);
        }
        return actualText;
    }

    private static class ByteArraySpliter {

        private final byte[] headerVersion;
        private final byte[] iv;
        private final byte[] encryptedSecretKey;
        private final byte[] encryptedData;
        private final byte[] publicKeyData;

        public ByteArraySpliter(byte[] data) throws Exception {
            int offset = 0;
            headerVersion = new byte[HEADER_DATA.length];
            copyByteArray(data, 0, headerVersion.length, headerVersion);
            offset = offset + HEADER_DATA.length;
            publicKeyData = new byte[PUBLIC_KEY_SIZE];
            copyByteArray(data, offset, publicKeyData.length, publicKeyData);
            offset = offset + PUBLIC_KEY_SIZE;
            iv = new byte[EID_SIZE];
            copyByteArray(data, offset, iv.length, iv);
            offset = offset + EID_SIZE;
            encryptedSecretKey = new byte[SECRET_KEY_SIZE];
            copyByteArray(data, offset, encryptedSecretKey.length, encryptedSecretKey);
            offset = offset + SECRET_KEY_SIZE;
            encryptedData = new byte[data.length - offset];
            copyByteArray(data, offset, encryptedData.length, encryptedData);
        }

        public byte[] getIv() {
            return iv;
        }

        public byte[] getEncryptedSecretKey() {
            return encryptedSecretKey;
        }

        public byte[] getEncryptedData() {
            return encryptedData;
        }

        private void copyByteArray(byte[] src, int offset, int length, byte[] dest) throws Exception {
            try {
                System.arraycopy(src, offset, dest, 0, length);
            } catch (Exception e) {

                throw new Exception("Decryption failed, Corrupted packet ", e);
            }
        }
    }

    private byte[][] split(byte[] src, int n) {
        byte[] l, r;
        if (src == null || src.length <= n) {
            l = src;
            r = new byte[0];
        } else {
            l = new byte[n];
            r = new byte[src.length - n];
            System.arraycopy(src, 0, l, 0, n);
            System.arraycopy(src, n, r, 0, r.length);
        }
        return new byte[][]{l, r};
    }

    public byte[] generateHash(byte[] message) throws Exception {
        byte[] hash = null;
        try {
            // Registering the Bouncy Castle as the RSA provider.
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM, SECURITY_PROVIDER);
            digest.reset();
            hash = digest.digest(message);
        } catch (GeneralSecurityException e) {
            throw new Exception("SHA-256 Hashing algorithm not available");
        }
        return hash;
    }

    public boolean verify(String xml) throws Exception {
        try {
            Document xmlDoc = getDomObject(xml);
            PublicKey publicKey = getPublicKey();
            NodeList nl = xmlDoc.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_TAG);
            if (nl.getLength() == 0)
                throw new IllegalArgumentException("Cannot find Signature element");

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance(MEC_TYPE, new XMLDSigRI());

            DOMValidateContext valContext = new DOMValidateContext(publicKey, nl.item(0));
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            return signature.validate(valContext);
        } catch (MarshalException | XMLSignatureException mExp) {
            throw new Exception(mExp);
        } catch (Exception e) {
            return false;
        }
    }


    private static Node getSignatureNode(Document inputDocument) {
        if (inputDocument != null) {
            Element rootElement = inputDocument.getDocumentElement();
            if (rootElement != null) {
                NodeList nl = rootElement.getChildNodes();
                if (nl != null) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Node n = nl.item(i);
                        if (n != null) {
                            if (n.getNodeName() != null && "signature".equalsIgnoreCase(n.getLocalName())) {
                                return n;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static Document removeSignature(Document inputDocument) {

        if (inputDocument != null) {
            Element rootElement = inputDocument.getDocumentElement();
            Node n = getSignatureNode(inputDocument);
            if (n != null) {
                rootElement.removeChild(n);
            }
        }

        return inputDocument;
    }
}

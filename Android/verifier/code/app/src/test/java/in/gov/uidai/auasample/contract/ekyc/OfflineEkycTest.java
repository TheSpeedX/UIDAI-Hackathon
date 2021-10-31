package in.gov.uidai.auasample.input.contract.ekyc;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Assert;
import org.junit.Test;

public class OfflineEkycTest {

    @Test
    public void ShouldReturnRegisterRequestFromGivenXML() throws Exception {
        String ekycXML = getekycXML();

        OfflineEkyc offlineEkyc = OfflineEkyc.fromXML(ekycXML);

        Assert.assertEquals("2241", offlineEkyc.getLast4DigitUid());
        Assert.assertEquals("photoString", offlineEkyc.uidData.getPht());
    }

    @Test
    public void ShouldGetLast4DigitUid() {
        OfflineEkyc validOfflineEkyc1 = new OfflineEkyc(new UidData(), "123456");
        OfflineEkyc validOfflineEkyc2 = new OfflineEkyc(new UidData(), "1234");

        Assert.assertEquals("1234", validOfflineEkyc1.getLast4DigitUid());
        Assert.assertEquals("1234", validOfflineEkyc2.getLast4DigitUid());
    }

    @Test
    public void ShouldReturnEmptyIfGetLast4DigitUidNotFound() {
        OfflineEkyc emptyReferenceId = new OfflineEkyc(new UidData(), "");
        OfflineEkyc nullReferenceId = new OfflineEkyc(new UidData(), null);
        OfflineEkyc referenceIDWithInvalidReferenceID = new OfflineEkyc(new UidData(), "123");

        Assert.assertEquals("", emptyReferenceId.getLast4DigitUid());
        Assert.assertEquals("", referenceIDWithInvalidReferenceID.getLast4DigitUid());
        Assert.assertEquals("", nullReferenceId.getLast4DigitUid());
    }

    private String getekycXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<OfflinePaperlessKyc referenceId=\"224120200417143053086\">\n" +
                "  <UidData>\n" +
                "    <Poi dob=\"07-12-1980\" e=\"4567876543567898765\" gender=\"M\" m=\"98745678987656789\" name=\"John Doe\" />\n" +
                "    <Poa careof=\"Abc\" country=\"India\" dist=\"Bangalore\" house=\"\" landmark=\"\" loc=\"Rajarajeshwari Nagar\" pc=\"560098\" po=\"Rajarajeshwarinagar\" state=\"Karnataka\" street=\"Layout\" subdist=\"Bangalore South\" vtc=\"Bangalore South\" />\n" +
                "    <Pht>photoString</Pht>\n" +
                "  </UidData>\n" +
                "  <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "    <SignedInfo>\n" +
                "      <CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" />\n" +
                "      <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" />\n" +
                "      <Reference URI=\"\">\n" +
                "        <Transforms>\n" +
                "          <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" />\n" +
                "        </Transforms>\n" +
                "        <DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "        <DigestValue>Jnd2WERrfM7q/j7y9kHK6qo5bKumeMoNpx7vOB+49x4=</DigestValue>\n" +
                "      </Reference>\n" +
                "    </SignedInfo>\n" +
                "    <SignatureValue>AM9joTAmsbeK1AhvgrcxT4gztIITx4b020xDbtl7uxqm+xh+7X0Xheh7Scfen5p1Xs3otlze36RC nCNYGrk6+Uq140wq25RXCilrdxw/UOydS1piDxKRuwKTW8o0pCe/WrxBEyo84hm09jZhpeuvyufW IOBLxeDFyuhcgl5Xz+VuSD0iGuliL8PplaV5NdAN+bMMHbwSBnKHYBc0JPPlsB3NT2JksOccM2OH mhDJ2mSDeQlA2rTNZYrfomgOJWspVhH9pv5thKM5Wx1r+2rcrPdSt7TW5i/OpMhH10KdVMzwgu4U xJqII7tTm/Vf/AUeNMukYiFQ0lwgSYZoG1CJlw==</SignatureValue>\n" +
                "  </Signature>\n" +
                "</OfflinePaperlessKyc>";
    }

}
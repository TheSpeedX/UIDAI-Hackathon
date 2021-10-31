package in.gov.uidai.auasample.input.contract;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustOptsTest {
    @Test
    public void ShouldInitializeCustOptsWithNameValueList() {
        CustOpts custOpts = new CustOpts();
        Assert.assertEquals(0, custOpts.nameValues.size());
    }

}
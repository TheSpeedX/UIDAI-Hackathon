package in.gov.uidai.auasample.input.contract.ekyc;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class UidData {
    @JacksonXmlProperty(localName = "Pht")
    private String pht;

    @JacksonXmlProperty(localName = "Poi")
    public Poi poi;

    public String getPht() {
        if (pht == null) {
            return "";
        }
        return pht;
    }

    @Nullable
    public String getName(){
        if (null==poi)
            return null;
        return poi.getName();
    }
}

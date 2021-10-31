package in.gov.uidai.auasample.input.contract;

public class HeaderInfo {
    private String appId;
    private String deviceId;
    private String ipAddress = "0.0.0.0";

    public HeaderInfo(String appId, String deviceId) {
        this.appId = appId;
        this.deviceId = deviceId;
    }
}

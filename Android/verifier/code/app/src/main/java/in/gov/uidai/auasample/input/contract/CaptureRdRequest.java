package in.gov.uidai.auasample.input.contract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


public class CaptureRdRequest {
    @SerializedName("header")
    private HeaderInfo headerInfo;

    private FaceData faceData;

    @SerializedName("pidOptions")
    private CaptureRequest captureRequest;

    public CaptureRdRequest(HeaderInfo headerInfo, FaceData faceData, PidOptions pidOptions) {
        this.headerInfo = headerInfo;
        this.faceData = faceData;
        this.captureRequest = (CaptureRequest) pidOptions;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(this);
    }
}

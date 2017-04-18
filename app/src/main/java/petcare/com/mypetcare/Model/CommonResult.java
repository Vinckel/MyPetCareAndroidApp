package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KS on 2017-03-24.
 */

public class CommonResult {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public Integer getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

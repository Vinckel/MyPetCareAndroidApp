package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-04-02.
 */

public class GeoStateVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<GeoStateObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class GeoStateObject {
        @SerializedName("ORG_CD")
        private String code;

        @SerializedName("ORGDOWN_NM")
        private String name;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<GeoStateObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

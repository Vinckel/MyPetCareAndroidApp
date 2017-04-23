package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-04-02.
 */

public class GeoRegionVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<GeoRegionObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class GeoRegionObject {
        @SerializedName("UPR_CD")
        private String stateCode;

        @SerializedName("ORG_CD")
        private String regionCode;

        @SerializedName("ORGDOWN_NM")
        private String regionName;

        public String getStateCode() {
            return stateCode;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public String getRegionName() {
            return regionName;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<GeoRegionObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

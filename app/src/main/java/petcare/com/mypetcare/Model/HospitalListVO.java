package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-02.
 */

public class HospitalListVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<HospitalObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    @SerializedName("TOTAL_COUNT")
    private Integer count;

    @SerializedName("SEARCH_COUNT")
    private Integer currentCount;

    @SerializedName("SEARCH_PAGE")
    private Integer page;

    public class HospitalObject {
        @SerializedName("HOSP_ID")
        private String id;

        @SerializedName("TITLE")
        private String name;

        @SerializedName("IMAGE_URL")
        private String imgUrl;

        @SerializedName("DISTANCE")
        private String distance;

        @SerializedName("DIRECTION")
        private String direction;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getDistance() {
            return distance;
        }

        public String getDirection() {
            return direction;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<HospitalObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public Integer getPage() {
        return page;
    }
}

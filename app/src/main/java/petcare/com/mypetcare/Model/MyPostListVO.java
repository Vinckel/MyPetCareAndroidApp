package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-21.
 */

public class MyPostListVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<MyPostObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    @SerializedName("TOTAL_COUNT")
    private Integer count;

    @SerializedName("SEARCH_COUNT")
    private Integer currentCount;

    @SerializedName("SEARCH_PAGE")
    private Integer page;

    public class MyPostObject {
        @SerializedName("MY_COM_DIVIDE")
        private String divide;

        @SerializedName("MY_COM_KEY")
        private String key;

        @SerializedName("MY_COM_IMG_URL")
        private String imgUrl;

        @SerializedName("MY_COM_IMG_THUM_URL")
        private String thumbImgUrl;

        @SerializedName("MY_COM_TYPE")
        private String type;

        @SerializedName("MY_COM_PET_NAME")
        private String petName;

        @SerializedName("FRST_REGIST_DTIME")
        private String createDate;

        @SerializedName("FRST_UPDATE_DTIME")
        private String updateDate;

        public String getDivide() {
            return divide;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getThumbImgUrl() {
            return thumbImgUrl;
        }

        public String getPetName() {
            return petName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public String getType() {
            return type;
        }

        public String getKey() {
            return key;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<MyPostObject> getData() {
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

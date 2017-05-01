package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-01.
 */

public class MatingListVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<MatingObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    @SerializedName("TOTAL_COUNT")
    private Integer count;

    @SerializedName("SEARCH_COUNT")
    private Integer currentCount;

    @SerializedName("SEARCH_PAGE")
    private Integer page;

    public class MatingObject {
        @SerializedName("PET_CB_ID")
        private String id;

        @SerializedName("PET_CB_NM")
        private String name;

        @SerializedName("PET_CB_KND")
        private String breed;

        @SerializedName("PET_CB_COLOR")
        private String color;

        @SerializedName("PET_CB_SEX")
        private String gender;

        @SerializedName("PET_CB_AGE")
        private String age;

        @SerializedName("PET_CB_DESC")
        private String description;

        @SerializedName("PET_CB_IMG_URL")
        private String imgUrl;

        @SerializedName("PET_CB_IMG_THUM_URL")
        private String thumbImgUrl;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getBreed() {
            return breed;
        }

        public String getColor() {
            return color;
        }

        public String getGender() {
            return gender;
        }

        public String getAge() {
            return age;
        }

        public String getDescription() {
            return description;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getThumbImgUrl() {
            return thumbImgUrl;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<MatingObject> getData() {
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

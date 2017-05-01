package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by KS on 2017-05-01.
 */

public class ReportListVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<ReportPetObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    @SerializedName("TOTAL_COUNT")
    private Integer count;

    @SerializedName("SEARCH_COUNT")
    private Integer currentCount;

    @SerializedName("SEARCH_PAGE")
    private Integer page;

    public class ReportPetObject {
        @SerializedName("PET_AR_ID")
        private String id;

        @SerializedName("PET_AR_TYPE")
        private String type;

        @SerializedName("PET_AR_NM")
        private String name;

        @SerializedName("PET_AR_KND")
        private String breed;

        @SerializedName("PET_AR_COLOR")
        private String color;

        @SerializedName("PET_AR_SEX")
        private String gender;

        @SerializedName("PET_AR_AGE")
        private String age;

        @SerializedName("PET_AR_PLACE")
        private String location;

        @SerializedName("PET_AR_DATE")
        private String date;

        @SerializedName("PET_AR_CHAR")
        private String mark;

        @SerializedName("PET_AR_REWARD")
        private String price;

        @SerializedName("PET_AR_DESC")
        private String description;

        @SerializedName("PET_AR_FIND_AT")
        private String isFound;

        @SerializedName("PET_AR_IMG_URL")
        private String imgUrl;

        @SerializedName("PET_AR_IMG_THUM_URL")
        private String thumbImgUrl;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
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

        public String getLocation() {
            return location;
        }

        public String getDate() {
            return date;
        }

        public String getMark() {
            return mark;
        }

        public String getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public String getIsFound() {
            return isFound;
        }

        public boolean isFound() {
            return StringUtils.equals(isFound, "Y");
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

    public List<ReportPetObject> getData() {
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

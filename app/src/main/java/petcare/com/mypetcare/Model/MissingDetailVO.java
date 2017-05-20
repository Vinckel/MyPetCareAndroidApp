package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by KS on 2017-04-02.
 */

public class MissingDetailVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<ReportPetObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

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

        @SerializedName("PET_AR_TELNUM")
        private String contact;

        @SerializedName("PET_AR_REWARD")
        private String price;

        @SerializedName("PET_AR_DESC")
        private String description;

        @SerializedName("PET_AR_FIND_AT")
        private String isFound;

        @SerializedName("PET_IMG")
        private List<PetImageObject> imgData;

        public String getContact() {
            return contact;
        }

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

        public List<PetImageObject> getImgData() {
            return imgData;
        }

        public class PetImageObject {
            @SerializedName("PET_AR_IMG_SN")
            private Integer no;

            @SerializedName("PET_AR_IMG_NM")
            private String name;

            @SerializedName("PET_AR_IMG_URL")
            private String petImgUrl;

            @SerializedName("PET_AR_IMG_THUM_URL")
            private String petImgThumbUrl;

            public Integer getNo() {
                return no;
            }

            public String getName() {
                return name;
            }

            public String getPetImgUrl() {
                return petImgUrl;
            }

            public String getPetImgThumbUrl() {
                return petImgThumbUrl;
            }
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
}

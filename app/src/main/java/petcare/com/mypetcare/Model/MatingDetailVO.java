package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-18.
 */

public class MatingDetailVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<MatingDetailObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class MatingDetailObject {

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

        @SerializedName("PET_IMG")
        private List<MatingDetailImageObject> imgData;

        public class MatingDetailImageObject {
            @SerializedName("PET_CB_IMG_SN")
            private String imgId;

            @SerializedName("PET_CB_IMG_NM")
            private String imgName;

            @SerializedName("PET_CB_IMG_URL")
            private String imgUrl;

            @SerializedName("PET_CB_IMG_THUM_URL")
            private String thumbnailUrl;

            public String getImgId() {
                return imgId;
            }

            public String getImgName() {
                return imgName;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }
        }

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

        public List<MatingDetailImageObject> getImgData() {
            return imgData;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<MatingDetailObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-04-02.
 */

public class MyInfoVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private MyInfoObject data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class MyInfoObject {
        @SerializedName("USER_NAME")
        private String userName;

        @SerializedName("USER_IMG_URL")
        private String userImgUrl;

        @SerializedName("USER_IMG_THUM_URL")
        private String userImgThumbUrl;

        @SerializedName("PET_INFO")
        private List<MyPetInfoObject> data;

        public String getUserName() {
            return userName;
        }

        public String getUserImgUrl() {
            return userImgUrl;
        }

        public String getUserImgThumbUrl() {
            return userImgThumbUrl;
        }

        public List<MyPetInfoObject> getData() {
            return data;
        }

        public class MyPetInfoObject {
            @SerializedName("PET_SN")
            private String no;

            @SerializedName("PET_KND_CD")
            private String petBreedCode;

            @SerializedName("PET_KND_NM")
            private String petBreed;

            @SerializedName("PET_BIRTH")
            private String petBirth;

            @SerializedName("PET_IMG_URL")
            private String petImgUrl;

            @SerializedName("PET_IMG_THUM_URL")
            private String petImgThumbUrl;

            public String getNo() {
                return no;
            }

            public String getPetBreedCode() {
                return petBreedCode;
            }

            public String getPetBreed() {
                return petBreed;
            }

            public String getPetBirth() {
                return petBirth;
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

    public MyInfoObject getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

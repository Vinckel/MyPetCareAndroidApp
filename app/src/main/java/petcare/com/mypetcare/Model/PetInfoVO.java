package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-04-02.
 */

public class PetInfoVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<PetInfoObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class PetInfoObject {

        @SerializedName("PET_KND_CD")
        private String breedCode;

        @SerializedName("PET_KND_NM")
        private String breed;

        @SerializedName("PET_BIRTH")
        private String birth;

        @SerializedName("PET_IMG")
        private List<PetImageObject> imgData;

        public String getBreedCode() {
            return breedCode;
        }

        public String getBreed() {
            return breed;
        }

        public String getBirth() {
            return birth;
        }

        public List<PetImageObject> getImgData() {
            return imgData;
        }

        public class PetImageObject {
            @SerializedName("PET_IMG_SN")
            private Integer no;

            @SerializedName("PET_IMG_NM")
            private String name;

            @SerializedName("PET_IMG_URL")
            private String petImgUrl;

            @SerializedName("PET_IMG_THUM_URL")
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

    public List<PetInfoObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

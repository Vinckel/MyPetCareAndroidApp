package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-04-24.
 */

public class AnnounceInfoVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<AnnounceInfoObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class AnnounceInfoObject {
        @SerializedName("DESERTION_NO")
        private String no;

        @SerializedName("FILENAME")
        private String thumbUrl;

        @SerializedName("HAPPEN_DT")
        private String receptDate;

        @SerializedName("HAPPEN_PLACE")
        private String receptPlace;

        @SerializedName("KIND_CD")
        private String breed;

        @SerializedName("COLOR_CD")
        private String color;

        @SerializedName("AGE")
        private String age;

        @SerializedName("WEIGHT")
        private String weight;

        @SerializedName("NOTICE_NO")
        private String noticeNo;

        @SerializedName("NOTICE_SDT")
        private String noticeStartDate;

        @SerializedName("NOTICE_EDT")
        private String noticeEndDate;

        @SerializedName("POPFILE")
        private String imageUrl;

        @SerializedName("PROCESS_STATE")
        private String state;

        @SerializedName("SEX_CD")
        private String gender;

        @SerializedName("NEUTER_YN")
        private String isNeutralized;

        @SerializedName("SPECIAL_MARK")
        private String mark;

        @SerializedName("CARE_NM")
        private String shelterName;

        @SerializedName("CARE_TEL")
        private String shelterCall;

        @SerializedName("CARE_ADDR")
        private String shelterAddr;

        @SerializedName("ORG_NM")
        private String organization;

        @SerializedName("CHARGE_NM")
        private String charger;

        @SerializedName("OFFICETEL")
        private String chargerCall;

        public String getNo() {
            return no;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public String getReceptDate() {
            return receptDate;
        }

        public String getReceptPlace() {
            return receptPlace;
        }

        public String getBreed() {
            return breed;
        }

        public String getColor() {
            return color;
        }

        public String getAge() {
            return age;
        }

        public String getWeight() {
            return weight;
        }

        public String getNoticeNo() {
            return noticeNo;
        }

        public String getNoticeStartDate() {
            return noticeStartDate;
        }

        public String getNoticeEndDate() {
            return noticeEndDate;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getState() {
            return state;
        }

        public String getGender() {
            return gender;
        }

        public String getIsNeutralized() {
            return isNeutralized;
        }

        public String getMark() {
            return mark;
        }

        public String getShelterName() {
            return shelterName;
        }

        public String getShelterCall() {
            return shelterCall;
        }

        public String getShelterAddr() {
            return shelterAddr;
        }

        public String getOrganization() {
            return organization;
        }

        public String getCharger() {
            return charger;
        }

        public String getChargerCall() {
            return chargerCall;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<AnnounceInfoObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

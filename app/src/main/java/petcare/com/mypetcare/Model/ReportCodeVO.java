package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-02.
 */

public class ReportCodeVO {

    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<ReportCodeObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class ReportCodeObject {

        @SerializedName("CD_GRP")
        private String group;

        @SerializedName("CD_GRP_NM")
        private String groupName;

        @SerializedName("cd")
        private String code;

        @SerializedName("CD_NM")
        private String codeName;

        public String getGroup() {
            return group;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getCode() {
            return code;
        }

        public String getCodeName() {
            return codeName;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<ReportCodeObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

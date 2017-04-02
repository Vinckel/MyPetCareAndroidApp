package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by KS on 2017-04-02.
 */

public class DiaryListVO {

    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<DiaryObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class DiaryObject {

        @SerializedName("FRST_REGIST_DTIME")
        private String createDate;

        @SerializedName("PET_JOURNAL_CN")
        private String contents;

        @SerializedName("PET_JOURNAL_SN")
        private Integer no;

        public String getCreateDate() {
            return createDate;
        }

        public String getContents() {
            return contents;
        }

        public Integer getNo() {
            return no;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<DiaryObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

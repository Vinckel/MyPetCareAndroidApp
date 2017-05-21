package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-21.
 */

public class NoticeDetailVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<NoticeObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class NoticeObject {
        @SerializedName("NOTICE_ID")
        private String id;

        @SerializedName("TITLE")
        private String title;

        @SerializedName("CONTENT")
        private String content;

        @SerializedName("FRST_REGIST_DTIME")
        private String createDate;

        @SerializedName("LAST_UPDATE_DTIME")
        private String updateDate;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<NoticeObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-21.
 */

public class NoticeListVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<NoticeObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    @SerializedName("TOTAL_COUNT")
    private Integer count;

    @SerializedName("SEARCH_COUNT")
    private Integer currentCount;

    @SerializedName("SEARCH_PAGE")
    private Integer page;

    public class NoticeObject {
        @SerializedName("NOTICE_ID")
        private String id;

        @SerializedName("TITLE")
        private String name;

        @SerializedName("CONTENT")
        private String content;

        @SerializedName("FRST_REGIST_DTIME")
        private String createDate;

        @SerializedName("LAST_UPDATE_DTIME")
        private String updateDate;

        @SerializedName("NEW_NOTICE_AT")
        private String noticeAt;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
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

        public String getNoticeAt() {
            return noticeAt;
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

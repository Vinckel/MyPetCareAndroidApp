package petcare.com.mypetcare.Model;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by KS on 2017-03-25.
 */

public class HttpResultVO {

    @Key("RESULT_CODE")
    private String resultCode;

    @Key("DATA")
    private List<Object> data;

    @Key("RESULT_MESSAGE")
    private String resultMessage;

    public String getResultCode() {
        return resultCode;
    }

    public List<Object> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

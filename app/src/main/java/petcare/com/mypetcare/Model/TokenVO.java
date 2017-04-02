package petcare.com.mypetcare.Model;

import com.google.api.client.util.Key;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by KS on 2017-03-25.
 */

public class TokenVO {

    @SerializedName("RESULT_CODE")
    Integer resultCode;

    @SerializedName("DATA")
    List<Map<String, String>> data;

    @SerializedName("RESULT_MESSAGE")
    String resultMessage;

    public Integer getResultCode() {
        return resultCode;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getToken() {
        return CollectionUtils.isNotEmpty(data) ? MapUtils.getString(data.get(0), "TOKEN") : null;
    }
}

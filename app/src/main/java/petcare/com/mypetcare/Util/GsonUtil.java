package petcare.com.mypetcare.Util;

import com.google.gson.Gson;

/**
 * Created by KS on 2017-04-02.
 */

public class GsonUtil {
    public static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}

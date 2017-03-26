package petcare.com.mypetcare.Util;

import android.app.Application;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KS on 2017-03-25.
 */

public class Global extends Application {
    private Map<String, Object> data;

    @Override
    public void onCreate() {
        super.onCreate();
        data = new HashMap<>();
        data.put("calling", false);
    }

    public Map<String, Object> getMap() {
        return data;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getToken() {
        return data.containsKey("token") ? MapUtils.getString(data, "token") : null;
    }
}

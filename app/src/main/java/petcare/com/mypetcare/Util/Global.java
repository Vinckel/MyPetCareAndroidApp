package petcare.com.mypetcare.Util;

import android.app.Application;
import android.content.SharedPreferences;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KS on 2017-03-25.
 */

public class Global extends Application {
    private Map<String, Object> data;
    private static final long DAY_TO_MILLISECONDS = 86400000;
    private static final long HOUR_TO_MILLISECONDS = 3600000;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        data = new HashMap<>();
        data.put("token_api_calling", false);
        pref = getSharedPreferences("local_auth", MODE_PRIVATE);
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

    public String getEmail() {
        return data.containsKey("email") ? MapUtils.getString(data, "email") : null;
    }

    public boolean isValidToken() {
        long tokenDate = pref.getLong("auth_date", 0L);
        String token = pref.getString("token", null);
        long now = Calendar.getInstance().getTimeInMillis();

        return !(now - tokenDate > DAY_TO_MILLISECONDS - HOUR_TO_MILLISECONDS || StringUtils.isBlank(token));
    }

    public boolean isTokenApiCalling() {
        Boolean calling = MapUtils.getBoolean(data, "token_api_calling");

        return !(calling == null || !calling);
    }
}

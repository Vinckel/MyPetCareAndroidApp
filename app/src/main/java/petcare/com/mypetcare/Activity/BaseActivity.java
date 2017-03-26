package petcare.com.mypetcare.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Util.HttpConn;
import petcare.com.mypetcare.Util.Global;

/**
 * Created by KS on 2017-03-25.
 */

public class BaseActivity extends AppCompatActivity {
    private static final long DAY_TO_MILLISECONDS = 86400000;
    private static final long HOUR_TO_MILLISECONDS = 3600000;
    protected static Global global = null;

    @Override
    public void onResume() {
        super.onResume();

        global = (Global) getApplicationContext();
        Boolean calling = MapUtils.getBoolean(global.getMap(), "token_api_calling");

        if (calling) {
            return ;
        }

        SharedPreferences pref = getSharedPreferences("local_auth", MODE_PRIVATE);
        long tokenDate = pref.getLong("auth_date", 0L);
        long now = Calendar.getInstance().getTimeInMillis();
        String url = "http://220.73.175.100:8080/MPMS/mob/auth.service";
        Map params = new HashMap<>();
        params.put("USER_EMAIL", "test@test.com");
        String contentType = "application/json";
        String serviceId = "";
        String token = pref.getString("token", null);

        if (now - tokenDate > DAY_TO_MILLISECONDS - HOUR_TO_MILLISECONDS || StringUtils.isBlank(token)) {
            global.set("token", null);
            HttpConn test = new HttpConn();
            test.setContext(global);
            test.execute(contentType, url, serviceId, params);
        } else {
            global.set("token", token);
        }
    }
}

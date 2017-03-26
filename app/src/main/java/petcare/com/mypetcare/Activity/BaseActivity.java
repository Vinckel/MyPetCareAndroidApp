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
    private static boolean initialized = false;
    protected static Global global = null;

    @Override
    public void onResume() {
        super.onResume();

//        Boolean calling = MapUtils.getBoolean(global.getMap(), "token_api_calling");
//
//        if (calling) {
//            return ;
//        }

        if (!initialized) {
            initialized = true;
            global = (Global) getApplicationContext();
            SharedPreferences pref = getSharedPreferences("local_auth", MODE_PRIVATE);
            long tokenDate = pref.getLong("auth_date", 0L);
            long now = Calendar.getInstance().getTimeInMillis();

            String email = pref.getString("email", null);
            String token = pref.getString("token", null);

            global.set("email", email);
            global.set("token", token);

            String url = "http://220.73.175.100:8080/MPMS/mob/auth.service";

            Map params = new HashMap<>();
            params.put("USER_EMAIL", email);
            String contentType = "application/json";
            String serviceId = "";

            if (now - tokenDate > DAY_TO_MILLISECONDS - HOUR_TO_MILLISECONDS || StringUtils.isBlank(token)) {
                HttpConn tokenApi = new HttpConn();
                tokenApi.setContext(global);
                tokenApi.execute(contentType, url, serviceId, params, StringUtils.EMPTY);
            }
        }
    }
}

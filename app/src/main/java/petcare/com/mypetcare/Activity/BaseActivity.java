package petcare.com.mypetcare.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.Global;
import petcare.com.mypetcare.Util.TokenApi;

/**
 * Created by KS on 2017-03-25.
 */

public class BaseActivity extends AppCompatActivity {
    private static boolean initialized = false;
    protected static Global global = null;
    private static Gson gson;

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
            GeneralApi.setGlobal(global);
            SharedPreferences pref = getSharedPreferences("local_auth", MODE_PRIVATE);

            String email = pref.getString("email", null);
            String token = pref.getString("token", null);

            global.set("email", email);
            global.set("token", token);

            if (!global.isValidToken() && StringUtils.isNotEmpty(email)) {
                TokenApi tokenApi = new TokenApi();
                tokenApi.setContext(global);
                tokenApi.execute(email);
            }
        }
    }
}

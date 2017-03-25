package petcare.com.mypetcare.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import org.apache.commons.collections4.MapUtils;

import java.util.Calendar;

import petcare.com.mypetcare.Util.AuthHttpConn;
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
        Boolean calling = MapUtils.getBoolean(global.getMap(), "calling");

        if (calling) {
            return ;
        }

        SharedPreferences pref = getSharedPreferences("local_auth", MODE_PRIVATE);
        long tokenDate = pref.getLong("auth_date", 0L);
        long now = Calendar.getInstance().getTimeInMillis();

        if (now - tokenDate > DAY_TO_MILLISECONDS - HOUR_TO_MILLISECONDS) {
            AuthHttpConn test = new AuthHttpConn();
            test.setContext(global);
            test.execute("test@test.com");
        }
    }
}

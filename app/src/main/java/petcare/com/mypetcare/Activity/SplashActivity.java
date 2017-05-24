package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import petcare.com.mypetcare.R;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_SECOND = 1500;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("local_auth", MODE_PRIVATE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SECOND);

        if (pref.contains("autoLogin") && pref.getBoolean("autoLogin", false)) { // 자동로그인이 true 일 때
        } else {
            // 자동로그인 설정이 없거나 false 일 때
            LoginManager.getInstance().logOut();
            OAuthLogin.getInstance().logout(SplashActivity.this);
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                }
            });
        }
    }
}

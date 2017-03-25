package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import petcare.com.mypetcare.R;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_SECOND = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_SECOND);
    }
}

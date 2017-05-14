package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;

import petcare.com.mypetcare.R;

public class SettingActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private RelativeLayout rlTerms;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_setting);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_setting, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_setting_back);
        rlTerms = (RelativeLayout) findViewById(R.id.rl_setting_terms);
        tvVersion = (TextView) findViewById(R.id.tv_setting_version);

        rlTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            String curVersion = getPackageManager().getPackageInfo("petcare.com.mypetcare", 0).versionName;
            tvVersion.setText(curVersion);

            VersionCheckApi check = new VersionCheckApi();
            check.execute();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tvVersion.setText("알 수 없음");
        }
    }

    class VersionCheckApi extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String curVersion = getPackageManager().getPackageInfo("petcare.com.mypetcare", 0).versionName;
                String newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "petcare.com.mypetcare" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return (value(curVersion) < value(newVersion)) ? true : false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isOutDated) {
            if (isOutDated) {
                tvVersion.setText(tvVersion.getText() + " 최신 버전이 아닙니다.");
            } else {
                tvVersion.setText(tvVersion.getText() + " 최신 버전입니다.");
            }
        }
    }

    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import org.jsoup.Jsoup;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GsonUtil;

public class SettingActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private RelativeLayout rlTerms;
    private TextView tvVersion;
    private SwitchCompat swLogin;
    SharedPreferences pref;
    private RelativeLayout rlLogout;

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

        pref = getSharedPreferences("local_auth", MODE_PRIVATE);

        ibBack = (ImageButton) findViewById(R.id.ib_setting_back);
//        rlTerms = (RelativeLayout) findViewById(R.id.rl_setting_terms);
        tvVersion = (TextView) findViewById(R.id.tv_setting_version);
        swLogin = (SwitchCompat) findViewById(R.id.sw_setting_login);
        rlLogout = (RelativeLayout) findViewById(R.id.rl_setting_logout);

        boolean isAutoLogin = pref.getBoolean("autoLogin", true);
        swLogin.setChecked(isAutoLogin);
        swLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.setMessage("자동 로그인이 해제되었습니다.\n재로그인 시 동일 계정으로 로그인해주세요.");
                    alert.setCancelable(false);
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btDone.setTextColor(getResources().getColor(R.color.normalFont));
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putBoolean("autoLogin", isChecked);
                edit.commit();
            }
        });

//        rlTerms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, TermsActivity.class);
//                startActivity(intent);
//            }
//        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putBoolean("autoLogin", false);
                        edit.commit();

                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);

                        LoginManager.getInstance().logOut();
                        OAuthLogin.getInstance().logout(SettingActivity.this);
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Toast.makeText(SettingActivity.this, "카카오 로그아웃 성공", Toast.LENGTH_SHORT).show();
                            }
                        });
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return;
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setMessage("로그아웃하시겠습니까?\n재로그인 시 동일 계정으로 로그인해주세요.");
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btDone.setTextColor(getResources().getColor(R.color.normalFont));
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

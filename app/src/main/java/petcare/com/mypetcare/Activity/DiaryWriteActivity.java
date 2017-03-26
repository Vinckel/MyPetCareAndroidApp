package petcare.com.mypetcare.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.repackaged.org.apache.commons.codec.Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import petcare.com.mypetcare.Model.HttpResultVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.HttpConn;

public class DiaryWriteActivity extends BaseActivity {
    private ImageButton ibBack;
    private TextView tvDate;
    private TextView tvDone;
    private Button btDone;
    private EditText etContent;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final boolean isNew = intent.getBooleanExtra("isNew", true);
//        final int writeTargetNo = intent.getIntExtra("writeTargetNo", 1);

        setContentView(R.layout.activity_diary_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.diary_write_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.diary_write_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_diary_write_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etContent = (EditText) findViewById(R.id.et_diary_write);
        etContent.requestFocus();

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(etContent, InputMethodManager.SHOW_FORCED);
//        imm.showSoftInputFromInputMethod (etContent.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED);

        tvDate = (TextView) findViewById(R.id.tv_diary_write_date);
        tvDate.setText(SDF.format(new Date()));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encContent = null;
                try {
                    encContent = URLEncoder.encode(etContent.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "내용에 허용되지 않는 특수문자가 포함되어 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpConn diaryWriteApi = new HttpConn();
                diaryWriteApi.setContext(global);

                String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
                String serviceId = "MPMS_02002";
                String contentType = "application/json";

                Map params = new HashMap<>();
                params.put("USER_EMAIL", global.get("email"));
//                params.put("PET_JOURNAL_SN", writeTargetNo);


                params.put("PET_JOURNAL_CN", encContent);
                HttpResultVO httpResultVO = null;
                try {
                    httpResultVO = diaryWriteApi.execute(contentType, url, serviceId, params, global.getToken()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                finish();
            }
        };

        tvDone = (TextView) findViewById(R.id.tv_diary_write_done);
        tvDone.setOnClickListener(listener);

        btDone = (Button) findViewById(R.id.bt_diary_write_done);
        btDone.setOnClickListener(listener);
    }
}

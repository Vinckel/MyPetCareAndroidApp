package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.Model.HospitalDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class DiaryWriteActivity extends BaseActivity {
    private ImageButton ibBack;
    private TextView tvDate;
    private TextView tvDone;
    private Button btDone;
    private EditText etContent;

    boolean isNew = true;
    Integer no = -1;

    private int lastSpecialRequestsCursorPosition = 0;
    private String specialRequests = StringUtils.EMPTY;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", true);

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
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = etContent.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etContent.removeTextChangedListener(this);

                if (etContent.getLineCount() > 3) {
                    etContent.setText(specialRequests);
                    etContent.setSelection(lastSpecialRequestsCursorPosition);
                } else
                    specialRequests = etContent.getText().toString();

                etContent.addTextChangedListener(this);
            }
        });

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

                callDiaryWriteApi(isNew, no, encContent);

//                HttpConn diaryWriteApi = new HttpConn();
//                diaryWriteApi.setContext(global);
//
//                String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
//                String serviceId;
//
//                if (isNew) {
//                    serviceId = "MPMS_02002";
//                } else {
//                    serviceId = "MPMS_02003";
//                }
//
//                String contentType = "application/json";
//
//                Map params = new HashMap<>();
//                params.put("USER_EMAIL", global.get("email"));
//                if (!isNew) {
//                    params.put("PET_JOURNAL_SN", no);
//                }
//                params.put("PET_JOURNAL_CN", encContent);
//                HttpResultVO httpResultVO = null;
//                try {
//                    httpResultVO = diaryWriteApi.execute(contentType, url, serviceId, params, global.getToken()).get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }

                finish();
            }
        };

        tvDone = (TextView) findViewById(R.id.tv_diary_write_done);
        tvDone.setOnClickListener(listener);

        btDone = (Button) findViewById(R.id.bt_diary_write_done);
        btDone.setOnClickListener(listener);

        if (!isNew) {
            etContent.setText(intent.getStringExtra("content"));
            no = intent.getIntExtra("no", -1);
            tvDate.setText(intent.getStringExtra("date"));
        }
    }

    private void callDiaryWriteApi(boolean isNew, Integer no, String contents) {
        try {
            DiaryWriteApi diaryWriteApi = new DiaryWriteApi();

            Map headers = new HashMap<>();
            Map params = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = null;

            params.put("PET_JOURNAL_CN", contents);

            if (!isNew) { // 수정
                serviceId = "MPMS_02003";
                params.put("PET_JOURNAL_SN", no);
            } else { // 새로작성
                serviceId = "MPMS_02002";
            }

            headers.put("url", url);
            headers.put("serviceName", serviceId);

            diaryWriteApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DiaryWriteActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class DiaryWriteApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CommonResult commonResult = GsonUtil.fromJson(result, CommonResult.class);
            if (commonResult.getResultCode() != 0) {
                Toast.makeText(DiaryWriteActivity.this, "등록하지 못했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            finish();
        }
    }
}

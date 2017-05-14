package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class InquiryActivity extends BaseActivity {
    private ImageButton ibBack;
    private TextView tvDone;
    private EditText etEmail;
    private EditText etInquiry;
    private Button btDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_inquiry);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_inquiry, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_inquiry_back);
        tvDone = (TextView) findViewById(R.id.tv_inquiry_done);
        etEmail = (EditText) findViewById(R.id.et_inquiry_email);
        etInquiry = (EditText) findViewById(R.id.et_inquiry_content);
        btDone = (Button) findViewById(R.id.bt_inquiry_register);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (global != null && global.getEmail() != null) {
            etEmail.setText(global.getEmail());
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    save();
                }
            }
        };

        tvDone.setOnClickListener(listener);
        btDone.setOnClickListener(listener);

    }


    public class InquiryApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CommonResult resultVO = GsonUtil.fromJson(result, CommonResult.class);
            if (resultVO.getResultCode() != 0) {
                resultVO.getResultMessage();
                Toast.makeText(InquiryActivity.this, "문의 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(InquiryActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            alert.setMessage("문의가 전달되었습니다. 답변은 이메일로 받아보실 수 있습니다.");
            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    private void save() {
        InquiryApi inquiryApi = new InquiryApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_14001";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("QNA_EMAIL", etEmail.getText().toString());
        body.put("QNA_CONTENT", etInquiry.getText().toString());

        inquiryApi.execute(header, body);
    }

    private boolean validate() {
        String errorMessage = null;
        if (StringUtils.isBlank(etEmail.getText())) {
            errorMessage = "이메일을 입력해주세요.";
        } else if (StringUtils.isBlank(etInquiry.getText())) {
            errorMessage = "문의 내용을 입력해주세요.";
        }

        if (StringUtils.isNotEmpty(errorMessage)) {
            Toast.makeText(InquiryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}

package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class InsureRegisterActivity extends BaseActivity {
    private ImageButton ibBack;
    private TextView tvDone;
    private Button btDone;

    private EditText etBreed;
    private EditText etBirth;
    private EditText etEmail;
    private EditText etContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insure_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_insure_register);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_insure_register, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_insure_register_back);
        tvDone = (TextView) findViewById(R.id.tv_insure_register_done);
        btDone = (Button) findViewById(R.id.bt_insure_register_done);

        etBreed = (EditText) findViewById(R.id.et_insure_register_breed);
        etBirth = (EditText) findViewById(R.id.et_insure_register_birth);
        etEmail = (EditText) findViewById(R.id.et_insure_register_email);
        etContact = (EditText) findViewById(R.id.et_insure_register_contact);

        View.OnClickListener doneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {  
                    save();
                }
            }
        };

        tvDone.setOnClickListener(doneClickListener);
        btDone.setOnClickListener(doneClickListener);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class InsureRegisterApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder alert = new AlertDialog.Builder(InsureRegisterActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            alert.setMessage("가입 접수가 완료되었습니다.");
            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    private void save() {

        InsureRegisterApi insureRegisterApi = new InsureRegisterApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01004";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
//        body.put();

//        insureRegisterApi.execute(header, body);
    }

    private boolean validate() {
        String message = null;
        if (StringUtils.isEmpty(etBreed.getText())) {
            message = "종을 입력해주세요.";
        } else if (StringUtils.isEmpty(etBirth.getText())) {
            message = "출생일을 입력해주세요.";
        } else if (StringUtils.isEmpty(etEmail.getText())) {
            message = "이메일을 입력해주세요.";
        } else if (StringUtils.isEmpty(etContact.getText())) {
            message = "연락처를 입력해주세요.";
        }

        if (StringUtils.isNotEmpty(message)) {
            Toast.makeText(InsureRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}

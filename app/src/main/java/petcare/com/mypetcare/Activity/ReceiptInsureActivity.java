package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.PicUtil;

public class ReceiptInsureActivity extends BaseActivity {
    private ImageView ivPicAttach1;
    private ImageView ivPicAttach2;
    private ImageView ivPicAttach3;

    private ImageButton ibBack;
    private TextView tvDone;
    private Button btDone;
    private EditText etName;
    private EditText etTime;
    private EditText etSymptom;
    private EditText etContact;

    private boolean isPicChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_insure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_receipt_insure);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_receipt_insure, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ivPicAttach1 = (ImageView) findViewById(R.id.iv_receipt_insure_attach_1);
        ivPicAttach2 = (ImageView) findViewById(R.id.iv_receipt_insure_attach_2);
        ivPicAttach3 = (ImageView) findViewById(R.id.iv_receipt_insure_attach_3);
        ibBack = (ImageButton) findViewById(R.id.ib_receipt_insure_back);
        tvDone = (TextView) findViewById(R.id.tv_receipt_insure_done);
        btDone = (Button) findViewById(R.id.bt_receipt_insure_done);

        etName = (EditText) findViewById(R.id.et_receipt_insure_name);
        etTime = (EditText) findViewById(R.id.et_receipt_insure_time);
        etSymptom = (EditText) findViewById(R.id.et_receipt_insure_symptom);
        etContact = (EditText) findViewById(R.id.et_receipt_insure_contact);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        ivPicAttach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        ivPicAttach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        ivPicAttach3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 3);
            }
        });
    }

    public class JsonSaveApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder alert = new AlertDialog.Builder(ReceiptInsureActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            CommonResult commonResult = GsonUtil.fromJson(result, CommonResult.class);
            if (commonResult.getResultCode() != 0) {
                commonResult.getResultMessage();
                alert.setMessage("등록에 실패했습니다.");
            } else {
                alert.setMessage("등록이 완료되었습니다.");
            }

            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    public class MultipartApi extends GeneralMultipartApi {

        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);

            AlertDialog.Builder alert = new AlertDialog.Builder(ReceiptInsureActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            alert.setMessage("접수가 완료되었습니다.");

            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    private boolean validate() {
        String errorMessage = "";

        if (StringUtils.isBlank(etName.getText())) {
            errorMessage = "성함을 적어주세요.";
        } else if (StringUtils.isBlank(etTime.getText())) {
            errorMessage = "치료 일시를 적어주세요.";
        } else if (StringUtils.isBlank(etSymptom.getText())) {
            errorMessage = "증상을 적어주세요.";
        } else if (StringUtils.isBlank(etContact.getText())) {
            errorMessage = "연락처를 적어주세요.";
        }

        if (StringUtils.isNotEmpty(errorMessage)) {
            Toast.makeText(ReceiptInsureActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void save() {
        String pic1ImagePath = (String) ivPicAttach1.getTag();
        String pic2ImagePath = (String) ivPicAttach2.getTag();
        String pic3ImagePath = (String) ivPicAttach3.getTag();
        MultipartApi multipartApi = new MultipartApi();
        JsonSaveApi jsonApi = new JsonSaveApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01005";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
//        body.put("PET_TYPE", etName.getText().toString());
//        body.put("PET_BIRTH", etTime,);

        Map<String, String> files = new HashMap<>();
        if (pic1ImagePath != null) {
            files.put("1", pic1ImagePath);
        }
        if (pic2ImagePath != null) {
            files.put("2", pic2ImagePath);
        }
        if (pic3ImagePath != null) {
            files.put("3", pic3ImagePath);
        }

        if (isPicChanged) {
            multipartApi.execute(header, body, files);
        } else {
            jsonApi.execute(header, body);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                Bitmap selectedImage = PicUtil.getPicture(getApplicationContext(), imageUri);
                String pathFromUri = PicUtil.getResizedImagePathFromUri(getApplicationContext(), data.getData(), String.valueOf(requestCode));
                switch (requestCode) {
                    case 1:
                        ivPicAttach1.setTag(pathFromUri);
                        ivPicAttach1.setImageBitmap(selectedImage);
                        break;
                    case 2:
                        ivPicAttach2.setTag(pathFromUri);
                        ivPicAttach2.setImageBitmap(selectedImage);
                        break;
                    case 3:
                        ivPicAttach3.setTag(pathFromUri);
                        ivPicAttach3.setImageBitmap(selectedImage);
                        break;
                }

                isPicChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

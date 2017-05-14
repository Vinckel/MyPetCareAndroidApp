package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Model.MissingDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class MissingDetailActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvFound;
    private TextView tvDate;
    private TextView tvLocation;
    private TextView tvMark;
    private TextView tvPrice;
    private TextView tvEtc;
    private TextView tvBreed;
    private TextView tvColor;
    private TextView tvGender;
    private TextView tvAge;
    private Button btCall;

    private MissingDetailVO.ReportPetObject petObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_missing_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_missing_detail, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        String id = getIntent().getStringExtra("id");
        if (StringUtils.isBlank(id)) {
            Toast.makeText(MissingDetailActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle = (TextView) findViewById(R.id.tv_missing_detail_title);
        tvName = (TextView) findViewById(R.id.tv_missing_detail_name);
        tvFound = (TextView) findViewById(R.id.tv_missing_detail_found);
        tvDate = (TextView) findViewById(R.id.tv_missing_detail_date_desc);
        tvLocation = (TextView) findViewById(R.id.tv_missing_detail_location_desc);
        tvMark = (TextView) findViewById(R.id.tv_missing_detail_mark_desc);
        tvPrice = (TextView) findViewById(R.id.tv_missing_detail_price_desc);
        tvEtc = (TextView) findViewById(R.id.tv_missing_detail_etc_desc);
        tvBreed = (TextView) findViewById(R.id.tv_missing_detail_breed_desc);
        tvColor = (TextView) findViewById(R.id.tv_missing_detail_color_desc);
        tvGender = (TextView) findViewById(R.id.tv_missing_detail_gender_desc);
        tvAge = (TextView) findViewById(R.id.tv_missing_detail_age_desc);
        btCall = (Button) findViewById(R.id.bt_missing_detail_call);

        MissingDetailApi missingDetailApi = new MissingDetailApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("PET_AR_ID", id);

        missingDetailApi.execute(header, body);
    }


    public class MissingDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MissingDetailVO missingDetailVO = GsonUtil.fromJson(result, MissingDetailVO.class);
            if (missingDetailVO.getResultCode() != 0) {
                Toast.makeText(MissingDetailActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                return ;
            }

            petObject = missingDetailVO.getData().get(0);

            tvTitle.setText(petObject.getName());
            tvName.setText(petObject.getName());
            tvDate.setText(petObject.getDate());
            tvLocation.setText(petObject.getLocation());
            tvMark.setText(petObject.getMark());
            tvPrice.setText(petObject.getPrice());
//            tvEtc.setText(petObject.);
            tvBreed.setText(petObject.getBreed());
            tvColor.setText(petObject.getColor());
            tvGender.setText(petObject.getGender());
            tvAge.setText(petObject.getAge());

            btCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                        } else {
//                            String call = petObject.getContact();
                            String call = "";
                            if (StringUtils.isNotBlank(call)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                                startActivity(intent);
                            }
                        }
                    } else {
//                        String call = petObject.getContact();
                        String call = "";
                        if (StringUtils.isNotBlank(call)) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && petObject != null) {

                    String call = "";
                    if (StringUtils.isNotBlank(call)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        }

                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MissingDetailActivity.this, "권한이 거부되어 전화를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.MatingRequestViewpagerAdapter;
import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.PicUtil;

public class ReportWriteActivity extends BaseActivity {
    private static final int PIC_LIMIT_COUNT = 10;
    private ImageButton ibBack;
    private ImageView ivAddPic;
    private ViewPager pager;
    private Map<String, String> paths;
    private List<Bitmap> bitmapPaths;
    private TextView tvPageCount;
    private MatingRequestViewpagerAdapter adapter;
    private RelativeLayout rlPicAreaWithPager;
    private RelativeLayout rlPicTouch;
    private LinearLayout llPicAreaWithAddPhoto;
    private TextView tvDone;
    private TextView tvTitle;

    private EditText etName;
    private EditText etBreed;
    private EditText etColor;
    private ToggleButton btGenderMale;
    private ToggleButton btGenderFemale;
    private EditText etAge;
    private EditText etDate;
    private EditText etLocation;
    private EditText etMark;
    private EditText etPrice;
    private EditText etEtc;
    private EditText etContact;
    private ScrollView sv;

    private RelativeLayout rlDim;
    private ProgressBar pbDim;

    private static String arType = null;
    private static boolean isBlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_report_write);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_report_write, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        arType = getIntent().getStringExtra("arType");
        String arTypeStr = getIntent().getStringExtra("arTypeStr");

        if (StringUtils.isBlank(arType)) {
            Toast.makeText(ReportWriteActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ibBack = (ImageButton) findViewById(R.id.ib_report_write_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llPicAreaWithAddPhoto = (LinearLayout) findViewById(R.id.ll_report_write_pic_area);
        rlPicAreaWithPager = (RelativeLayout) findViewById(R.id.rl_report_write_pic_area);
        rlPicTouch = (RelativeLayout) findViewById(R.id.rl_report_write_pic_touch);

        tvTitle = (TextView) findViewById(R.id.tv_report_write_title);
        etName = (EditText) findViewById(R.id.et_report_write_name);
        etBreed = (EditText) findViewById(R.id.et_report_write_breed);
        etColor = (EditText) findViewById(R.id.et_report_write_color);
        etAge = (EditText) findViewById(R.id.et_report_write_age);
        etDate = (EditText) findViewById(R.id.et_report_write_date);
        etLocation = (EditText) findViewById(R.id.et_report_write_location);
        etMark = (EditText) findViewById(R.id.et_report_write_mark);
        etPrice = (EditText) findViewById(R.id.et_report_write_price);
        etEtc = (EditText) findViewById(R.id.et_report_write_etc);
        etContact = (EditText) findViewById(R.id.et_report_write_contact);
        rlDim = (RelativeLayout) findViewById(R.id.rl_report_dim);
        pbDim = (ProgressBar) findViewById(R.id.pb_report);
        sv = (ScrollView) findViewById(R.id.sv_report);

        btGenderMale = (ToggleButton) findViewById(R.id.bt_report_write_gender_male);
        btGenderFemale = (ToggleButton) findViewById(R.id.bt_report_write_gender_female);

        tvTitle.setText(arTypeStr + " 등록");

        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isBlocked;
            }
        });

        btGenderMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btGenderFemale.setChecked(!isChecked);
                if (isChecked) {
                    buttonView.setTextColor(Color.parseColor("#ffffff"));
                    buttonView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    buttonView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    buttonView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        });

        btGenderFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btGenderMale.setChecked(!isChecked);
                if (isChecked) {
                    buttonView.setTextColor(Color.parseColor("#ffffff"));
                    buttonView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    buttonView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    buttonView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        });

        tvDone = (TextView) findViewById(R.id.tv_report_write_done);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    save();
                }
            }
        });

        ivAddPic = (ImageView) findViewById(R.id.iv_add_pic);
        rlPicTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        tvPageCount = (TextView) findViewById(R.id.tv_report_write_page_count);
        pager = (ViewPager) findViewById(R.id.vp_report_write);
        paths = new HashMap<>();
        adapter = new MatingRequestViewpagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvPageCount.setText((position + 1) + "/" + paths.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bitmapPaths = new ArrayList<>();
    }

    private void onLoadingStart() {
        pager.setEnabled(false);
        rlDim.setVisibility(View.VISIBLE);
        pbDim.setVisibility(View.VISIBLE);

        etName.setEnabled(false);
        etBreed.setEnabled(false);
        etColor.setEnabled(false);
        btGenderMale.setEnabled(false);
        btGenderFemale.setEnabled(false);
        etAge.setEnabled(false);
        etLocation.setEnabled(false);
        etMark.setEnabled(false);
        etPrice.setEnabled(false);
        etEtc.setEnabled(false);
        etContact.setEnabled(false);
        etDate.setEnabled(false);
        isBlocked = true;
    }

    private void onLoadingEnd() {
        pager.setEnabled(true);
        rlDim.setVisibility(View.GONE);
        pbDim.setVisibility(View.GONE);

        etName.setEnabled(true);
        etBreed.setEnabled(true);
        etColor.setEnabled(true);
        btGenderMale.setEnabled(true);
        btGenderFemale.setEnabled(true);
        etAge.setEnabled(true);
        etLocation.setEnabled(true);
        etMark.setEnabled(true);
        etPrice.setEnabled(true);
        etEtc.setEnabled(true);
        etContact.setEnabled(true);
        etDate.setEnabled(true);
        isBlocked = false;
    }

    private boolean validate() {
        String errorMessage = null;

        if (StringUtils.isEmpty(etName.getText())) {
            errorMessage = "이름을 입력하세요.";
            etName.setFocusableInTouchMode(true);
        } else if (StringUtils.isEmpty(etBreed.getText())) {
            errorMessage = "견종을 입력하세요.";
            etBreed.setFocusableInTouchMode(true);
        } else if (StringUtils.isEmpty(etColor.getText())) {
            errorMessage = "컬러를 입력하세요.";
            etColor.setFocusableInTouchMode(true);
        } else if (!btGenderFemale.isChecked() && !btGenderMale.isChecked()) {
            errorMessage = "성별을 입력하세요.";
        } else if (StringUtils.isEmpty(etAge.getText())) {
            errorMessage = "나이를 입력하세요.";
            etAge.setFocusableInTouchMode(true);
        } else if (bitmapPaths.size() == 0) {
            errorMessage = "사진을 1장 이상 추가해주세요.";
        } else if (StringUtils.isBlank(etLocation.getText())) {
            errorMessage = "실종장소를 입력하세요.";
        } else if (StringUtils.isBlank(etContact.getText())) {
            errorMessage = "연락처를 입력하세요.";
        }

        if (StringUtils.isNotEmpty(errorMessage)) {
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void save() {
        MultipartApi multipartApi = new MultipartApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11003";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("PET_AR_TYPE", arType);
        body.put("PET_AR_NM", etName.getText().toString());
        body.put("PET_AR_KND", etBreed.getText().toString());
        body.put("PET_AR_COLOR", etColor.getText().toString());
        body.put("PET_AR_SEX", btGenderMale.isChecked() ? "남" : "여");
        body.put("PET_AR_AGE", etAge.getText().toString());
        body.put("PET_AR_PLACE", etLocation.getText().toString());
        body.put("PET_AR_DATE", etDate.getText().toString());
        body.put("PET_AR_CHAR", etMark.getText().toString());
        body.put("PET_AR_REWARD", etPrice.getText().toString());
        body.put("PET_AR_DESC", etEtc.getText().toString());
        body.put("PET_AR_TELNUM", etContact.getText().toString());
        body.put("PET_AR_FIND_AT", "N");

        multipartApi.execute(header, body, paths);
        onLoadingStart();
    }

    public class MultipartApi extends GeneralMultipartApi {

        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);

            AlertDialog.Builder alert = new AlertDialog.Builder(ReportWriteActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    setResult(1);
                    finish();
                }
            });

            boolean isAllOk = true;
            for (String each : resultList) {
                CommonResult result = GsonUtil.fromJson(each, CommonResult.class);
                if (result.getResultCode() != 0) {
                    result.getResultMessage();
                    isAllOk = false;
                }
            }

            onLoadingEnd();

            if (isAllOk) {
                alert.setMessage("등록이 완료되었습니다.");
            } else {
                alert.setMessage("등록이 완료되었습니다.\n일부 이미지 등록에 실패했습니다.");
            }

            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();

            try {
                if (clipData != null) {
                    if (clipData.getItemCount() > PIC_LIMIT_COUNT) {
                        Toast.makeText(getApplicationContext(), "이미지는 " + PIC_LIMIT_COUNT + "개 이하만 가능합니다.", Toast.LENGTH_SHORT).show();
                    }

                    int count = clipData.getItemCount() > PIC_LIMIT_COUNT ? PIC_LIMIT_COUNT : clipData.getItemCount();

                    for (int i = 0; i < count; i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        paths.put(String.valueOf(i + 1), PicUtil.getPathFromUri(getApplicationContext(), uri));
                        bitmapPaths.add(PicUtil.getPicture(getApplicationContext(), uri));
                    }
                } else {
                    Uri uri = data.getData();

                    paths.put("1", PicUtil.getPathFromUri(getApplicationContext(), uri));
                    bitmapPaths.add(PicUtil.getPicture(getApplicationContext(), uri));
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "파일을 불러오던 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("error", e.getMessage());
            }

            adapter.addItemAll(bitmapPaths);
            llPicAreaWithAddPhoto.setVisibility(View.GONE);
            rlPicAreaWithPager.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}

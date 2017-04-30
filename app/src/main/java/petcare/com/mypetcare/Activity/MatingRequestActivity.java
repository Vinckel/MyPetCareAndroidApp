package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

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

public class MatingRequestActivity extends AppCompatActivity {
    private static final int PIC_LIMIT_COUNT = 2;
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

    private EditText etName;
    private EditText etBreed;
    private EditText etColor;
    private ToggleButton btGenderMale;
    private ToggleButton btGenderFemale;
    private EditText etAge;
    private EditText etIntroduce;

    private boolean genderPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mating_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_mating_request);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_mating_request, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_mating_request_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llPicAreaWithAddPhoto = (LinearLayout) findViewById(R.id.ll_pic_area);
        rlPicAreaWithPager = (RelativeLayout) findViewById(R.id.rl_pic_area);
        rlPicTouch = (RelativeLayout) findViewById(R.id.rl_pic_touch);

        etName = (EditText) findViewById(R.id.et_mating_request_name);
        etBreed = (EditText) findViewById(R.id.et_mating_request_breed);
        etColor = (EditText) findViewById(R.id.et_mating_request_color);
        etAge = (EditText) findViewById(R.id.et_mating_request_age);
        etIntroduce = (EditText) findViewById(R.id.et_mating_request_intro);
        btGenderMale = (ToggleButton) findViewById(R.id.bt_mating_request_gender_male);
        btGenderFemale = (ToggleButton) findViewById(R.id.bt_mating_request_gender_female);

//        View.OnClickListener toggleButtonListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btGenderMale.isChecked()) {
//                    btGenderFemale.setChecked(false);
//                } else {
//                    btGenderFemale.setChecked(true);
//                }
//            }
//        };

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

        tvDone = (TextView) findViewById(R.id.tv_mating_request_done);
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

        tvPageCount = (TextView) findViewById(R.id.tv_mating_detail_page_count);
        pager = (ViewPager) findViewById(R.id.vp_mating_request);
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
        String serviceId = "MPMS_01004";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
//        body.put();

//        multipartApi.execute(header, body, paths);
    }
    public class MultipartApi extends GeneralMultipartApi {

        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);

            AlertDialog.Builder alert = new AlertDialog.Builder(MatingRequestActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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
                    List<ClipData.Item> itemList = new ArrayList<>();
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

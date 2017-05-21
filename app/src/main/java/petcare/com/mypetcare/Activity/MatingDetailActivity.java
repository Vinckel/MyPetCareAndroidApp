package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.AdoptDetailViewpagerAdapter;
import petcare.com.mypetcare.Adapter.MatingDetailViewpagerAdapter;
import petcare.com.mypetcare.Model.MatingDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class MatingDetailActivity extends BaseActivity {
    private ViewPager pager;
    private ImageButton ibBack;
    private TextView tvPageCount;
    private List<String> urls;
    private static double longitude = -1.0;
    private static double latitude = -1.0;
    private static String name = "";
    private static String radius = "";
    private static MatingDetailVO.MatingDetailObject matingDetailObject;

    private TextView tvTitle;
    private TextView tvName;
    private TextView tvSubTitle;
    private TextView tvHomepage;
    private TextView tvBreed;
    private TextView tvColor;
    private TextView tvGender;
    private TextView tvAge;
    private Button btCall;

    private MatingDetailViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mating_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_mating);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_mating_detail, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (StringUtils.isBlank(id)) {
            Toast.makeText(MatingDetailActivity.this, "정보를 읽어오지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

//        ivMap = (ImageView) findViewById(R.id.iv_mating_detail_map);
//        ivMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (longitude < 0 || latitude < 0) {
//                    return ;
//                }
//                Intent intent = new Intent(getApplicationContext(), HospitalMapActivity.class);
//                intent.putExtra("longitude", longitude);
//                intent.putExtra("latitude", latitude);
//                intent.putExtra("name", name);
//
//                startActivity(intent);
//            }
//        });

        tvTitle = (TextView) findViewById(R.id.tv_mating_detail_title);
        tvName = (TextView) findViewById(R.id.tv_mating_detail_name);
        tvSubTitle = (TextView) findViewById(R.id.tv_mating_detail_subtitle);
        tvHomepage = (TextView) findViewById(R.id.tv_mating_detail_url);
        tvBreed = (TextView) findViewById(R.id.tv_mating_detail_breed_desc);
        tvColor = (TextView) findViewById(R.id.tv_mating_detail_color_desc);
        tvGender = (TextView) findViewById(R.id.tv_mating_detail_gender_desc);
        tvAge = (TextView) findViewById(R.id.tv_mating_detail_age_desc);
        btCall = (Button) findViewById(R.id.bt_mating_detail_call);

        tvPageCount = (TextView) findViewById(R.id.tv_mating_detail_page_count);
        ibBack = (ImageButton) findViewById(R.id.ib_mating_detail_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.vp_mating_detail);
        adapter = new MatingDetailViewpagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvPageCount.setText((position + 1) + "/" + adapter.getCount());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        callMatingDetailApi(id);
    }

    private void callMatingDetailApi(String id) {
        try {
            MatingDetailApi matingDetailApi = new MatingDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_10002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("PET_CB_ID", id);

            matingDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MatingDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class MatingDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                MatingDetailVO matingDetailVO = GsonUtil.fromJson(result, MatingDetailVO.class);
                if (matingDetailVO.getResultCode() != 0) {
                    Toast.makeText(MatingDetailActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                List<MatingDetailVO.MatingDetailObject> data = matingDetailVO.getData();
                matingDetailObject = data.get(0);
                tvTitle.setText(matingDetailObject.getName());
                tvName.setText(matingDetailObject.getName());
//            tvSubTitle.setText(matingDetailObject.get);
//            tvHomepage.setText(matingDetailObject.get);
                tvBreed.setText(matingDetailObject.getBreed());
                tvColor.setText(matingDetailObject.getColor());
                tvGender.setText(matingDetailObject.getGender());
                tvAge.setText(matingDetailObject.getAge());

                List<MatingDetailVO.MatingDetailObject.MatingDetailImageObject> imgData = matingDetailObject.getImgData();
                for (MatingDetailVO.MatingDetailObject.MatingDetailImageObject imageObject : imgData) {
                    adapter.addItem(imageObject.getImgUrl());
                }

                adapter.notifyDataSetChanged();

                btCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                            } else {
//                            String call = matingDetailObject.getContact();
                                String call = "";
                                if (StringUtils.isNotBlank(call)) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                                    startActivity(intent);
                                }
                            }
                        } else {
//                        String call = matingDetailObject.getContact();
                            String call = "";
                            if (StringUtils.isNotBlank(call)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                                startActivity(intent);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MatingDetailActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && matingDetailObject != null) {

//                    String call = matingDetailObject.getContact();
                    String call = "";
                    if (StringUtils.isNotBlank(call)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        }

                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MatingDetailActivity.this, "권한이 거부되어 전화를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

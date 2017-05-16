package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.AdoptDetailViewpagerAdapter;
import petcare.com.mypetcare.Model.AdoptDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class AdoptDetailActivity extends BaseActivity {
    private ViewPager pager;
    private ImageButton ibBack;
    private TextView tvPageCount;
    private List<String> urls;
    private ImageView ivMap;
    private AdoptDetailViewpagerAdapter adapter;
    private AdoptDetailVO.AdoptObject adoptObject;

    private TextView tvTitle;
    private TextView tvActionBarTitle;
    private TextView tvSubtitle;
    private TextView tvUrl;
    private TextView tvBreed;
    private TextView tvColor;
    private TextView tvGender;
    private TextView tvAge;
    private TextView tvInoculation;
    private TextView tvPrice;
    private TextView tvDescription;
    private Button btCall;
    private static double longitude = -1.0;
    private static double latitude = -1.0;
    private static double searchLongitude = -1.0;
    private static double searchLatitude = -1.0;
    private static String radius = "";
    private static String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_adopt);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.adopt_detail_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        String id = getIntent().getStringExtra("id");
        searchLongitude = getIntent().getDoubleExtra("id", -1.0);
        searchLatitude = getIntent().getDoubleExtra("id", -1.0);
        radius = getIntent().getStringExtra("radius");

        if (StringUtils.isBlank(id)) {
            Toast.makeText(AdoptDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ivMap = (ImageView) findViewById(R.id.iv_adopt_detail_map);
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (longitude < 0 || latitude < 0) {
                    return ;
                }
                Intent intent = new Intent(getApplicationContext(), HospitalMapActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("name", name);

                startActivity(intent);
            }
        });
        tvPageCount = (TextView) findViewById(R.id.tv_adopt_detail_page_count);
        ibBack = (ImageButton) findViewById(R.id.ib_adopt_detail_back);
        btCall = (Button) findViewById(R.id.bt_adopt_detail_call);
        tvActionBarTitle = (TextView) findViewById(R.id.tv_adopt_detail_title);
        tvTitle = (TextView) findViewById(R.id.tv_adopt_detail_title_content);
        tvSubtitle = (TextView) findViewById(R.id.tv_adopt_detail_subtitle);
        tvUrl = (TextView) findViewById(R.id.tv_adopt_detail_url);
        tvBreed = (TextView) findViewById(R.id.tv_adopt_detail_breed_desc);
        tvColor = (TextView) findViewById(R.id.tv_adopt_detail_color_desc);
        tvGender = (TextView) findViewById(R.id.tv_adopt_detail_gender_desc);
        tvAge = (TextView) findViewById(R.id.tv_adopt_detail_age_desc);
        tvInoculation = (TextView) findViewById(R.id.tv_adopt_detail_inoc_desc);
        tvPrice = (TextView) findViewById(R.id.tv_adopt_detail_price_desc);
        tvDescription = (TextView) findViewById(R.id.tv_adopt_detail_description);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.vp_adopt_detail);
        adapter = new AdoptDetailViewpagerAdapter(getLayoutInflater());
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

        callHospitalDetailApi(id);
    }

    private void callHospitalDetailApi(String id) {
        try {
            AdoptDetailApi adoptDetailApi = new AdoptDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_09002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", searchLongitude);
            params.put("SEARCH_LAT", searchLatitude);
            params.put("SEARCH_RADIUS", radius);

            adoptDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AdoptDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class AdoptDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AdoptDetailVO adoptDetailVO = GsonUtil.fromJson(result, AdoptDetailVO.class);
            if (adoptDetailVO.getResultCode() != 0) {
                Toast.makeText(AdoptDetailActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            List<AdoptDetailVO.AdoptObject> data = adoptDetailVO.getData();
            adoptObject = data.get(0);
            tvTitle.setText(adoptObject.getName());
            tvActionBarTitle.setText(adoptObject.getName());
//            tvName.setText(adoptObject.getName());
//            tvSubTitle.setText(hospitalObject.getSubTitle());
            tvUrl.setText(adoptObject.getPlaceUrl());
            latitude = adoptObject.getLatitude();
            longitude = adoptObject.getLongitude();
            name = adoptObject.getName();
//            if (StringUtils.isNotBlank(adoptObject.getDistance())) {
//                double distance = Double.parseDouble(adoptObject.getDistance());
//                distance = Math.round(distance / 100f) / 10f;
//                tvDistance.setText(String.format("%.1f", distance) + "km");
//            }

            adapter.addItem(adoptObject.getImgurl());

            if (StringUtils.isNotBlank(adoptObject.getImgurl())) {
                tvPageCount.setText("1/1");
            }

            adapter.notifyDataSetChanged();

            btCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                        } else {
                            String call = adoptObject.getContact();
                            if (StringUtils.isNotBlank(call)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                                startActivity(intent);
                            }
                        }
                    } else {
                        String call = adoptObject.getContact();
                        if (StringUtils.isNotBlank(call)) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}

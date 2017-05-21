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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.HospitalDetailViewpagerAdapter;
import petcare.com.mypetcare.Model.HospitalDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class HospitalDetailActivity extends BaseActivity {
    private ViewPager pager;
    private ImageButton ibBack;
    private TextView tvPageCount;
    private List<String> urls;
    private ImageView ivMap;

    private TextView tvTitle;
    private TextView tvName;
    private TextView tvDistance;
    private TextView tvSubTitle;
    private TextView tvTime;
    private TextView tvUrl;
    private TextView tvDescription;
    private Button btCall;
    private ImageView ivZero;

    private HospitalDetailVO.HospitalObject hospitalObject;

    private static final int NUM_HOSPITAL = 0;
    private static final int NUM_BEAUTY = 1;
    private static final int NUM_HOTEL = 2;
    private static final int NUM_TOOL = 3;
    private static final int NUM_CAFE = 4;
    private static final int NUM_FUNERAL = 5;
    private static final int NUM_ADOPT = 6;
    private static int currentNum = -1;
    private static double longitude = -1.0;
    private static double latitude = -1.0;
    private static String radius = "";
    private static String name = "";

    private HospitalDetailViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        longitude = intent.getDoubleExtra("searchLongitude", -1.0);
        latitude = intent.getDoubleExtra("searchLatitude", -1.0);
        radius = intent.getStringExtra("radius");
        currentNum = intent.getIntExtra("num", -1);

        if (currentNum < 0) {
            Toast.makeText(HospitalDetailActivity.this, "정보를 읽어오지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setContentView(R.layout.activity_hospital_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_hospital_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.hospital_detail_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ivMap = (ImageView) findViewById(R.id.iv_hospital_detail_map);
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

        ivZero = (ImageView) findViewById(R.id.iv_hospital_detail_zero);
        tvPageCount = (TextView) findViewById(R.id.tv_hospital_detail_page_count);
        ibBack = (ImageButton) findViewById(R.id.ib_hospital_detail_back);
        tvTitle = (TextView) findViewById(R.id.tv_hospital_detail_title);
        tvName = (TextView) findViewById(R.id.tv_hospital_detail_name);
        tvDistance = (TextView) findViewById(R.id.tv_hospital_detail_distance);
        tvSubTitle = (TextView) findViewById(R.id.tv_hospital_detail_subtitle);
        tvTime = (TextView) findViewById(R.id.tv_hospital_detail_time);
        tvUrl = (TextView) findViewById(R.id.tv_hospital_detail_url);
        tvDescription = (TextView) findViewById(R.id.tv_hospital_detail_description);
        btCall = (Button) findViewById(R.id.bt_hospital_detail_call);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.vp_hospital_detail);
        urls = new ArrayList<>();
//        urls.add("http://i.imgur.com/3jXjgTT.jpg");
//        urls.add("http://i.imgur.com/SEBjThb.jpg");
//        urls.add("http://i.imgur.com/SEBjThb.jpg");
//        urls.add("http://i.imgur.com/SEBjThb.jpg");
//        urls.add("http://i.imgur.com/SEBjThb.jpg");
        adapter = new HospitalDetailViewpagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                tvPageCount.setText((position + 1) + "/" + urls.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switch (currentNum) {
            case NUM_HOSPITAL:
                callHospitalDetailApi(id);
                break;
            case NUM_BEAUTY:
                callBeautyDetailApi(id);
                break;
            case NUM_HOTEL:
                callHotelDetailApi(id);
                break;
            case NUM_TOOL:
                callToolDetailApi(id);
                break;
            case NUM_CAFE:
                callCafeDetailApi(id);
                break;
            case NUM_FUNERAL:
                callFuneralDetailApi(id);
                break;
            case NUM_ADOPT:
                callAdoptDetailApi(id);
                break;
        }
    }

    private void callHospitalDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_03002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callBeautyDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_04002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callHotelDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_05002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callToolDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_06002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callCafeDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_07002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callAdoptDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_09002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callFuneralDetailApi(String id) {
        try {
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_08002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("ID", id);
            params.put("SEARCH_LON", longitude);
            params.put("SEARCH_LAT", latitude);
            params.put("SEARCH_RADIUS", radius);

            hospitalDetailApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HospitalDetailActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class HospitalDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            HospitalDetailVO hospitalDetailVO = GsonUtil.fromJson(result, HospitalDetailVO.class);
            if (hospitalDetailVO.getResultCode() != 0) {
                Toast.makeText(HospitalDetailActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            List<HospitalDetailVO.HospitalObject> data = hospitalDetailVO.getData();
            hospitalObject = data.get(0);
            tvTitle.setText(hospitalObject.getName());
            tvName.setText(hospitalObject.getName());
//            tvSubTitle.setText(hospitalObject.getSubTitle());
            tvUrl.setText(hospitalObject.getPlaceUrl());
            if (StringUtils.isNotBlank(hospitalObject.getDistance())) {
                double distance = Double.parseDouble(hospitalObject.getDistance());
                distance = Math.round(distance / 100f) / 10f;
                tvDistance.setText(String.format("%.1f", distance) + "km");
            }

            latitude = hospitalObject.getLatitude();
            longitude = hospitalObject.getLongitude();
            name = hospitalObject.getName();

            adapter.addItem(hospitalObject.getImgurl());

            if (StringUtils.isNotBlank(hospitalObject.getImgurl())) {
                ivZero.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);
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
                            String call = hospitalObject.getContact();
                            if (StringUtils.isNotBlank(call)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                                startActivity(intent);
                            }
                        }
                    } else {
                        String call = hospitalObject.getContact();
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
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && hospitalObject != null) {

                    String call = hospitalObject.getContact();
                    if (StringUtils.isNotBlank(call)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        }

                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(HospitalDetailActivity.this, "권한이 거부되어 전화를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

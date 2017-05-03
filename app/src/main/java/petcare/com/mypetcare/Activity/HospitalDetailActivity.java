package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.HospitalDetailViewpagerAdapter;
import petcare.com.mypetcare.Model.HospitalDetailVO;
import petcare.com.mypetcare.Model.HospitalListVO;
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

    private HospitalDetailViewpagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

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
                Intent intent = new Intent(getApplicationContext(), HospitalMapActivity.class);
                startActivity(intent);
            }
        });

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
                tvPageCount.setText((position + 1) + "/" + urls.size());
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
            HospitalDetailApi hospitalDetailApi = new HospitalDetailApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_03002";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("HOSP_ID", id);

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
                return;
            }

            List<HospitalDetailVO.HospitalObject> data = hospitalDetailVO.getData();
            HospitalDetailVO.HospitalObject hospitalObject = data.get(0);
            tvTitle.setText(hospitalObject.getName());
            tvName.setText(hospitalObject.getName());
//            tvSubTitle.setText(hospitalObject.getSubTitle());
            tvUrl.setText(hospitalObject.getPlaceUrl());
            tvDistance.setText(hospitalObject.getDistance() + "km");
            adapter.addItem(hospitalObject.getImgurl());
            tvPageCount.setText("1/1");
            adapter.notifyDataSetChanged();
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import petcare.com.mypetcare.Adapter.AdoptDetailViewpagerAdapter;
import petcare.com.mypetcare.Adapter.AnnounceDetailViewpagerAdapter;
import petcare.com.mypetcare.Model.AnnounceInfoListVO;
import petcare.com.mypetcare.R;

public class AnnounceActivity extends BaseActivity {
    private static final SimpleDateFormat SDF_RAW = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat SDF_REFINED = new SimpleDateFormat("yyyy. MM. dd");
    private ViewPager pager;
    private ImageButton ibBack;
    private TextView tvPageCount;
    private List<String> urls;

    private TextView tvName;
    private TextView tvDate;
    private TextView tvLocation;
    private TextView tvMark;
    private TextView tvPrice;
    private TextView tvEtc;
    private TextView tvHomepage;
    private TextView tvBreed;
    private TextView tvColor;
    private TextView tvGender;
    private TextView tvAge;
    private Button btCall;
    private AnnounceDetailViewpagerAdapter adapter;

    private AnnounceInfoListVO.AnnounceInfoObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_announce);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_announce, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        data = (AnnounceInfoListVO.AnnounceInfoObject) getIntent().getSerializableExtra("data");

        tvName = (TextView) findViewById(R.id.tv_announce_name);
        tvDate = (TextView) findViewById(R.id.tv_announce_date_desc);
        tvLocation = (TextView) findViewById(R.id.tv_announce_location_desc);
        tvMark = (TextView) findViewById(R.id.tv_announce_mark_desc);
        tvPrice = (TextView) findViewById(R.id.tv_announce_price_desc);
        tvEtc = (TextView) findViewById(R.id.tv_announce_etc_desc);
        tvHomepage = (TextView) findViewById(R.id.tv_announce_homepage_desc);
        tvBreed = (TextView) findViewById(R.id.tv_announce_breed_desc);
        tvColor = (TextView) findViewById(R.id.tv_announce_color_desc);
        tvGender = (TextView) findViewById(R.id.tv_announce_gender_desc);
        tvAge = (TextView) findViewById(R.id.tv_announce_age_desc);
        btCall = (Button) findViewById(R.id.bt_announce_call);

//        tvName.setText(data.get);
        try {
            Date date = SDF_RAW.parse(data.getReceptDate());
            SDF_REFINED.format(date);
            tvDate.setText(SDF_REFINED.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            tvDate.setText("알 수 없음");
        }

        tvLocation.setText(data.getReceptPlace());
        tvMark.setText(data.getMark());
//        tvPrice.setText();
//        tvEtc.setText();
//        tvHomepage.setText();
        tvBreed.setText(data.getBreed());
        tvColor.setText(data.getColor());
        tvGender.setText(data.getGender());
        tvAge.setText(data.getAge());

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    String call = data.getShelterCall();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
                    startActivity(intent);
                }
            }
        });

        tvPageCount = (TextView) findViewById(R.id.tv_announce_page_count);
        ibBack = (ImageButton) findViewById(R.id.ib_announce_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pager = (ViewPager) findViewById(R.id.vp_announce);
        adapter = new AnnounceDetailViewpagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        adapter.addItem(data.getImageUrl());
        adapter.notifyDataSetChanged();
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
    }

    @TargetApi(23)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
        } else {
            String call = data.getShelterCall();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String call = data.getShelterCall();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(AnnounceActivity.this, "권한이 거부되어 전화를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

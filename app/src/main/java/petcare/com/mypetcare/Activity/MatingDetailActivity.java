package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Adapter.AdoptDetailViewpagerAdapter;
import petcare.com.mypetcare.R;

public class MatingDetailActivity extends BaseActivity {
    private ViewPager pager;
    private ImageButton ibBack;
    private TextView tvPageCount;
    private List<String> urls;
    private ImageView ivMap;

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

        ivMap = (ImageView) findViewById(R.id.iv_mating_detail_map);
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HospitalMapActivity.class);

                startActivity(intent);
            }
        });
        tvPageCount = (TextView) findViewById(R.id.tv_mating_detail_page_count);
        ibBack = (ImageButton) findViewById(R.id.ib_mating_detail_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.vp_mating_detail);
        AdoptDetailViewpagerAdapter adapter = new AdoptDetailViewpagerAdapter(getLayoutInflater());
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
    }
}

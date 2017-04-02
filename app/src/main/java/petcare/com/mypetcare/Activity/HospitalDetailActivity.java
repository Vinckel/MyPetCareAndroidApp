package petcare.com.mypetcare.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import petcare.com.mypetcare.Adapter.HospitalDetailViewpagerAdapter;
import petcare.com.mypetcare.R;

public class HospitalDetailActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.vp_hospital_detail);
        pager.setAdapter(new HospitalDetailViewpagerAdapter(getLayoutInflater()));
    }
}

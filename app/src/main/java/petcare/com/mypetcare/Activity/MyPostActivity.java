package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.HospitalDetailViewpagerAdapter;
import petcare.com.mypetcare.Adapter.MyPostListViewAdapter;
import petcare.com.mypetcare.Model.MissingDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class MyPostActivity extends BaseActivity {
    private ImageButton ibBack;
    private ListView lvList;
    private MyPostListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_my_post);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_my_post, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_my_post_back);
        lvList = (ListView) findViewById(R.id.lv_my_post);

        adapter = new MyPostListViewAdapter(getApplicationContext());
        lvList.setAdapter(adapter);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyPostListApi myPostListApi = new MyPostListApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
//        body.put("PET_AR_ID", id);
//        myPostListApi.execute(header, body);
    }

    public class MyPostListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            MyPostVO myPostVO = GsonUtil.fromJson(result, MyPostVO.class);
//            if (myPostVO.getResultCode() != 0) {
//                Toast.makeText(MyPostActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//                return;
//            }
//
//            postObject = myPostVO.getData().get(0);

//            adapter.addItem(null, null, null, null);

//            for () {
//
//            }

            adapter.notifyDataSetChanged();
        }
    }
}

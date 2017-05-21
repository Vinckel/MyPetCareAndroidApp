package petcare.com.mypetcare.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Adapter.NoticeListViewAdapter;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;

public class NoticeActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private ListView lvList;
    private NoticeListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_notice);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_notice, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_my_post_back);
        lvList = (ListView) findViewById(R.id.lv_notice);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new NoticeListViewAdapter(getApplicationContext());
        lvList.setAdapter(adapter);

        NoticeListApi noticeListApi = new NoticeListApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
//        body.put("PET_AR_ID", id);
//        myPostListApi.execute(header, body);
    }

    public class NoticeListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            NoticeListVO noticeListVO = GsonUtil.fromJson(result, NoticeListVO.class);
//            if (noticeListVO.getResultCode() != 0) {
//                Toast.makeText(MyPostActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//                return;
//            }
//
//            noticeObject = noticeListVO.getData().get(0);

//            adapter.addItem(null, null, null, null);

//            for () {
//
//            }

            adapter.notifyDataSetChanged();
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.MyPostListViewAdapter;
import petcare.com.mypetcare.Model.MyPostListVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class MyPostActivity extends BaseActivity {
    private ImageButton ibBack;
    private ListView lvList;
    private MyPostListViewAdapter adapter;
    private int pagingCount = 1;
    private static final long SCROLL_MIN_TERM = 1000L;
    private static long scrollCooldown = 0L;
    private static final int SEARCH_COUNT = 20;
    private static final SimpleDateFormat sdfRaw = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

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

        callMyPostApi();
    }

    private void callMyPostApi() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown = currentTime;
        }

        MyPostListApi myPostListApi = new MyPostListApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_16001";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map params = new HashMap<>();
        params.put("SEARCH_COUNT", SEARCH_COUNT);
        params.put("SEARCH_PAGE", pagingCount++);
        myPostListApi.execute(header, params);
    }

    public class MyPostListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                MyPostListVO noticeListVO = GsonUtil.fromJson(result, MyPostListVO.class);
                if (noticeListVO.getResultCode() == -1001) {
                    return;
                }

                if (noticeListVO.getResultCode() != 0) {
                    Toast.makeText(MyPostActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                List<MyPostListVO.MyPostObject> data = noticeListVO.getData();

                for (MyPostListVO.MyPostObject myPostObject : data) {
                    adapter.addItem(myPostObject.getPetName(), sdf.format(sdfRaw.parse(myPostObject.getCreateDate())), myPostObject.getDivide(), myPostObject.getType(), myPostObject.getThumbImgUrl());
                }

                adapter.notifyDataSetChanged();

                lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount >= totalItemCount && adapter.getCount() > 0) {
                            Log.d("listview mypost", "reached at bottom");
                            callMyPostApi();
                        }
                    }

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                });

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyPostActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Activity.SearchFragment.SlidingTabsBasicFragment;
import petcare.com.mypetcare.Adapter.NoticeListViewAdapter;
import petcare.com.mypetcare.Model.AnnounceInfoListVO;
import petcare.com.mypetcare.Model.NoticeListData;
import petcare.com.mypetcare.Model.NoticeListVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class NoticeActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private ListView lvList;
    private NoticeListViewAdapter adapter;
    private NoticeListApi noticeListApi;
    private int pagingCount = 1;
    private static final long SCROLL_MIN_TERM = 1000L;
    private static long scrollCooldown = 0L;
    private static final int SEARCH_COUNT = 20;
    private static final SimpleDateFormat sdfRaw = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

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

        ibBack = (ImageButton) findViewById(R.id.ib_notice_back);
        lvList = (ListView) findViewById(R.id.lv_notice);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new NoticeListViewAdapter(getApplicationContext());
        lvList.setAdapter(adapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                NoticeListData item = adapter.getItem(position);
                intent.putExtra("id", item.getId());

                startActivity(intent);
            }
        });

        callNoticeListApi();
    }

    private void callNoticeListApi() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown = currentTime;
        }

        try {
            noticeListApi = new NoticeListApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_15001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            params.put("SEARCH_PAGE", pagingCount++);

            noticeListApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NoticeActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public class NoticeListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                NoticeListVO noticeListVO = GsonUtil.fromJson(result, NoticeListVO.class);
                if (noticeListVO.getResultCode() == -1001) {
                    return;
                }

                if (noticeListVO.getResultCode() != 0) {
                    Toast.makeText(NoticeActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                List<NoticeListVO.NoticeObject> data = noticeListVO.getData();

                for (NoticeListVO.NoticeObject noticeObject : data) {
                    try {
                        adapter.addItem(noticeObject.getId(), noticeObject.getName(), sdf.format(sdfRaw.parse(noticeObject.getCreateDate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

                lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem + visibleItemCount >= totalItemCount && adapter.getCount() > 0) {
                            Log.d("listview notice", "reached at bottom");
                            callNoticeListApi();
                        }
                    }

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(NoticeActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }
}

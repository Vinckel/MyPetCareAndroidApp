package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import petcare.com.mypetcare.Adapter.DiaryListViewAdapter;
import petcare.com.mypetcare.Model.HttpResultVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.HttpConn;

public class DiaryListActivity extends BaseActivity {
    private ImageView ivWrite;
    private static final SimpleDateFormat SDF_DATA = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat SDF_VIEW_MD = new SimpleDateFormat("MM.dd");
    private static final SimpleDateFormat SDF_VIEW_Y = new SimpleDateFormat("yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.diary_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.diary_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ivWrite = (ImageView) findViewById(R.id.iv_diary_write);
        ivWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryListActivity.this, DiaryWriteActivity.class);
                intent.putExtra("isNew", true);
//                intent.putExtra("writeTargetNo", writeTargetNo);
                startActivity(intent);
            }
        });

        final ListView lv = (ListView) findViewById(R.id.lv_diary_list);
        DiaryListViewAdapter adapter = new DiaryListViewAdapter(getApplicationContext());
        lv.setAdapter(adapter);

        HttpConn diaryListApi = new HttpConn();
        diaryListApi.setContext(global);

        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_02001";
        String contentType = "application/json";

        Map params = new HashMap<>();
        params.put("USER_EMAIL", global.get("email"));
        params.put("SEARCH_COUNT", 20);
        params.put("SEARCH_PAGE", 1);
        HttpResultVO httpResultVO = null;

        try {
            httpResultVO = diaryListApi.execute(contentType, url, serviceId, params, global.getToken()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        if (httpResultVO != null) {
//            Integer totalCount = MapUtils.getInteger((Map) httpResultVO.getData().get(0), "TOTAL_COUNT");
//        }

        if (httpResultVO != null && httpResultVO.getData() != null && httpResultVO.getData().size() > 0) {
            List<DataObj> parseData = new ArrayList<>();
            List<Object> data = httpResultVO.getData();

            for (int i = 0; i < data.size(); i++) {
                DataObj dataObj = new DataObj();
                String registerTime = MapUtils.getString((Map) data.get(i), "FRST_REGIST_DTIME");
                String contents = MapUtils.getString((Map) data.get(i), "PET_JOURNAL_CN");
                try {
                    dataObj.setContent(URLDecoder.decode(contents, "UTF-8"));
                    dataObj.setCreateDate(SDF_DATA.parse(registerTime));
                    parseData.add(dataObj);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(parseData, new Comp());

            for (DataObj dataObj : parseData) {

            }
        }

        adapter.addItem("1.14", "dkdkdkdkdk");
        adapter.addItem("2.14", "dkdkdkdkdk");
        adapter.addItem("3.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
        adapter.addItem("4.14", "dkdkdkdkdk");
    }

    class DataObj {
        private Date createDate;
        private String content;

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    class Comp implements Comparator<DataObj> {
        public int compare(DataObj s1, DataObj s2) {
            return s1.getCreateDate().compareTo(s2.getCreateDate());
        }
    }
}

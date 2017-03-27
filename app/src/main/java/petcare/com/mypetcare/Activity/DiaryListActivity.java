package petcare.com.mypetcare.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.MapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.DiaryListViewAdapter;
import petcare.com.mypetcare.Model.DiaryListData;
import petcare.com.mypetcare.Model.HttpResultVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.HttpConn;

public class DiaryListActivity extends BaseActivity {
    private ImageView ivWrite;
    private ImageButton ibBack;
    private ListView lvDiary;
    private DiaryListViewAdapter adapter;
    private PopupWindow popup;
    private DiaryListData datapopupCurrentData;
    private int currentPosition = 1;
    private static final int SEARCH_COUNT = 20;
    private boolean moreLoading = false;

    private static final SimpleDateFormat SDF_DATA = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat SDF_INTENT = new SimpleDateFormat("yyyy.MM.dd");

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

        lvDiary = (ListView) findViewById(R.id.lv_diary_list);

        lvDiary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                datapopupCurrentData = (DiaryListData) lvDiary.getItemAtPosition(position);
//                Dialog d = new Dialog(DiaryListActivity.this);
//                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View inf = inflater.inflate(R.layout.popupwindow_diary_list, null);
//                d.setContentView(inf);
//                d.show();
//                TextView tt = (TextView) inf.findViewById(R.id.tv_popup_diary_list_edit);
//                tt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        d.hide();
//                    }
//                });

                popup = new PopupWindow(view);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.popupwindow_diary_list, null);
                popup.setContentView(v);
                popup.setTouchable(true);
                popup.setFocusable(true);
                popup.setOutsideTouchable(true);
                popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popup.showAsDropDown(view, 550, 0);
                popup.setAnimationStyle(R.style.AlertDialogCustom);
                popup.showAtLocation(findViewById(R.id.ll_diary_list), Gravity.CENTER, 0, 0);

                TextView edit = (TextView) v.findViewById(R.id.tv_popup_diary_list_edit);
                TextView remove = (TextView) v.findViewById(R.id.tv_popup_diary_list_delete);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datapopupCurrentData == null) {
                            return;
                        }

                        Intent intent = new Intent(DiaryListActivity.this, DiaryWriteActivity.class);
                        intent.putExtra("isNew", false);
                        intent.putExtra("content", datapopupCurrentData.getContent());
                        intent.putExtra("date", SDF_INTENT.format(datapopupCurrentData.getRawDate()));
                        intent.putExtra("no", datapopupCurrentData.getNo());

                        popup.dismiss();
                        startActivity(intent);
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datapopupCurrentData == null) {
                            return;
                        }

//                        HttpConn diaryEditApi = new HttpConn();
//                        diaryEditApi.setContext(global);
//
//                        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
//                        String serviceId = "MPMS_02003";
//                        String contentType = "application/json";
//
//                        Map params = new HashMap<>();
//                        params.put("USER_EMAIL", global.get("email"));
//                        params.put("PET_JOURNAL_SN", datapopupCurrentData.getNo());
//                        HttpResultVO httpResultVO = null;
//
//                        try {
//                            httpResultVO = diaryEditApi.execute(contentType, url, serviceId, params, global.getToken()).get();
////                            startActivity(intent);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }

                        Toast.makeText(DiaryListActivity.this, "click", Toast.LENGTH_SHORT).show();
                        popup.dismiss();
                    }
                });

                return false;
            }
        });

        lvDiary.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    moreItems();

                }
            }
        });

        ibBack = (ImageButton) findViewById(R.id.ib_diary_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        adapter = new DiaryListViewAdapter(getApplicationContext());
        lv.setAdapter(adapter);
//        moreItems();
//        HttpConn diaryListApi = new HttpConn();
//        diaryListApi.setContext(global);
//
//        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
//        String serviceId = "MPMS_02001";
//        String contentType = "application/json";
//
//        Map params = new HashMap<>();
//        params.put("USER_EMAIL", global.get("email"));
//        params.put("SEARCH_COUNT", SEARCH_COUNT);
//        params.put("SEARCH_PAGE", currentPosition++);
//        HttpResultVO httpResultVO = null;
//
//        try {
//            httpResultVO = diaryListApi.execute(contentType, url, serviceId, params, global.getToken()).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        parseAndShow(httpResultVO);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        currentPosition = 1;
        moreItems();
    }

    private void parseAndShow(HttpResultVO httpResultVO) {
        if (httpResultVO != null && httpResultVO.getData() != null && httpResultVO.getData().size() > 0) {
            List<DiaryListData> parseData = new ArrayList<>();
            List<Object> data = httpResultVO.getData();

            for (int i = 0; i < data.size(); i++) {
                DiaryListData dataObj = new DiaryListData();
                try {
                    String registerTime = MapUtils.getString((Map) data.get(i), "FRST_REGIST_DTIME");
                    String contents = MapUtils.getString((Map) data.get(i), "PET_JOURNAL_CN");
                    Integer no = MapUtils.getInteger((Map) data.get(i), "PET_JOURNAL_SN");
                    dataObj.setContent(URLDecoder.decode(contents, "UTF-8"));
                    dataObj.setYearAndDate(SDF_DATA.parse(registerTime));
                    dataObj.setNo(no);
                    parseData.add(dataObj);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(parseData, new Comp());
            Collections.reverse(parseData);

            for (DiaryListData listData : parseData) {
                adapter.addItem(listData.getRawDate(), listData.getContent(), listData.getNo());
            }

            reCalcNewYear();
            adapter.notifyDataSetChanged();

            Log.d("tag", "tag");
        }
    }

    private void moreItems() {
        if (moreLoading == false) {
            moreLoading = true;
        } else {
            return;
        }

        DiaryMoreApi diaryMoreApi = new DiaryMoreApi();
        diaryMoreApi.setContext(global);

        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_02001";
        String contentType = "application/json";

        Map params = new HashMap<>();
        params.put("USER_EMAIL", global.get("email"));
        params.put("SEARCH_COUNT", SEARCH_COUNT);
        params.put("SEARCH_PAGE", currentPosition++);

        diaryMoreApi.execute(contentType, url, serviceId, params, global.getToken());
    }

    public class DiaryMoreApi extends HttpConn {

        @Override
        protected void onPostExecute(HttpResultVO result) {
            super.onPostExecute(result);
            parseAndShow(result);
            moreLoading = false;
        }
    }

    private void reCalcNewYear() {
        List<DiaryListData> list = new ArrayList<>();

        for (int i = 0; i < adapter.getCount(); i++) {
            list.add((DiaryListData) lvDiary.getAdapter().getItem(i));
        }

        for (int i = 0; i < list.size(); i++) {
            DiaryListData listData = list.get(i);
            boolean isViewYear = false;

            if (i + 1 == list.size()) {
                isViewYear = true;
            } else if (i + 1 < list.size()) {
                int year = Integer.parseInt(listData.getYear());
                int afterYear = Integer.parseInt(list.get(i + 1).getYear());

                if (year < afterYear) {
                    isViewYear = true;
                }
            }

            adapter.markYearAtPosition(isViewYear, i);
        }
    }

    class Comp implements Comparator<DiaryListData> {
        public int compare(DiaryListData d1, DiaryListData d2) {
            return d1.getRawDate().compareTo(d2.getRawDate());
        }
    }
}

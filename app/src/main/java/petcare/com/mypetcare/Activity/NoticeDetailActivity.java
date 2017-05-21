package petcare.com.mypetcare.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Model.NoticeDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.VolleySingleton;

public class NoticeDetailActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private NetworkImageView IvImage;
    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvContent;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_notice_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_notice_detail, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        String id = getIntent().getStringExtra("id");

        imageLoader = VolleySingleton.getInstance(NoticeDetailActivity.this).getImageLoader();
        ibBack = (ImageButton) findViewById(R.id.ib_notice_detail_back);
        IvImage = (NetworkImageView) findViewById(R.id.iv_notice_detail_image);
        tvDate = (TextView) findViewById(R.id.tv_notice_detail_date);
        tvTitle = (TextView) findViewById(R.id.tv_notice_detail_name);
        tvContent = (TextView) findViewById(R.id.tv_notice_detail_content);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NoticeDetailApi noticeDetailApi = new NoticeDetailApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_15002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("NOTICE_ID", id);
        noticeDetailApi.execute(header, body);
    }

    public class NoticeDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            NoticeDetailVO noticeListVO = GsonUtil.fromJson(result, NoticeDetailVO.class);
            if (noticeListVO.getResultCode() == -1001) {
                return;
            }

            if (noticeListVO.getResultCode() != 0) {
                Toast.makeText(NoticeDetailActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            NoticeDetailVO.NoticeObject noticeObject = noticeListVO.getData().get(0);

            tvTitle.setText(noticeObject.getTitle());
            tvDate.setText(noticeObject.getCreateDate());
            tvContent.setText(noticeObject.getContent());
//            IvImage.setImageUrl();
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Model.MissingDetailVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class MissingDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_missing_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_missing_detail, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        MissingDetailApi missingDetailApi = new MissingDetailApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("PET_AR_ID", "");

        missingDetailApi.execute(header, body);
    }


    public class MissingDetailApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            MissingDetailVO missingDetailVO = GsonUtil.fromJson(result, MissingDetailVO.class);
            if (missingDetailVO.getResultCode() != 0) {
                Toast.makeText(MissingDetailActivity.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                return ;
            }

//            AlertDialog.Builder alert = new AlertDialog.Builder(MissingDetailActivity.this);
//            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                }
//            });
//
//            alert.setMessage("가입 접수가 완료되었습니다.");
//            alert.setCancelable(false);
//            AlertDialog alertDialog = alert.create();
//            alertDialog.show();
//            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }
}

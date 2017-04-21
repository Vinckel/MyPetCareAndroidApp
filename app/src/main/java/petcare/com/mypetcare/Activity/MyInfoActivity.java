package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.Model.DiaryListVO;
import petcare.com.mypetcare.Model.MyInfoVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.PicUtil;
import petcare.com.mypetcare.Util.VolleySingleton;

public class MyInfoActivity extends AppCompatActivity {
    private ImageView ivAdd;
    private NetworkImageView ivProfile;
    private TextView tvDone;
    private EditText etName;
    private Button btDone;
    private boolean isLoading;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_my_info);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_my_info, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ivAdd = (ImageView) findViewById(R.id.iv_my_info_add);
        ivProfile = (NetworkImageView) findViewById(R.id.iv_my_info_profile);
        tvDone = (TextView) findViewById(R.id.tv_my_info_done);
        btDone = (Button) findViewById(R.id.bt_my_info_done);
        etName = (EditText) findViewById(R.id.et_my_info_name);
        isLoading = false;
        imageLoader = VolleySingleton.getInstance(MyInfoActivity.this).getImageLoader();

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        View.OnClickListener doneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                save();
            }
        };

        tvDone.setOnClickListener(doneListener);
        btDone.setOnClickListener(doneListener);

        MyInfoLoadApi myInfoLoadApi = new MyInfoLoadApi();

        Map headers = new HashMap<>();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01001";
        headers.put("url", url);
        headers.put("serviceName", serviceId);

        Map params = new HashMap<>();

        myInfoLoadApi.execute(headers, params);
    }
    public class MyInfoLoadApi extends GeneralApi {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MyInfoVO myInfoVO = GsonUtil.fromJson(result, MyInfoVO.class);
            if (myInfoVO.getResultCode() != 0) {
                return ;
            }

            MyInfoVO.MyInfoObject data = myInfoVO.getData();
            if (StringUtils.isNotEmpty(data.getUserName())) {
                etName.setText(data.getUserName());
            }

            if (StringUtils.isNotEmpty(data.getUserImgThumbUrl())) {
                ivProfile.setImageUrl(data.getUserImgThumbUrl(), imageLoader);
            }

            if (CollectionUtils.isNotEmpty(data.getData())) {
                List<MyInfoVO.MyInfoObject.MyPetInfoObject> petList = data.getData();
            }
        }
    }

    public class MultipartApi extends GeneralMultipartApi {

        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);
            finish();

//            AlertDialog.Builder alert = new AlertDialog.Builder(MyInfoActivity.this);
//            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                }
//            });
//
//            boolean isAllOk = true;
//            for (String each : resultList) {
//                CommonResult result = GsonUtil.fromJson(each, CommonResult.class);
//                if (result.getResultCode() != 0) {
//                    result.getResultMessage();
//                    isAllOk = false;
//                }
//            }

//            if (isAllOk) {
//                alert.setMessage("등록이 완료되었습니다.");
//            } else {
//                alert.setMessage("등록이 완료되었습니다.\n일부 이미지 등록에 실패했습니다.");
//            }

//            alert.setCancelable(false);
//            AlertDialog alertDialog = alert.create();
//            alertDialog.show();
//            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    private void save() {
        String imagePath = (String) ivProfile.getTag();

        MultipartApi multipartApi = new MultipartApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01002";

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        Map<String, String> files = new HashMap<>();
        if (imagePath != null) {
            files.put("1", imagePath);
        }

        multipartApi.execute(header, body, files);
    }

    private void validate() {
        // 딱히 체크 안 함
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                String pathFromUri = PicUtil.getPathFromUri(getApplicationContext(), data.getData());
                Bitmap selectedImage = PicUtil.getPicture(getApplicationContext(), imageUri);
                ivProfile.setTag(pathFromUri);
                ivProfile.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

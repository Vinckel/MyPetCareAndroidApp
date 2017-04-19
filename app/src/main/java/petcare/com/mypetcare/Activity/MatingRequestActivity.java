package petcare.com.mypetcare.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Adapter.MatingRequestViewpagerAdapter;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.PicUtil;

public class MatingRequestActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private ImageView ivAddPic;
    private ViewPager pager;
    private List<String> paths;
    private List<Bitmap> bitmapPaths;
    private TextView tvPageCount;
    private MatingRequestViewpagerAdapter adapter;
    private RelativeLayout rlPicAreaWithPager;
    private LinearLayout llPicAreaWithAddPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mating_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_mating_request);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_mating_request, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ibBack = (ImageButton) findViewById(R.id.ib_mating_request_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivAddPic = (ImageView) findViewById(R.id.iv_add_pic);
        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        tvPageCount = (TextView) findViewById(R.id.tv_mating_detail_page_count);
        pager = (ViewPager) findViewById(R.id.vp_mating_request);
        paths = new ArrayList<>();
        adapter = new MatingRequestViewpagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvPageCount.setText((position + 1) + "/" + paths.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bitmapPaths = new ArrayList<>();
        llPicAreaWithAddPhoto = (LinearLayout) findViewById(R.id.ll_pic_area);
        rlPicAreaWithPager = (RelativeLayout) findViewById(R.id.rl_pic_area);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();
            List<String> tmpPathList = new ArrayList<>();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    try {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        paths.add(PicUtil.getPathFromUri(getApplicationContext(), uri));
                        bitmapPaths.add(PicUtil.getPicture(getApplicationContext(), uri));
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "파일을 불러오던 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("error", e.getMessage());
                    }
                }

                adapter.addItemAll(bitmapPaths);
                llPicAreaWithAddPhoto.setVisibility(View.GONE);
                rlPicAreaWithPager.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else {
                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageEncoded = cursor.getString(columnIndex);
                cursor.close();
            }
        }
    }
}

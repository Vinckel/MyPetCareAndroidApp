package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Activity.CustomView.RoundedImageView;
import petcare.com.mypetcare.Adapter.NavigationListViewAdapter;
import petcare.com.mypetcare.Model.MyInfoVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.VolleySingleton;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ImageButton ibHospital;
    private ImageButton ibBeauty;
    private ImageButton ibHotel;
    private ImageButton ibShop;
    private ImageButton ibCafe;
    private ImageButton ibFuneral;
    private ImageButton ibAdopt;
    private ImageButton ibReport;
    private ImageButton ibNoti;
    private ConstraintLayout clMyInfoArea;
    private TextView tvMyInfoName;
    private TextView tvMyInfoPetCount;
    private RoundedImageView tvMyInfoProfile;
    private ImageLoader imageLoader;
    private LinearLayout llInsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ibHospital = (ImageButton) findViewById(R.id.btnHospital);
        ibBeauty = (ImageButton) findViewById(R.id.btnBeauty);
        ibHotel = (ImageButton) findViewById(R.id.btnHotel);
        ibShop = (ImageButton) findViewById(R.id.btnTool);
        ibCafe = (ImageButton) findViewById(R.id.btnCafe);
        ibFuneral = (ImageButton) findViewById(R.id.btnFuneral);
        ibAdopt = (ImageButton) findViewById(R.id.btnAdopt);
        ibReport = (ImageButton) findViewById(R.id.btnReport);
        ibNoti = (ImageButton) findViewById(R.id.btnNoti);
        llInsure = (LinearLayout) findViewById(R.id.llInsure);

        tvMyInfoName = (TextView) findViewById(R.id.tv_nav_top_name);
        tvMyInfoPetCount = (TextView) findViewById(R.id.tv_nav_top_pet_count);
        tvMyInfoProfile = (RoundedImageView) findViewById(R.id.iv_nav_top_profile);
        imageLoader = VolleySingleton.getInstance(MainActivity.this).getImageLoader();

        ibHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 0);
                startActivity(intent);
            }
        });

        ibBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 1);
                startActivity(intent);
            }
        });

        ibHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 2);
                startActivity(intent);
            }
        });

        ibShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 3);
                startActivity(intent);
            }
        });

        ibCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 4);
                startActivity(intent);
            }
        });

        ibFuneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 5);
                startActivity(intent);
            }
        });

        ibAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 6);
                startActivity(intent);
            }
        });

        ibReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 7);
                startActivity(intent);
            }
        });

        ibNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, petcare.com.mypetcare.Activity.SearchFragment.MainActivity.class);
                intent.putExtra("startPage", 8);
                startActivity(intent);
            }
        });

        llInsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsureInfoActivity.class);
                startActivity(intent);
            }
        });

        setCustomActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        settingMyInfo();
    }

    private void settingMyInfo() {
        MyInfoLoadApi myInfoLoadApi = new MyInfoLoadApi();

        Map headers = new HashMap<>();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01001";
        headers.put("url", url);
        headers.put("serviceName", serviceId);

        Map params = new HashMap<>();

        myInfoLoadApi.execute(headers, params);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
////        setLogo();
//        return super.onPrepareOptionsMenu(menu);
//    }

    private void setCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbarView = inflater.inflate(R.layout.main_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarView, lp1);

        Toolbar parent = (Toolbar) actionbarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageButton ibHamburger = (ImageButton) findViewById(R.id.ibHeaderHamburger);
        ibHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        final ListView lv = (ListView) findViewById(R.id.nav_lst);
        NavigationListViewAdapter adapter = new NavigationListViewAdapter(this);
        lv.setAdapter(adapter);

        adapter.addItem(R.drawable.ic_insurance_t, R.string.nav_insure, null);
        adapter.addItem(R.drawable.ic_list, R.string.nav_diary, null);
        adapter.addItem(R.drawable.ic_add_info, R.string.nav_info, null);
        adapter.addItem(R.drawable.ic_beauty_shop_t, R.string.nav_my_write, null);
        adapter.addItem(R.drawable.ic_share, R.string.nav_share, null);
        adapter.addItem(R.drawable.ic_info, R.string.nav_inquiry, null);
        adapter.addItem(R.drawable.ic_event_notice, R.string.nav_notice, R.drawable.ic_event_notice);
        adapter.addItem(R.drawable.ic_setting_t, R.string.nav_setting, null);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tv_nav_list_title);
                String selected = tv.getText().toString();

                String receipt = getApplicationContext().getResources().getString(R.string.nav_insure);
                String info = getApplicationContext().getResources().getString(R.string.nav_info);
                String diary = getApplicationContext().getResources().getString(R.string.nav_diary);
                String inquiry = getApplicationContext().getResources().getString(R.string.nav_inquiry);

                drawer.closeDrawer(GravityCompat.START);
                Intent intent = null;

                if (StringUtils.equals(info, selected)) {
                    intent = new Intent(getApplicationContext(), JoinActivity.class);
                } else if (StringUtils.equals(diary, selected)) {
                    intent = new Intent(getApplicationContext(), DiaryListActivity.class);
                } else if (StringUtils.equals(receipt, selected)) {
                    intent = new Intent(getApplicationContext(), ReceiptInsureActivity.class);
                } else if (StringUtils.equals(inquiry, selected)) {
                    intent = new Intent(getApplicationContext(), InquiryActivity.class);
                }

                startActivity(intent);
            }
        });

        clMyInfoArea = (ConstraintLayout) findViewById(R.id.cl_nav_my_info);
        clMyInfoArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MyInfoLoadApi extends GeneralApi {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MyInfoVO myInfoVO = GsonUtil.fromJson(result, MyInfoVO.class);

            if (myInfoVO.getResultCode() != 0) {
                return ;
            }
            MyInfoVO.MyInfoObject info = myInfoVO.getData().get(0);
            String userName = info.getUserName();
            String url = info.getUserImgThumbUrl();
            int count = CollectionUtils.size(info.getData());

            tvMyInfoName.setText(userName);
            tvMyInfoPetCount.setText("마이펫 " + String.valueOf(count));
            imageLoader.get(url, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", "Image Load Error: " + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        tvMyInfoProfile.setImageBitmap(response.getBitmap());
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addinfo) {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Activity.CustomView.RoundedImageView;
import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.Adapter.NavigationListViewAdapter;
import petcare.com.mypetcare.Model.MyInfoVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.VolleySingleton;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String STORE_URL = "https://play.google.com/store/apps/details?id=petcare.com.mypetcare";
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
    private Dialog shareDialog;
    private static GoogleApiClient mGoogleApiClient;

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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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

        JoinPopupListViewAdapter adapterShare = new JoinPopupListViewAdapter(getApplicationContext(), null);
        shareDialog = new Dialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.setContentView(R.layout.popup_share);
        adapterShare.addItem("카카오톡");
        adapterShare.addItem("페이스북");
        adapterShare.addItem("라인");
        adapterShare.addItem("밴드");
        adapterShare.addItem("이메일");

        ListView lvPopup = (ListView) shareDialog.findViewById(R.id.lv_popup_share);
        lvPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String message = "";
                    PackageManager manager = getBaseContext().getPackageManager();
                    Intent i = null;
                    String encodedText = null;
                    Uri uri = null;

                    switch (position) {
                        case 0: // kakaotalk
                            i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.setPackage("com.kakao.talk");
                            i.putExtra(Intent.EXTRA_TEXT, STORE_URL);
                            startActivity(i);
                            break;
                        case 1: // facebook
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setContentUrl(Uri.parse(STORE_URL))
                                    .build();
                            ShareDialog.show(MainActivity.this, content);
                            break;
                        case 2: // line
                            i = manager.getLaunchIntentForPackage("jp.naver.line.android");

                            if (i == null) {
                                i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=jp.naver.line.android"));
                                startActivity(i);
                                break;
                            }

                            encodedText = URLEncoder.encode(STORE_URL, "utf-8");
                            uri = Uri.parse("line://msg/text/" + encodedText);
                            i = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i);
                            break;
                        case 3: // band
                            i = manager.getLaunchIntentForPackage("com.nhn.android.band");

                            if (i == null) {
                                i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.band"));
                                startActivity(i);
                                break;
                            }

                            encodedText = URLEncoder.encode(STORE_URL, "utf-8");
                            uri = Uri.parse("bandapp://create/post?text=" + encodedText);
                            i = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i);
                            break;
                        case 4: // email
                            i = new Intent(Intent.ACTION_SENDTO);
                            i.setType("*/*");
                            i.setData(Uri.parse("mailto:"));
                            i.putExtra(Intent.EXTRA_EMAIL, "");
                            i.putExtra(Intent.EXTRA_SUBJECT, STORE_URL);
                            i.putExtra(Intent.EXTRA_TEXT, STORE_URL);
                            startActivity(i);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "공유에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
                shareDialog.dismiss();
            }
        });
        lvPopup.setAdapter(adapterShare);
    }

    @Override
    public void onResume() {
        super.onResume();
        settingMyInfo();
    }

    private void settingMyInfo() {
        try {
            MyInfoLoadApi myInfoLoadApi = new MyInfoLoadApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_01001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();

            myInfoLoadApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
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
                String share = getApplicationContext().getResources().getString(R.string.nav_share);
                String setting = getApplicationContext().getResources().getString(R.string.nav_setting);

                drawer.closeDrawer(GravityCompat.START);
                Intent intent = null;

                if (StringUtils.equals(info, selected)) {
                    intent = new Intent(getApplicationContext(), JoinActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals(diary, selected)) {
                    intent = new Intent(getApplicationContext(), DiaryListActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals(receipt, selected)) {
                    intent = new Intent(getApplicationContext(), ReceiptInsureActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals(inquiry, selected)) {
                    intent = new Intent(getApplicationContext(), InquiryActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals(setting, selected)) {
                    intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals(share, selected)) {
                    shareDialog.show();
                }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            SharedPreferences pref = getSharedPreferences("local_auth", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            double lastLatitude = mLastLocation.getLatitude();
            double lastLongitude = mLastLocation.getLongitude();
            edit.putString("lastLatitude", String.valueOf(lastLatitude));
            edit.putString("lastLongitude", String.valueOf(lastLongitude));
            edit.commit();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public class MyInfoLoadApi extends GeneralApi {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MyInfoVO myInfoVO = GsonUtil.fromJson(result, MyInfoVO.class);

            if (myInfoVO.getResultCode() != 0) {
                return;
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

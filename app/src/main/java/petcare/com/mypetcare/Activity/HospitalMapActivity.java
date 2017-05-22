package petcare.com.mypetcare.Activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.Model.MyInfoVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

public class HospitalMapActivity extends BaseActivity
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private TextView tvTitle;
    private static double latitude = -1.0;
    private static double longitude = -1.0;
    private static String name = null;
    private static MapPoint customMarkerPoint = null;
    private ImageButton ibBack;
    private ImageButton btDot;
    private Dialog shareDialog;
    private static String locationDetailName = null;
    private RelativeLayout rlDim;
    private ProgressBar pbMap;
    private ListView lvPopup;

    @Override
    protected void onStart() {
        super.onStart();
        locationDetailName = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_map);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.hospital_map_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        latitude = intent.getDoubleExtra("latitude", -1);
        longitude = intent.getDoubleExtra("longitude", -1);

        if (latitude < 0 || longitude < 0) {
            finish();
            return ;
        }

        tvTitle = (TextView) findViewById(R.id.tv_hospital_map_title);
        ibBack = (ImageButton) findViewById(R.id.ib_hospital_map_back);
        btDot = (ImageButton) findViewById(R.id.bt_hospital_map_dot);
        rlDim = (RelativeLayout) findViewById(R.id.rl_map_dim);
        pbMap = (ProgressBar) findViewById(R.id.pb_map);

        btDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isBlank(locationDetailName)) {
                    DaumDetailLocationApi api = new DaumDetailLocationApi();
                    api.execute();
                    onLoadingStart();
                    return;
                }

                JoinPopupListViewAdapter adapterShare = new JoinPopupListViewAdapter(getApplicationContext(), null);
                shareDialog = new Dialog(HospitalMapActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.setContentView(R.layout.popup_map);
                adapterShare.addItem("주소 복사");
                adapterShare.addItem("네이버 지도로 보기");
                adapterShare.addItem("카카오 네비로 보기");
//                adapterShare.addItem("T MAP으로 보기");
                adapterShare.addItem("카카오톡 공유");
                adapterShare.addItem("라인 공유");
                adapterShare.addItem("메세지 공유");

                lvPopup = (ListView) shareDialog.findViewById(R.id.lv_popup_map);
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
                                case 0: // 주소 클립보드 복사
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("location", locationDetailName);
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(HospitalMapActivity.this, "주소가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1: // 네이버지도
                                    String url = "https://m.map.naver.com/search2/search.nhn?query=" + URLEncoder.encode(locationDetailName, "utf-8");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + "#/map"));
                                    startActivity(intent);
                                    break;
                                case 2:
                                    Location destination = Location.newBuilder(name, longitude, latitude).build();
                                    KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination)
                                            .setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build());

                                    KakaoNaviService.shareDestination(HospitalMapActivity.this, builder.build());
                                    break;
                                case 3: // 카카오톡
                                    i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.setPackage("com.kakao.talk");
                                    i.putExtra(Intent.EXTRA_TEXT, locationDetailName);
                                    startActivity(i);
                                    break;
                                case 4: // 라인
                                    i = manager.getLaunchIntentForPackage("jp.naver.line.android");

                                    if (i == null) {
                                        i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=jp.naver.line.android"));
                                        startActivity(i);
                                        break;
                                    }

                                    encodedText = URLEncoder.encode(locationDetailName, "utf-8");
                                    uri = Uri.parse("line://msg/text/" + encodedText);
                                    i = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(i);
                                    break;
                                case 5: // 메세지
                                    i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("smsto:"));
                                    i.putExtra("sms_body", locationDetailName);
                                    startActivity(i);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HospitalMapActivity.this, "실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                        shareDialog.dismiss();
                    }
                });
                lvPopup.setAdapter(adapterShare);

                shareDialog.show();
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText(name);

        MapView mapView = new MapView(HospitalMapActivity.this);
        mapView.setDaumMapApiKey(getResources().getString(R.string.daum_map_key));
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setMapType(MapView.MapType.Standard);

//        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
//        createCustomMarker(mapView);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.rl_map_view);
        mapViewContainer.addView(mapView);
    }

    public void onLoadingStart() {
        rlDim.setVisibility(View.VISIBLE);
        pbMap.setVisibility(View.VISIBLE);
    }

    public void onLoadingEnd() {
        rlDim.setVisibility(View.GONE);
        pbMap.setVisibility(View.GONE);
    }

    public class DaumDetailLocationApi extends AsyncTask<Void, Void, String> {
        private OkHttpClient client;
        private final String DAUM_API = "https://apis.daum.net/local/geo/coord2detailaddr?inputCoordSystem=WGS84&output=json";

        @Override
        protected String doInBackground(Void... params) {
            client = new OkHttpClient();
            String url = DAUM_API + "&apikey=" + getResources().getString(R.string.daum_map_key) + "&x=" + longitude + "&y=" + latitude;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Log.d("header", request.headers().toString());

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                Log.d("response", result);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (StringUtils.isBlank(result)) {
                Toast.makeText(HospitalMapActivity.this, "실패했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map map = GsonUtil.fromJson(result, Map.class);
            Map<String, String> old = (Map<String, String>) MapUtils.getMap(map, "old");
            locationDetailName = MapUtils.getString(old, "name");

            onLoadingEnd();
            btDot.performClick();
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        customMarkerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 2, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(customMarkerPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        mapView.setMapCenterPoint(customMarkerPoint, false);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
        Log.i("df",	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        String url = "daummaps://route?sp="+ latitude +"," + longitude + "&ep=37.4979502,127.0276368&by=PUBLICTRANSIT";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        String url = "daummaps://route?sp="+ latitude +"," + longitude + "&ep=37.4979502,127.0276368&by=PUBLICTRANSIT";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        String url = "daummaps://route?sp="+ latitude +"," + longitude + "&ep=37.4979502,127.0276368&by=PUBLICTRANSIT";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}

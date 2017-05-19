package petcare.com.mypetcare.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.net.URLEncoder;

import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.R;

public class HospitalMapActivity extends BaseActivity
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private TextView tvTitle;
    private static double latitude = -1.0;
    private static double longitude = -1.0;
    private static String name = null;
    private static MapPoint customMarkerPoint = null;
    private ImageButton ibBack;
    private Button btDot;
    private Dialog shareDialog;

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
        btDot = (Button) findViewById(R.id.bt_hospital_map_dot);

        btDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinPopupListViewAdapter adapterShare = new JoinPopupListViewAdapter(getApplicationContext(), 0);
                shareDialog = new Dialog(HospitalMapActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                shareDialog.setContentView(R.layout.popup_map);
                adapterShare.addItem("주소 복사");
                adapterShare.addItem("네이버 지도로 보기");
                adapterShare.addItem("카카오톡 공유");
                adapterShare.addItem("라인 공유");
                adapterShare.addItem("메세지 공유");

                ListView lvPopup = (ListView) shareDialog.findViewById(R.id.lv_popup_map);
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
                                    MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder(getResources().getString(R.string.daum_map_key), MapPoint.mapPointWithGeoCoord(latitude, longitude), new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
                                        @Override
                                        public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                                            Toast.makeText(HospitalMapActivity.this, s, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                                            Toast.makeText(HospitalMapActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }, HospitalMapActivity.this);
                                    reverseGeoCoder.startFindingAddress();
                                    break;
                                case 1: // facebook
//                                    ShareLinkContent content = new ShareLinkContent.Builder()
//                                            .setContentUrl(Uri.parse(STORE_URL))
//                                            .build();
//                                    ShareDialog.show(MainActivity.this, content);
                                    break;
                                case 2: // line
//                                    i = manager.getLaunchIntentForPackage("jp.naver.line.android");
//
//                                    if (i == null) {
//                                        i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=jp.naver.line.android"));
//                                        startActivity(i);
//                                        break;
//                                    }
//
//                                    encodedText = URLEncoder.encode(STORE_URL, "utf-8");
//                                    uri = Uri.parse("line://msg/text/" + encodedText);
//                                    i = new Intent(Intent.ACTION_VIEW, uri);
//                                    startActivity(i);
                                    break;
                                case 3: // band
//                                    i = manager.getLaunchIntentForPackage("com.nhn.android.band");
//
//                                    if (i == null) {
//                                        i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.band"));
//                                        startActivity(i);
//                                        break;
//                                    }
//
//                                    encodedText = URLEncoder.encode(STORE_URL, "utf-8");
//                                    uri = Uri.parse("bandapp://create/post?text=" + encodedText);
//                                    i = new Intent(Intent.ACTION_VIEW, uri);
//                                    startActivity(i);
                                    break;
                                case 4: // email
//                                    i = new Intent(Intent.ACTION_SENDTO);
//                                    i.setType("*/*");
//                                    i.setData(Uri.parse("mailto:"));
//                                    i.putExtra(Intent.EXTRA_EMAIL, "");
//                                    i.putExtra(Intent.EXTRA_SUBJECT, STORE_URL);
//                                    i.putExtra(Intent.EXTRA_TEXT, STORE_URL);
//                                    startActivity(i);
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

//    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
//        private final View mCalloutBalloon;
//
//        public CustomCalloutBalloonAdapter() {
//            mCalloutBalloon = getLayoutInflater().inflate(R.layout.map_callout_balloon, null);
//        }
//
//        @Override
//        public View getCalloutBalloon(MapPOIItem poiItem) {
////            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
////            ((TextView) mCalloutBalloon.findViewById(R.id.tv_mark)).setText("Custom CalloutBalloon");
//            return mCalloutBalloon;
//        }
//
//        @Override
//        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
//            return null;
//        }
//    }
//
//    private void createCustomMarker(MapView mapView) {
//        customMarker = new MapPOIItem();
//        String name = "Custom Marker";
//        customMarker.setItemName(name);
//        customMarker.setTag(1);
//        customMarker.setMapPoint(customMarkerPoint);
//
//        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//
////        customMarker.setCustomImageResourceId(R.drawable.custom_marker_red);
//        customMarker.setCustomImageAutoscale(false);
//        customMarker.setCustomImageAnchor(0.5f, 1.0f);
//
//        mapView.addPOIItem(customMarker);
//        mapView.selectPOIItem(customMarker, true);
//        mapView.setMapCenterPoint(customMarkerPoint, false);
//
//    }

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

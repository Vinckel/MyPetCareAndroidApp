/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package petcare.com.mypetcare.Activity.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Activity.AnnounceActivity;
import petcare.com.mypetcare.Activity.BaseActivity;
import petcare.com.mypetcare.Activity.HospitalDetailActivity;
import petcare.com.mypetcare.Activity.MatingDetailActivity;
import petcare.com.mypetcare.Activity.MatingRequestActivity;
import petcare.com.mypetcare.Activity.MissingDetailActivity;
import petcare.com.mypetcare.Activity.ReportWriteActivity;
import petcare.com.mypetcare.Adapter.AnnounceGridViewAdapter;
import petcare.com.mypetcare.Adapter.HospitalListViewAdapter;
import petcare.com.mypetcare.Adapter.MatingGridViewAdapter;
import petcare.com.mypetcare.Adapter.MissingGridViewAdapter;
import petcare.com.mypetcare.Model.AdoptListVO;
import petcare.com.mypetcare.Model.AnnounceInfoListVO;
import petcare.com.mypetcare.Model.BeautyListVO;
import petcare.com.mypetcare.Model.CafeListVO;
import petcare.com.mypetcare.Model.FuneralListVO;
import petcare.com.mypetcare.Model.GeoRegionVO;
import petcare.com.mypetcare.Model.GeoStateVO;
import petcare.com.mypetcare.Model.HospitalListVO;
import petcare.com.mypetcare.Model.HotelListVO;
import petcare.com.mypetcare.Model.MatingListVO;
import petcare.com.mypetcare.Model.ReportCodeVO;
import petcare.com.mypetcare.Model.ReportListVO;
import petcare.com.mypetcare.Model.ToolListVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GsonUtil;

import static android.content.Context.MODE_PRIVATE;

public class SlidingTabsBasicFragment extends Fragment {
    private static final int SEARCH_COUNT = 20;
    private static ActionBar actionBar;
    private static TextView tvTitle;
    private static ImageView ivWrite;
    private static ImageButton ibBack;
    private static SamplePagerAdapter adapter;
    private static List<GeoStateVO.GeoStateObject> stateList;
    private static List<GeoRegionVO.GeoRegionObject> regionList;
    private static String selectedState = null;
    private static String selectedRegion = null;
    private static final String[] titleArr = {"병원", "미용", "호텔", "용품", "카페", "장례", "분양", "신고", "공고"};
    private static AnnounceGridViewAdapter adapterAnnounce;
    private static MatingGridViewAdapter adapterMating;
    private static MissingGridViewAdapter adapterMissing;
    private static HospitalListViewAdapter[] adapterHospital;
    private static List<Integer> pagingCount;
    private static List<Integer> pagingCountAdopt;
    private static List<Integer> pagingCountReport;
    private static final int NUM_HOSPITAL = 0;
    private static final int NUM_BEAUTY = 1;
    private static final int NUM_HOTEL = 2;
    private static final int NUM_TOOL = 3;
    private static final int NUM_CAFE = 4;
    private static final int NUM_FUNERAL = 5;
    private static final int NUM_ADOPT = 6;
    private static final int NUM_REPORT = 7;
    private static final int NUM_NOTI = 8;
    private static final int NUM_ADOPT_ADOPT = 0;
    private static final int NUM_ADOPT_MATING = 1;
    private static final int NUM_REPORT_MISSING = 0;
    private static final int NUM_REPORT_CENTER = 1;
    private static final int PAGE_OFFSET = 15;
    private static List<Boolean> pagingLastCheck;
    private static List<Boolean> pagingLastCheckAdopt;
    private static List<Boolean> pagingLastCheckReport;
    private static boolean[] isLoaded;
    private static Map<String, String> reportCodeMap;
    private static double lastLongitude = 127.0276;
    private static double lastLatitude = 37.497959;
    private static Spinner[] spHospital;
    private static long[] scrollCooldown;
    private static long[] scrollCooldownAdopt;
    private static long[] scrollCooldownReport;
    private static final long SCROLL_MIN_TERM = 500L;
    private static HotelApi hotelApi;
    private static HospitalApi hospitalApi;
    private static BeautyApi beautyApi;
    private static ToolApi toolApi;
    private static CafeApi cafeApi;
    private static FuneralApi funeralApi;
    private static AdoptApi adoptApi;
    private static MissingListApi missingListApi;
    private static Context context;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static RelativeLayout[] rlZero;
    private static ListView[] lvList;
    private static GridView gvAnnounce;
    private static RelativeLayout rlAnnounceZero;
    private static String currentReportType = "실종";
    private static final int REQUEST_CODE_MATING = 1;
    private static final int REQUEST_CODE_REPORT = 2;

    SharedPreferences pref; // 위치 저장용

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_slide);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);

        actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        View actionBarView = inflater.inflate(R.layout.actionbar_main2, null);
        tvTitle = (TextView) actionBarView.findViewById(R.id.tv_main2_title);
        ibBack = (ImageButton) actionBarView.findViewById(R.id.ib_main2_back);
        ivWrite = (ImageView) actionBarView.findViewById(R.id.iv_main2_write);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);
        context = getContext();

        toolbar.setContentInsetsAbsolute(0, 0);

        pref = getActivity().getSharedPreferences("local_auth", MODE_PRIVATE);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mViewPager.getCurrentItem() == NUM_ADOPT && adapter.getAdoptPageState() != 0) {
                    adapter.setAdoptPageState(0);
                    ivWrite.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    tvTitle.setText(titleArr[NUM_ADOPT]);
                    return true;
                }
                return false;
            }
        });

        reportCodeMap = new HashMap<>();
        pagingLastCheck = new ArrayList<>();
        pagingCount = new ArrayList<>();

        pagingLastCheckAdopt = new ArrayList<>();
        pagingLastCheckAdopt.add(false);
        pagingLastCheckAdopt.add(false);
        pagingCountAdopt = new ArrayList<>();
        pagingCountAdopt.add(1);
        pagingCountAdopt.add(1);

        pagingLastCheckReport = new ArrayList<>();
        pagingLastCheckReport.add(false);
        pagingLastCheckReport.add(false);
        pagingCountReport = new ArrayList<>();
        pagingCountReport.add(1);
        pagingCountReport.add(1);

        for (int i = 0; i < titleArr.length; i++) {
            pagingCount.add(1);
            pagingLastCheck.add(false);
        }

        spHospital = new Spinner[9];
        isLoaded = new boolean[9];
        rlZero = new RelativeLayout[9];
        lvList = new ListView[9];
        for (int i = 0; i < isLoaded.length; i++) {
            isLoaded[i] = false;
        }

        adapterHospital = new HospitalListViewAdapter[7];
        scrollCooldown = new long[11];
        scrollCooldownAdopt = new long[] {0L, 0L};
        scrollCooldownReport = new long[] {0L, 0L};

        for (int i = 0; i < scrollCooldown.length; i++) {
            scrollCooldown[i] = 0L;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lastLatitude = Double.parseDouble(pref.getString("lastLatitude", "37.497959"));
        lastLongitude = Double.parseDouble(pref.getString("lastLongitude", "127.0276"));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set its PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        Bundle bundle = this.getArguments();
        int startPage = bundle.getInt("startPage");
        mViewPager.setCurrentItem(startPage);
        mViewPager.setOffscreenPageLimit(1);

        if (startPage == NUM_REPORT) {
            ivWrite.setVisibility(View.VISIBLE);
            ivWrite.setOnClickListener(null);
            ivWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReportWriteActivity.class);
                    intent.putExtra("arType", reportCodeMap.get(currentReportType));
                    intent.putExtra("arTypeStr", currentReportType);
                    startActivityForResult(intent, REQUEST_CODE_REPORT);
                }
            });
        }

        tvTitle.setText(titleArr[startPage]);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(titleArr[position]);
                if (position == NUM_ADOPT && adapter.getAdoptPageState() == 2) {
                    tvTitle.setText("교배");
                    ivWrite.setVisibility(View.VISIBLE);
                    ivWrite.setOnClickListener(null);
                    ivWrite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MatingRequestActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MATING);
                        }
                    });
                } else if (position == NUM_REPORT) {
                    ivWrite.setVisibility(View.VISIBLE);
                    ivWrite.setOnClickListener(null);
                    ivWrite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ReportWriteActivity.class);
                            intent.putExtra("arType", reportCodeMap.get(currentReportType));
                            intent.putExtra("arTypeStr", currentReportType);
                            startActivityForResult(intent, REQUEST_CODE_REPORT);
                        }
                    });
                } else {
                    ivWrite.setVisibility(View.GONE);
                    ivWrite.setOnClickListener(null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // its PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
//        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.indicatorSelectedFont));
        mSlidingTabLayout.setDividerColors(Color.parseColor("#00000000"));
        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#FFFFFF"));
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {
        private int adoptPageState = 0;
        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 9;
        }

        public int getAdoptPageState() {
            return adoptPageState;
        }

        public void setAdoptPageState(int state) {
            adoptPageState = state;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titleArr[position];
        }

        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = null;

            switch (position) {
                case NUM_REPORT:
                    pagingCount.set(NUM_REPORT, 1);
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_report, container, false);
                    container.addView(view);

                    TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tl_report);
                    tabLayout.addTab(tabLayout.newTab().setText("실종"));
                    tabLayout.addTab(tabLayout.newTab().setText("보호중"));

                    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            switch (tab.getPosition()) {
                                case 0:
                                    Fragment fragment1 = MissingFragment.newInstance(0);
                                    getFragmentManager().beginTransaction().replace(R.id.containerLayout, fragment1).commit();
                                    break;
                                default:
                                    Fragment fragment2 = MissingFragment.newInstance(1);
                                    getFragmentManager().beginTransaction().replace(R.id.containerLayout, fragment2).commit();
                                    break;
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                    Fragment fragment1 = MissingFragment.newInstance(0);
                    getFragmentManager().beginTransaction().replace(R.id.containerLayout, fragment1).commit();

                    break;
                case NUM_ADOPT:
                    switch (adoptPageState) {
                        case 1:
//                            view = adoptProcess(container, NUM_ADOPT_ADOPT);
                            view = call(container, NUM_ADOPT);
                            break;
                        case 2:
                            view = adoptProcess(container, NUM_ADOPT_MATING);
                            break;
                        default:
                            view = getActivity().getLayoutInflater().inflate(R.layout.fragment_adopt, container, false);
                            container.addView(view);
                            ImageView ivAdopt = (ImageView) view.findViewById(R.id.iv_adopt);
                            ivAdopt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    adoptPageState = 1;
                                    tvTitle.setText("업체 분양");
                                    notifyDataSetChanged();
                                }
                            });
                            ImageView ivMating = (ImageView) view.findViewById(R.id.iv_mating);
                            ivMating.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    adoptPageState = 2;
                                    ivWrite.setOnClickListener(null);

                                    ivWrite.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), MatingRequestActivity.class);
                                            startActivityForResult(intent, REQUEST_CODE_MATING);
                                        }
                                    });

                                    tvTitle.setText("교배");
                                    ivWrite.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                }
                            });
                            break;
                    }
                    break;
//                case 3:
//                    break;
                case NUM_NOTI:
                    pagingCount.set(NUM_NOTI, 1);
                    pagingLastCheck.set(NUM_NOTI, false);
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_announce_list, container, false);
                    container.addView(view);

                    rlAnnounceZero = (RelativeLayout) view.findViewById(R.id.rl_announce_zero);
                    gvAnnounce = (GridView) view.findViewById(R.id.gv_announce_list);
                    adapterAnnounce = new AnnounceGridViewAdapter(getContext(), R.layout.gridview_announce_list);
                    gvAnnounce.setAdapter(adapterAnnounce);
                    gvAnnounce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), AnnounceActivity.class);
                            AnnounceInfoListVO.AnnounceInfoObject item = adapterAnnounce.getItem(position);
                            intent.putExtra("data", item);

                            startActivity(intent);
                        }
                    });

                    gvAnnounce.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (firstVisibleItem + visibleItemCount >= totalItemCount && adapterAnnounce.getCount() > 0) {
                                Log.d("gridview", "reached at bottom");
                                callAnnouncePetCallApi();
                            }
                        }

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }
                    });

                    Spinner spState = (Spinner) view.findViewById(R.id.sp_announce_state);
                    final Spinner spRegion = (Spinner) view.findViewById(R.id.sp_announce_region);

                    spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (CollectionUtils.isNotEmpty(stateList)) {
                                adapterAnnounce.removeAllItems();
                                pagingLastCheck.set(NUM_NOTI, false);
                                pagingCount.set(NUM_NOTI, 1);
                                if (position > 0) {
                                    spRegion.setEnabled(true);
                                    GeoStateVO.GeoStateObject stateObject = stateList.get(position - 1);
                                    selectedState = stateObject.getCode();

                                    callRegionApi(spRegion);
                                } else {
                                    spRegion.setAdapter(null);
                                    spRegion.setEnabled(false);
                                    rlAnnounceZero.setVisibility(View.VISIBLE);
                                    gvAnnounce.setVisibility(View.GONE);
                                    adapterAnnounce.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (CollectionUtils.isNotEmpty(regionList)) {
                                adapterAnnounce.removeAllItems();
                                pagingLastCheck.set(NUM_NOTI, false);
                                pagingCount.set(NUM_NOTI, 1);
                                if (position > 0) {
                                    GeoRegionVO.GeoRegionObject regionObject = regionList.get(position - 1);
                                    selectedRegion = regionObject.getRegionCode();

                                    callAnnouncePetCallApi();
                                } else {
                                    rlAnnounceZero.setVisibility(View.VISIBLE);
                                    gvAnnounce.setVisibility(View.GONE);
                                    adapterAnnounce.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    GeoStateApi geoStateApi = new GeoStateApi(spState, spRegion);
                    Map headerGeo = new HashMap<>();
                    String urlGeo = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
                    String serviceIdGeo = "MPMS_12002";
                    headerGeo.put("url", urlGeo);
                    headerGeo.put("serviceName", serviceIdGeo);

                    Map bodyGeo = new HashMap<>();

                    geoStateApi.execute(headerGeo, bodyGeo);
                    break;
                case NUM_HOTEL:
                    view = call(container, NUM_HOTEL);
                    break;
                case NUM_BEAUTY:
                    view = call(container, NUM_BEAUTY);
                    break;
                case NUM_HOSPITAL:
                    view = call(container, NUM_HOSPITAL);
                    break;
                case NUM_TOOL:
                    view = call(container, NUM_TOOL);
                    break;
                case NUM_CAFE:
                    view = call(container, NUM_CAFE);
                    break;
                case NUM_FUNERAL:
                    view = call(container, NUM_FUNERAL);
                    break;
//                    pagingCount.set(NUM_HOSPITAL, 1);
//                    pagingLastCheck.set(NUM_HOSPITAL, false);
//                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_hospital, container, false);
//                    container.addView(view);
//                    ListView lvHospitalList = (ListView) view.findViewById(R.id.lv_hospital_list);
//                    spHospital = (Spinner) view.findViewById(R.id.sp_hospital_distance);
//                    adapterHospital[NUM_HOSPITAL] = new HospitalListViewAdapter(view.getContext());
//                    callHospitalApi(StringUtils.substring(spHospital.getSelectedItem().toString(), 0, 1));
//
//                    lvHospitalList.setAdapter(adapterHospital[NUM_HOSPITAL]);
//                    lvHospitalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(getActivity(), HospitalDetailActivity.class);
//                            intent.putExtra("id", adapterHospital[NUM_HOSPITAL].getItem(position).getId());
//
//                            startActivity(intent);
//                        }
//                    });
//
//                    lvHospitalList.setOnScrollListener(new AbsListView.OnScrollListener() {
//                        @Override
//                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                            if (firstVisibleItem + visibleItemCount >= totalItemCount && isLoaded) {
//                                Log.d("listview hospital", "reached at bottom");
//                                callHospitalApi(StringUtils.substring(spHospital.getSelectedItem().toString(), 0, 1));
//                            }
//                        }
//
//                        @Override
//                        public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//                        }
//                    });
//                case 6:
//                    break;
                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    // Retrieve a TextView from the inflated View, and update its text
                    TextView title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position));
                    break;
            }

//            if (position == 7) {
//                v = getActivity().getLayoutInflater().inflate(R.layout.fragment_hospital, container, false);
//                container.addView(v);
//            } else {
//                v = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
//                        container, false);
//                // Add the newly created View to the ViewPager
//                container.addView(v);
//
//                // Retrieve a TextView from the inflated View, and update its text
//                TextView title = (TextView) v.findViewById(R.id.item_title);
//                title.setText(String.valueOf(position + 1));
//            }

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }

    private View adoptProcess(ViewGroup container, final int num) {
        pagingCountAdopt.set(num, 1);
        pagingLastCheckAdopt.set(num, false);
        isLoaded[NUM_ADOPT] = false;

        View view = null;
        GridView gvAdopt = null;
        switch (num) {
            case NUM_ADOPT_ADOPT:
                break;
            case NUM_ADOPT_MATING:
                view = getActivity().getLayoutInflater().inflate(R.layout.fragment_mating_list, container, false);
                container.addView(view);

                gvAdopt = (GridView) view.findViewById(R.id.gv_mating_list);
                break;
        }

//        adapterAdopt = new AdoptGridViewAdapter(getContext(), R.layout.gridview_adopt_list);
        adapterMating = new MatingGridViewAdapter(getContext(), R.layout.gridview_adopt_list);
        gvAdopt.setAdapter(adapterMating);

        gvAdopt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MatingDetailActivity.class);
                intent.putExtra("id", adapterMating.getItemSaleId(position));

                startActivity(intent);
            }
        });

        gvAdopt.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && adapterMating.getCount() > 0) {
                    Log.d("listview adopt", "reached at bottom");
                    String radius = "1000";

                    if (lastLatitude < 0 || lastLongitude < 0) {
                        Toast.makeText(getContext(), "위치를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    switch (num) {
                        case NUM_ADOPT_ADOPT:
//                            callAdoptApi(radius);
                            break;
                        case NUM_ADOPT_MATING:
                            callMatingApi();
                            break;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        switch (num) {
            case NUM_ADOPT_ADOPT:
//                callAdoptApi("1");
                break;
            case NUM_ADOPT_MATING:
                callMatingApi();
                break;
        }

        return view;
    }

    private void callAdoptApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_ADOPT] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_ADOPT] = currentTime;
        }

        try {
            adoptApi = new AdoptApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_09001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_ADOPT);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_ADOPT, currentPage + 1);

            adoptApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            switch (requestCode) {
                case REQUEST_CODE_MATING:
                    adapter.notifyDataSetChanged();
                    break;
                case REQUEST_CODE_REPORT:
                    if (StringUtils.equals("실종", currentReportType)) {
                        Fragment fragment1 = MissingFragment.newInstance(0);
                        getFragmentManager().beginTransaction().replace(R.id.containerLayout, fragment1).commit();
                    } else {
                        Fragment fragment1 = MissingFragment.newInstance(1);
                        getFragmentManager().beginTransaction().replace(R.id.containerLayout, fragment1).commit();
                    }
                    break;
            }
        }
    }

    private View call(ViewGroup container, final int num) {
//        callCooldown = Calendar.getInstance().getTimeInMillis();
        pagingCount.set(num, 1);
        pagingLastCheck.set(num, false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_hospital, container, false);
        container.addView(view);
        lvList[num] = (ListView) view.findViewById(R.id.lv_hospital_list);
        spHospital[num] = (Spinner) view.findViewById(R.id.sp_hospital_distance);
        rlZero[num] = (RelativeLayout) view.findViewById(R.id.rl_hospital_zero);
        spHospital[num].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String radiusStr = spHospital[num].getItemAtPosition(position).toString();
                String radius = null;

                if (StringUtils.startsWith(radiusStr, "1")) {
                    radius = "1000";
                } else if (StringUtils.startsWith(radiusStr, "2")) {
                    radius = "2000";
                } else if (StringUtils.startsWith(radiusStr, "3")) {
                    radius = "3000";
                } else if (StringUtils.startsWith(radiusStr, "4")) {
                    radius = "4000";
                } else {
                    radius = "20000";
                }

                pagingCount.set(num, 1);
                scrollCooldown[num] = 0L;
                Log.d("scroll cooldown", "" + scrollCooldown[num]);
                adapterHospital[num].clear();

                if (lastLatitude < 0 || lastLongitude < 0) {
                    Toast.makeText(getContext(), "위치를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (num) {
                    case NUM_HOTEL:
                        pagingCount.set(NUM_HOTEL, 1);
                        callHotelApi(radius);
                        break;
                    case NUM_BEAUTY:
                        pagingCount.set(NUM_BEAUTY, 1);
                        callBeautyApi(radius);
                        break;
                    case NUM_HOSPITAL:
                        pagingCount.set(NUM_HOSPITAL, 1);
                        callHospitalApi(radius);
                        break;
                    case NUM_TOOL:
                        pagingCount.set(NUM_TOOL, 1);
                        callToolApi(radius);
                        break;
                    case NUM_CAFE:
                        pagingCount.set(NUM_CAFE, 1);
                        callCafeApi(radius);
                        break;
                    case NUM_FUNERAL:
                        pagingCount.set(NUM_FUNERAL, 1);
                        callFuneralApi(radius);
                        break;
                    case NUM_ADOPT:
                        pagingCount.set(NUM_ADOPT, 1);
                        callAdoptApi(radius);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterHospital[num] = new HospitalListViewAdapter(view.getContext());

        lvList[num].setAdapter(adapterHospital[num]);
        lvList[num].setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HospitalDetailActivity.class);
                intent.putExtra("id", adapterHospital[num].getItem(position).getId());
                intent.putExtra("searchLongitude", adapterHospital[num].getItem(position).getLongitude());
                intent.putExtra("searchLatitude", adapterHospital[num].getItem(position).getLatitude());
                intent.putExtra("radius", adapterHospital[num].getItem(position).getRadius());
                intent.putExtra("num", num);

                startActivity(intent);
            }
        });

        lvList[num].setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && adapterHospital[num].getCount() > 0) {
                    Log.d("listview hospital", "reached at bottom");

                    String radiusStr = spHospital[num].getSelectedItem().toString();
                    String radius = null;

                    if (StringUtils.startsWith(radiusStr, "1")) {
                        radius = "1000";
                    } else if (StringUtils.startsWith(radiusStr, "2")) {
                        radius = "2000";
                    } else if (StringUtils.startsWith(radiusStr, "3")) {
                        radius = "3000";
                    } else if (StringUtils.startsWith(radiusStr, "4")) {
                        radius = "4000";
                    } else {
                        radius = "20000";
                    }

                    if (lastLatitude < 0 || lastLongitude < 0) {
                        Toast.makeText(getContext(), "위치를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    switch (num) {
                        case NUM_HOTEL:
                            callHotelApi(radius);
                            break;
                        case NUM_BEAUTY:
                            callBeautyApi(radius);
                            break;
                        case NUM_HOSPITAL:
                            callHospitalApi(radius);
                            break;
                        case NUM_TOOL:
                            callToolApi(radius);
                            break;
                        case NUM_CAFE:
                            callCafeApi(radius);
                            break;
                        case NUM_FUNERAL:
                            callFuneralApi(radius);
                            break;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });

        return view;
    }

    private void callHotelApi(String radius) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_HOTEL] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_HOTEL] = currentTime;
        }

        try {
            hotelApi = new HotelApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_05001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_HOTEL);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_HOTEL, currentPage + 1);

            hotelApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callCafeApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_CAFE] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_CAFE] = currentTime;
        }

        try {
            cafeApi = new CafeApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_07001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_CAFE);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_CAFE, currentPage + 1);

            cafeApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callFuneralApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_FUNERAL] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_FUNERAL] = currentTime;
        }

        try {
            funeralApi = new FuneralApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_08001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_FUNERAL);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_FUNERAL, currentPage + 1);

            funeralApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callToolApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_TOOL] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_TOOL] = currentTime;
        }

        try {
            toolApi = new ToolApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_06001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_TOOL);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_TOOL, currentPage + 1);

            toolApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callBeautyApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_BEAUTY] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_BEAUTY] = currentTime;
        }

        try {
            beautyApi = new BeautyApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_04001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_BEAUTY);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            pagingCount.set(NUM_BEAUTY, currentPage + 1);

            beautyApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callHospitalApi(String radius) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldown[NUM_HOSPITAL] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldown[NUM_HOSPITAL] = currentTime;
        }

        try {
            hospitalApi = new HospitalApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_03001";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map params = new HashMap<>();
            params.put("SEARCH_COUNT", SEARCH_COUNT);
            int currentPage = pagingCount.get(NUM_HOSPITAL);
            params.put("SEARCH_PAGE", currentPage);
            params.put("SEARCH_LAT", lastLatitude);
            params.put("SEARCH_LON", lastLongitude);
            params.put("SEARCH_RADIUS", radius);
            Log.d("latitude", String.valueOf(lastLatitude));
            Log.d("latitude", String.valueOf(lastLongitude));

            hospitalApi.execute(headers, params);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MissingFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public MissingFragment() {
        }

        public static MissingFragment newInstance(int sectionNumber) {
            MissingFragment fragment = new MissingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;

            switch (num) {
                case 0:
                    rootView = reportProcess(inflater, container, NUM_REPORT_MISSING);
                    if (MapUtils.isEmpty(reportCodeMap)) {
                        ReportCodeApi reportCodeApi = new ReportCodeApi();
                        Map header = new HashMap<>();
                        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
                        String serviceId = "MPMS_00003";
                        header.put("url", url);
                        header.put("serviceName", serviceId);

                        Map body = new HashMap<>();
                        reportCodeApi.execute(header, body);
                    }
                    currentReportType = "실종";
                    break;
                case 1:
                    rootView = reportProcess(inflater, container, NUM_REPORT_CENTER);
//                    callReportListApi("보호중", NUM_REPORT_CENTER);
//                    rootView = inflater.inflate(R.layout.fragment_missing_list, container, false);
//                    GridView gvProtection = (GridView) rootView.findViewById(R.id.gv_missing_list);
//                    adapterProtection = new MissingGridViewAdapter(getContext(), R.layout.gridview_missing_list);
//                    gvProtection.setAdapter(adapterProtection);
                    currentReportType = "보호중";
                    break;
            }
            return rootView;
        }
    }

    private static View reportProcess(LayoutInflater inflater, ViewGroup container, final int num) {
        pagingCountReport.set(num, 1);
        pagingLastCheckReport.set(num, false);

        if (missingListApi != null) {
            missingListApi.cancel(true);
        }

        View view = null;
        GridView gvMissing = null;

        view = inflater.inflate(R.layout.fragment_missing_list, container, false);
        gvMissing = (GridView) view.findViewById(R.id.gv_missing_list);




        adapterMissing = new MissingGridViewAdapter(context, R.layout.gridview_missing_list);
        gvMissing.setAdapter(adapterMissing);

        gvMissing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, MissingDetailActivity.class);
                intent.putExtra("id", adapterMissing.getItem(position).getId());

                context.startActivity(intent);
            }
        });

        if (MapUtils.isNotEmpty(reportCodeMap)) {
            switch (num) {
                case NUM_REPORT_MISSING:
                    callReportListApi(reportCodeMap.get("실종"), num);
                    break;
                case NUM_REPORT_CENTER:
                    callReportListApi(reportCodeMap.get("보호중"), num);
                    break;
            }
        }

        return view;
    }

    private static void callReportListApi(String status, int num) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldownReport[num] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldownReport[num] = currentTime;
        }

        missingListApi = new MissingListApi();
        Map header = new HashMap<>();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_11001";
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map body = new HashMap<>();
        body.put("SEARCH_COUNT", PAGE_OFFSET);
        int currentPage = pagingCountReport.get(num);
        body.put("SEARCH_PAGE", currentPage);
        body.put("PET_AR_TYPE", status);
        pagingCountReport.set(num, currentPage + 1);
        missingListApi.execute(header, body);
    }

    private static class MissingListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                ReportListVO reportListVO = GsonUtil.fromJson(result, ReportListVO.class);
                if (reportListVO.getResultCode() == -1001) {
                    return;
                }

                if (reportListVO.getResultCode() != 0) {
                    return;
                }

                List<ReportListVO.ReportPetObject> dataList = reportListVO.getData();
                for (ReportListVO.ReportPetObject data : dataList) {
                    adapterMissing.addItem(data.getId(), data.getThumbImgUrl(), data.isFound(), data.getLocation());
                }

                adapterMissing.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MatingListApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                MatingListVO matingListVO = GsonUtil.fromJson(result, MatingListVO.class);
                if (matingListVO.getResultCode() == -1001) {
                    return;
                }

                if (matingListVO.getResultCode() != 0) {
                    return;
                }

                List<MatingListVO.MatingObject> dataList = matingListVO.getData();
                for (MatingListVO.MatingObject data : dataList) {
                    adapterMating.addItem(data.getId(), data.getThumbImgUrl(), data.getGender());
                }

                adapterMating.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callMatingApi() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (scrollCooldownAdopt[NUM_ADOPT_MATING] + SCROLL_MIN_TERM > currentTime) {
            return;
        } else {
            scrollCooldownAdopt[NUM_ADOPT_MATING] = currentTime;
        }
        try {
            MatingListApi matingListApi = new MatingListApi();

            Map header = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_10001";
            header.put("url", url);
            header.put("serviceName", serviceId);

            Map body = new HashMap<>();
            body.put("SEARCH_COUNT", PAGE_OFFSET);
            int currentPage = pagingCountAdopt.get(NUM_ADOPT_MATING);
            body.put("SEARCH_PAGE", currentPage);
            pagingCountAdopt.set(NUM_ADOPT_MATING, currentPage + 1);

            matingListApi.execute(header, body);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class GeoStateApi extends GeneralApi {
        Spinner spinnerState;
        Spinner spinnerRegion;

        public GeoStateApi(Spinner spinnerState, Spinner spinnerRegion) {
            this.spinnerState = spinnerState;
            this.spinnerRegion = spinnerRegion;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                GeoStateVO geoStateVO = GsonUtil.fromJson(result, GeoStateVO.class);
                if (geoStateVO.getResultCode() != 0) {
                    return;
                }

                stateList = geoStateVO.getData();
                List<String> arr = new ArrayList<>();
                arr.add("시/도");

                for (GeoStateVO.GeoStateObject geo : stateList) {
                    arr.add(geo.getName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arr) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        return setCentered(super.getView(position, convertView, parent));
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        return setCentered(super.getDropDownView(position, convertView, parent));
                    }

                    private View setCentered(View view) {
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setGravity(Gravity.CENTER);
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerState.setAdapter(spinnerArrayAdapter);
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }

//            if (stateList.size() > 0) {
//                pagingLastCheck.set(NUM_NOTI, false);
//                selectedState = stateList.get(0).getCode();
//                callRegionApi(spinnerRegion);
//            }
        }
    }

    private void callRegionApi(Spinner spinnerRegion) {
        try {
            GeoRegionApi geoRegionApi = new GeoRegionApi(spinnerRegion);
            Map headerGeo = new HashMap<>();
            String urlGeo = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceIdGeo = "MPMS_12003";
            headerGeo.put("url", urlGeo);
            headerGeo.put("serviceName", serviceIdGeo);

            Map bodyGeo = new HashMap<>();
            bodyGeo.put("UPR_CD", selectedState);

            geoRegionApi.execute(headerGeo, bodyGeo);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "정보를 조회하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private static class ReportCodeApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                ReportCodeVO reportCodeVO = GsonUtil.fromJson(result, ReportCodeVO.class);
                if (reportCodeVO.getResultCode() != 0) {
//                    Toast.makeText(, "신고 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ReportCodeVO.ReportCodeObject> data = reportCodeVO.getData();

                if (CollectionUtils.isEmpty(data) || data.size() < 2) {
    //                Toast.makeText(getActivity(), "신고 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                reportCodeMap.put("실종", data.get(0).getCode());
                reportCodeMap.put("보호중", data.get(1).getCode());

                callReportListApi(reportCodeMap.get("실종"), NUM_REPORT_MISSING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ToolApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                ToolListVO toolListVO = GsonUtil.fromJson(result, ToolListVO.class);
                if (showListEmpty(toolListVO.getResultCode(), NUM_TOOL)) {
                    return;
                }

                if (toolListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_TOOL, pagingCount.get(NUM_TOOL) + 1);

                List<ToolListVO.ToolObject> data = toolListVO.getData();

                for (ToolListVO.ToolObject toolObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(toolObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(toolObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_TOOL].addItem(toolObject.getName(), "", distanceStr, toolObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, toolObject.getId(), lastLongitude, lastLatitude, toolObject.getRadius());
                }

                adapterHospital[NUM_TOOL].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class FuneralApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                FuneralListVO funeralListVO = GsonUtil.fromJson(result, FuneralListVO.class);
                if (showListEmpty(funeralListVO.getResultCode(), NUM_FUNERAL)) {
                    return;
                }

                if (funeralListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_FUNERAL, pagingCount.get(NUM_FUNERAL) + 1);

                List<FuneralListVO.FuneralObject> data = funeralListVO.getData();

                for (FuneralListVO.FuneralObject funeralObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(funeralObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(funeralObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_FUNERAL].addItem(funeralObject.getName(), "", distanceStr, funeralObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, funeralObject.getId(), lastLongitude, lastLatitude, funeralObject.getRadius());
                }

                adapterHospital[NUM_FUNERAL].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AdoptApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                AdoptListVO adoptListVO = GsonUtil.fromJson(result, AdoptListVO.class);
                if (showListEmpty(adoptListVO.getResultCode(), NUM_ADOPT)) {
                    return;
                }

                if (adoptListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_ADOPT, pagingCount.get(NUM_ADOPT) + 1);

                List<AdoptListVO.AdoptObject> data = adoptListVO.getData();

                for (AdoptListVO.AdoptObject adoptObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(adoptObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(adoptObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

//                    adapterHospital[NUM_ADOPT].addItem(adoptObject.getId(), adoptObject.getImgUrl(), lastLongitude, lastLatitude, adoptObject.getRadius());
                    adapterHospital[NUM_ADOPT].addItem(adoptObject.getName(), "", distanceStr, adoptObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, adoptObject.getId(), lastLongitude, lastLatitude, adoptObject.getRadius());
                }

                adapterHospital[NUM_ADOPT].notifyDataSetChanged();

                if (adapterHospital[NUM_ADOPT].getCount() < 1) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CafeApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                CafeListVO cafeListVO = GsonUtil.fromJson(result, CafeListVO.class);
                if (showListEmpty(cafeListVO.getResultCode(), NUM_CAFE)) {
                    return;
                }

                if (cafeListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_CAFE, pagingCount.get(NUM_CAFE) + 1);

                List<CafeListVO.CafeObject> data = cafeListVO.getData();

                for (CafeListVO.CafeObject cafeObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(cafeObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(cafeObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_CAFE].addItem(cafeObject.getName(), "", distanceStr, cafeObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, cafeObject.getId(), lastLongitude, lastLatitude, cafeObject.getRadius());
                }

                adapterHospital[NUM_CAFE].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class HotelApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                HotelListVO hotelListVO = GsonUtil.fromJson(result, HotelListVO.class);
                if (showListEmpty(hotelListVO.getResultCode(), NUM_HOTEL)) {
                    return;
                }

                if (hotelListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_HOTEL, pagingCount.get(NUM_HOTEL) + 1);

                List<HotelListVO.HotelObject> data = hotelListVO.getData();

                for (HotelListVO.HotelObject hotelObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(hotelObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(hotelObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_HOTEL].addItem(hotelObject.getName(), "", distanceStr, hotelObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, hotelObject.getId(), lastLongitude, lastLatitude, hotelObject.getRadius());
                }

                adapterHospital[NUM_HOTEL].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean showListEmpty(int resultCode, int num) {
        if (resultCode == -1001) {
            if (rlZero[num] != null && adapterHospital[num].getCount() < 1) {
                rlZero[num].setVisibility(View.VISIBLE);
                lvList[num].setVisibility(View.GONE);
            }

            return true;
        } else {
            if (rlZero[num] != null) {
                rlZero[num].setVisibility(View.GONE);
                lvList[num].setVisibility(View.VISIBLE);
            }
        }

        return false;
    }

    private class BeautyApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                BeautyListVO beautyListVO = GsonUtil.fromJson(result, BeautyListVO.class);
                if (showListEmpty(beautyListVO.getResultCode(), NUM_BEAUTY)) {
                    return;
                }

                if (beautyListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_BEAUTY, pagingCount.get(NUM_BEAUTY) + 1);

                List<BeautyListVO.BeautyObject> data = beautyListVO.getData();

                for (BeautyListVO.BeautyObject beautyObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(beautyObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(beautyObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_BEAUTY].addItem(beautyObject.getName(), "", distanceStr, beautyObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, beautyObject.getId(), lastLongitude, lastLatitude, beautyObject.getRadius());
                }

                adapterHospital[NUM_BEAUTY].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class HospitalApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                HospitalListVO hospitalListVO = GsonUtil.fromJson(result, HospitalListVO.class);
                if (showListEmpty(hospitalListVO.getResultCode(), NUM_HOSPITAL)) {
                    return;
                }

                if (hospitalListVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                pagingCount.set(NUM_HOSPITAL, pagingCount.get(NUM_HOSPITAL) + 1);

                List<HospitalListVO.HospitalObject> data = hospitalListVO.getData();

                for (HospitalListVO.HospitalObject hospitalObject : data) {
                    String distanceStr = "";
                    if (StringUtils.isNotBlank(hospitalObject.getDistance())) {
                        double distance = Math.round(Double.parseDouble(hospitalObject.getDistance()) / 100f) / 10f;
                        distanceStr = String.format("%.1f", distance) + "km";
                    }

                    adapterHospital[NUM_HOSPITAL].addItem(hospitalObject.getName(), "", distanceStr, hospitalObject.getImgUrl(), null /*Arrays.asList(new String[]{"d", "b"})*/, hospitalObject.getId(), lastLongitude, lastLatitude, hospitalObject.getRadius());
                }
                adapterHospital[NUM_HOSPITAL].notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GeoRegionApi extends GeneralApi {
        Spinner spinner;

        public GeoRegionApi(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                GeoRegionVO geoRegionVO = GsonUtil.fromJson(result, GeoRegionVO.class);
                if (geoRegionVO.getResultCode() != 0) {
                    Toast.makeText(getActivity(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    spinner.setAdapter(null);
                    return;
                }

                regionList = geoRegionVO.getData();
                List<String> arr = new ArrayList<>();
                arr.add("시/구/군");

                for (GeoRegionVO.GeoRegionObject geo : regionList) {
                    arr.add(geo.getRegionName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_spinner_item, arr)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        return setCentered(super.getView(position, convertView, parent));
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent)
                    {
                        return setCentered(super.getDropDownView(position, convertView, parent));
                    }

                    private View setCentered(View view)
                    {
                        TextView textView = (TextView)view.findViewById(android.R.id.text1);
                        textView.setGravity(Gravity.CENTER);
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(null);
                spinner.setAdapter(spinnerArrayAdapter);

                pagingLastCheck.set(NUM_NOTI, false);
                if (regionList.size() > 0) {
                    selectedRegion = regionList.get(0).getRegionCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callAnnouncePetCallApi() {
        if (pagingLastCheck.get(NUM_NOTI)) {
            return ;
        }

        AnnouncePetCallApi announcePetCallApi = new AnnouncePetCallApi();
        Map headerGeo = new HashMap<>();
        String urlGeo = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceIdGeo = "MPMS_12001";
        headerGeo.put("url", urlGeo);
        headerGeo.put("serviceName", serviceIdGeo);

        Map bodyGeo = new HashMap<>();
        bodyGeo.put("UPR_CD", selectedState);
        bodyGeo.put("ORG_CD", selectedRegion);

        bodyGeo.put("SEARCH_COUNT", PAGE_OFFSET);
        int currentPage = pagingCount.get(NUM_NOTI);
        bodyGeo.put("SEARCH_PAGE", currentPage);
        pagingCount.set(NUM_NOTI, currentPage + 1);
        Calendar c = Calendar.getInstance();
        String endDateStr = sdf.format(c.getTime());
        c.add(Calendar.MONTH, -3);
        String startDateStr = sdf.format(c.getTime());
        bodyGeo.put("BGN_DE", startDateStr);
        bodyGeo.put("END_DE", endDateStr);

        announcePetCallApi.execute(headerGeo, bodyGeo);
    }

    private class AnnouncePetCallApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (pagingLastCheck.get(NUM_NOTI)) {
                    return ;
                }

                AnnounceInfoListVO announceInfoListVO = GsonUtil.fromJson(result, AnnounceInfoListVO.class);
                if (announceInfoListVO.getResultCode() != 0) {
                    return;
                }

                List<AnnounceInfoListVO.AnnounceInfoObject> dataList = announceInfoListVO.getData();

                if (dataList.size() == 0) {
                    pagingLastCheck.set(NUM_NOTI, true);
                    if (rlAnnounceZero != null && gvAnnounce != null) {
                        rlAnnounceZero.setVisibility(View.VISIBLE);
                        gvAnnounce.setVisibility(View.GONE);
                    }
                    return ;
                }

                for (AnnounceInfoListVO.AnnounceInfoObject data : dataList) {
//                    adapterAnnounce.addItem(data.getThumbUrl());
                    adapterAnnounce.addItem(data);
                }

                if (adapterAnnounce.getCount() > 0) {
                    if (rlAnnounceZero != null && gvAnnounce != null) {
                        rlAnnounceZero.setVisibility(View.GONE);
                        gvAnnounce.setVisibility(View.VISIBLE);
                    }
                }

                adapterAnnounce.notifyDataSetChanged();

                if (dataList.size() < PAGE_OFFSET) {
                    pagingLastCheck.set(NUM_NOTI, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

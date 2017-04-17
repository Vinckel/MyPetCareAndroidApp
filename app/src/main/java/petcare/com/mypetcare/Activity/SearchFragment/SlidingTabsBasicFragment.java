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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Activity.AdoptDetailActivity;
import petcare.com.mypetcare.Activity.BaseActivity;
import petcare.com.mypetcare.Activity.HospitalDetailActivity;
import petcare.com.mypetcare.Activity.MatingDetailActivity;
import petcare.com.mypetcare.Activity.MatingRequestActivity;
import petcare.com.mypetcare.Adapter.AdoptGridViewAdapter;
import petcare.com.mypetcare.Adapter.HospitalListViewAdapter;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;

/**
 * A basic sample which shows how to use {@link com.example.android.common.view.SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment {
    private int hospitalCurrentPosition = 1;
    private static final int SEARCH_COUNT = 20;

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private static ActionBar actionBar;
    private static TextView tvTitle;
    private static ImageView ivWrite;
    private static SamplePagerAdapter adapter;
    private static final String[] titleArr = {"카페", "장례" ,"분양", "신고", "공고", "병원", "미용", "미용", "미용"};

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
        ivWrite = (ImageView) actionBarView.findViewById(R.id.iv_main2_write);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

//        Toolbar parent = (Toolbar) actionBarView.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);





        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mViewPager.getCurrentItem() == 2 && adapter.getAdoptPageState() != 0) {
                    adapter.setAdoptPageState(0);
                    ivWrite.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    tvTitle.setText(titleArr[2]);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
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
        tvTitle.setText(titleArr[startPage]);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(titleArr[position]);
                if (position == 2 && adapter.getAdoptPageState() == 2) {
                    tvTitle.setText("교배");
                    ivWrite.setVisibility(View.VISIBLE);
                    ivWrite.setOnClickListener(null);
                    ivWrite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MatingRequestActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (position == 3) {
                    ivWrite.setVisibility(View.VISIBLE);
                    ivWrite.setOnClickListener(null);
                    ivWrite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

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

        public class HospitalApi extends GeneralApi {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = null;

            switch (position) {
//                case 0:
//                    break;
//                case 1:
//                    break;
                case 2:
                    switch (adoptPageState) {
                        case 1:
                            view = getActivity().getLayoutInflater().inflate(R.layout.fragment_adopt_list, container, false);
                            container.addView(view);

//                            ivWrite.setVisibility(View.VISIBLE);

                            GridView gvAdopt = (GridView) view.findViewById(R.id.gv_adopt_list);
                            AdoptGridViewAdapter adapterAdopt = new AdoptGridViewAdapter(getContext(), R.layout.gridview_adopt_list);
                            gvAdopt.setAdapter(adapterAdopt);
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterAdopt.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            gvAdopt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), AdoptDetailActivity.class);
                                    intent.putExtra("id", id);

                                    startActivity(intent);
                                }
                            });

                            break;
                        case 2:
                            view = getActivity().getLayoutInflater().inflate(R.layout.fragment_mating_list, container, false);
                            container.addView(view);
                            tvTitle.setText("교배");

                            ivWrite.setVisibility(View.VISIBLE);

                            GridView gvMating = (GridView) view.findViewById(R.id.gv_mating_list);
                            AdoptGridViewAdapter adapterMating = new AdoptGridViewAdapter(getContext(), R.layout.gridview_adopt_list);
                            gvMating.setAdapter(adapterMating);
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterMating.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterMating.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterMating.addItem("http://i.imgur.com/SEBjThb.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            adapterMating.addItem("http://i.imgur.com/3jXjgTT.jpg");
                            gvMating.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), MatingDetailActivity.class);
                                    intent.putExtra("id", id);

                                    startActivity(intent);
                                }
                            });

                            break;
                        default:
                            view = getActivity().getLayoutInflater().inflate(R.layout.fragment_adopt, container, false);
                            container.addView(view);
                            ImageView ivAdopt = (ImageView) view.findViewById(R.id.iv_adopt);
                            ivAdopt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    adoptPageState = 1;
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
                                            startActivity(intent);
                                        }
                                    });
                                    notifyDataSetChanged();
                                }
                            });
                            break;
                    }
//                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_adopt, container, false);
//                    container.addView(view);
//                    ImageView ivAdopt = (ImageView) view.findViewById(R.id.iv_adopt);
//                    ivAdopt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            adoptPageState = 1;
//                            notifyDataSetChanged();
//                        }
//                    });
                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
                case 5:
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_hospital, container, false);
                    container.addView(view);
                    ListView lvHospitalList = (ListView) view.findViewById(R.id.lv_hospital_list);
                    HospitalListViewAdapter adapter = new HospitalListViewAdapter(view.getContext());
                    HospitalApi hospitalApi = new HospitalApi();

                    Map headers = new HashMap<>();
                    String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
                    String serviceId = "MPMS_02001";
                    String contentType = "application/json";
                    headers.put("url", url);
                    headers.put("serviceName", serviceId);

                    Map params = new HashMap<>();
//                    params.put("USER_EMAIL", global.get("email"));
                    params.put("SEARCH_COUNT", SEARCH_COUNT);
                    params.put("SEARCH_PAGE", hospitalCurrentPosition++);

//                    hospitalApi.execute(headers, params);

                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    adapter.addItem("test", "testdesc", "testdesc", R.drawable.ic_menu_camera, Arrays.asList(new String[]{"d", "b"}));
                    lvHospitalList.setAdapter(adapter);
                    lvHospitalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), HospitalDetailActivity.class);
                            intent.putExtra("id", id);

                            startActivity(intent);
                        }
                    });
                    break;
//                case 6:
//                    break;
//                case 7:
//                    break;
//                case 8:
//                    break;
                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                            container, false);
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
}

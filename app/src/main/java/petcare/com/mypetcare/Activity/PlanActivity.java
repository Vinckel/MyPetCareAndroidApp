package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import petcare.com.mypetcare.R;

public class PlanActivity extends BaseActivity {
    private ImageButton ibBack;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_plan);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_plan, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.vp_plan);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_plan);
        tabLayout.setupWithViewPager(mViewPager);

        ibBack = (ImageButton) findViewById(R.id.ib_plan_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (num) {
                case 1:
                    SpannableStringBuilder sb = null;
                    rootView = inflater.inflate(R.layout.fragment_plan_1, container, false);
                    TextView tvSimpleSurgery = (TextView) rootView.findViewById(R.id.tv_plan_simple_surgery);
                    TextView tvSimpleHospital = (TextView) rootView.findViewById(R.id.tv_plan_simple_hospital);
                    TextView tvAllSurgery = (TextView) rootView.findViewById(R.id.tv_plan_all_surgery);
                    TextView tvAllHospital = (TextView) rootView.findViewById(R.id.tv_plan_all_hospital);
                    TextView tvAllGo = (TextView) rootView.findViewById(R.id.tv_plan_all_go);

                    String simpleSurgery = "최고 150만원";
                    String simpleHospital = "최고 10만원";
                    String allSurgery = "최고 150만원";
                    String allHospital = "최고 10만원";
                    String allGo = "최고 10만원";
                    sb = new SpannableStringBuilder(simpleSurgery);
                    sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), simpleSurgery.indexOf("1"), simpleSurgery.indexOf("만원"), 0);
                    tvSimpleSurgery.setMovementMethod(LinkMovementMethod.getInstance());
                    tvSimpleSurgery.setText(sb, TextView.BufferType.SPANNABLE);

                    sb = new SpannableStringBuilder(simpleHospital);
                    sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), simpleHospital.indexOf("1"), simpleHospital.indexOf("만원"), 0);
                    tvSimpleHospital.setMovementMethod(LinkMovementMethod.getInstance());
                    tvSimpleHospital.setText(sb, TextView.BufferType.SPANNABLE);

                    sb = new SpannableStringBuilder(allSurgery);
                    sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), allSurgery.indexOf("1"), allSurgery.indexOf("만원"), 0);
                    tvAllSurgery.setMovementMethod(LinkMovementMethod.getInstance());
                    tvAllSurgery.setText(sb, TextView.BufferType.SPANNABLE);

                    sb = new SpannableStringBuilder(allHospital);
                    sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), allHospital.indexOf("1"), allHospital.indexOf("만원"), 0);
                    tvAllHospital.setMovementMethod(LinkMovementMethod.getInstance());
                    tvAllHospital.setText(sb, TextView.BufferType.SPANNABLE);

                    sb = new SpannableStringBuilder(allGo);
                    sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), allGo.indexOf("1"), allGo.indexOf("만원"), 0);
                    tvAllGo.setMovementMethod(LinkMovementMethod.getInstance());
                    tvAllGo.setText(sb, TextView.BufferType.SPANNABLE);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_plan_2, container, false);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "보상한도\n(치료비 한도)";
                case 1:
                    return "보상한도\n(횟수)";
            }
            return null;
        }
    }
}

package petcare.com.mypetcare.Activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Activity.CustomView.RoundedImageView;
import petcare.com.mypetcare.R;

public class InsureInfoActivity extends AppCompatActivity {

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
    private List<ImageView> ivPagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insure_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_insure_info);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.actionbar_insure_info, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ivPagerList = new ArrayList<>();
        ivPagerList.add((ImageView) findViewById(R.id.iv_insure_info_page_1));
        ivPagerList.add((ImageView) findViewById(R.id.iv_insure_info_page_2));
        ivPagerList.add((ImageView) findViewById(R.id.iv_insure_info_page_3));
        ivPagerList.add((ImageView) findViewById(R.id.iv_insure_info_page_4));
        ivPagerList.add((ImageView) findViewById(R.id.iv_insure_info_page_5));
        ivPagerList.get(0).setColorFilter(Color.parseColor("#7579e8"));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.vp_insure_info);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (ImageView iv : ivPagerList) {
                    iv.setColorFilter(Color.parseColor("#edecec"));
                }
                ivPagerList.get(position).setColorFilter(Color.parseColor("#7579e8"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                    rootView = inflater.inflate(R.layout.fragment_insure_info_1, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_insure_info_2, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_insure_info_3, container, false);
                    TextView tv3Answer = (TextView) rootView.findViewById(R.id.tv_insure_info_3_answer);
//                    String answer3 = "반쪽의 질병 및 상해에 대한 병원비\n를 \'실손\' 보상해준답니다.\n\n(자세한 내용은 \'보상하는 손해\'와\n\'보상하지 않는 손해\' 참고해주세요.)";
//                    SpannableString ss3 = new SpannableString(answer3);
//                    ClickableSpan clickableSpan3 = new ClickableSpan() {
//                        @Override
//                        public void onClick(View widget) {
//                            Toast.makeText(getContext(), "test!", Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    ss3.setSpan(clickableSpan3, answer3.indexOf("'보상하는"), answer3.lastIndexOf("'와") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    ss3.setSpan(clickableSpan3, answer3.indexOf("'보상하지"), answer3.lastIndexOf("' ") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv3Answer.setText(ss3);
//                    tv3Answer.setMovementMethod(LinkMovementMethod.getInstance());

                    SpannableStringBuilder sb = new SpannableStringBuilder("반쪽의 질병 및 상해에 대한 병원비\n를 \'실손\' 보상해준답니다.\n\n(자세한 내용은 ");
                    sb.append("'보상하는 손해'");
                    sb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(getContext(), "test!", Toast.LENGTH_SHORT).show();
                        }
                    }, sb.length() - "'보상하는 손해'".length(), sb.length(), 0);
                    sb.append("와\n");
                    sb.append("'보상하지 않는 손해'");
                    sb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {

                        }
                    }, sb.length() - "'보상하지 않는 손해'".length(), sb.length(), 0);
                    sb.append(" 참고해주세요.)");
                    tv3Answer.setMovementMethod(LinkMovementMethod.getInstance());
                    tv3Answer.setText(sb, TextView.BufferType.SPANNABLE);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_insure_info_4, container, false);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_insure_info_5, container, false);
                    TextView tv5Answer = (TextView) rootView.findViewById(R.id.tv_insure_info_5_answer);
                    String answer5 = "1년 단위 보험으로\n5세 소형견은 125,930원,\n5세 중대형견은 233,200원,\n5세 고양이는 129,490원이에요.\n(1년에 1회 납부, 분납 가능)\n\n자세한 내용은 \'보험료 예시\' 화면\n참고해주세요.";
                    SpannableString ss5 = new SpannableString(answer5);
                    ClickableSpan clickableSpan5 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(getContext(), "test!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    ss5.setSpan(clickableSpan5, answer5.indexOf("'"), answer5.lastIndexOf("'") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv5Answer.setText(ss5);
                    tv5Answer.setMovementMethod(LinkMovementMethod.getInstance());

                    break;
            }
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
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
            switch (position) {

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

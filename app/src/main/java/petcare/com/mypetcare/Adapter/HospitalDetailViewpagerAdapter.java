package petcare.com.mypetcare.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-04-02.
 */

public class HospitalDetailViewpagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private List<String> imageUrlList;
    private ImageLoader imageLoader;

    public HospitalDetailViewpagerAdapter(LayoutInflater inflater/*, List<String> imageUrlList*/) {
        this.inflater = inflater;
        this.imageUrlList = new ArrayList<>();
//        this.imageUrlList = imageUrlList;
    }
    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.viewpager_hospital_detail, null);
        container.addView(v);

        if (CollectionUtils.isEmpty(imageUrlList) || StringUtils.isEmpty(imageUrlList.get(position))) {
            return v;
        }

        imageLoader = VolleySingleton.getInstance(container.getContext()).getImageLoader();

        NetworkImageView image = (NetworkImageView) v.findViewById(R.id.iv_hospital_detail_viewpager);

        image.setImageUrl(imageUrlList.get(position), imageLoader);
        return v;
    }

    public void addItem(String url) {
        imageUrlList.add(url);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

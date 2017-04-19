package petcare.com.mypetcare.Adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-04-15.
 */

public class MatingRequestViewpagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private List<Bitmap> imageBitmapList;

    public MatingRequestViewpagerAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        imageBitmapList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imageBitmapList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.viewpager_mating_request, null);
        container.addView(v);

        if (CollectionUtils.isEmpty(imageBitmapList) || imageBitmapList.get(position) == null) {
            return v;
        }

        ImageView image = (ImageView) v.findViewById(R.id.iv_mating_request_viewpager);

        image.setImageBitmap(imageBitmapList.get(position));
        return v;
    }

    public void addItem(Bitmap bitmap) {
        imageBitmapList.add(bitmap);
    }

    public void addItemAll(List<Bitmap> list) {
        imageBitmapList.addAll(list);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

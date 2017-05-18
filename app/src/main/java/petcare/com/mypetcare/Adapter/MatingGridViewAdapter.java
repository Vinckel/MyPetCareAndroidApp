package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Activity.CustomView.RoundedImageView;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-05-16.
 */

public class MatingGridViewAdapter extends BaseAdapter {
    private List<String> imageUrlList;
    private List<String> idList;
    private List<String> genderList;
    private LayoutInflater inf;
    private Context context;
    private int layout;
    private ImageLoader imageLoader;

    public MatingGridViewAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUrlList = new ArrayList<>();
        idList = new ArrayList<>();
        genderList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlList.get(position);
    }

    public String getItemSaleId(int position) {
        return idList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isMaleAtPosition(int position) {
        return StringUtils.equals("남", genderList.get(position));
    }

    public void addItem(String id, String url, String gender) {
        idList.add(id);
        imageUrlList.add(url);
        genderList.add(gender);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.iv_gridview_adopt_list);
        ImageView gender = (ImageView) convertView.findViewById(R.id.iv_gridview_adopt_list_gender);
        image.setImageUrl(imageUrlList.get(position), imageLoader);
        if (isMaleAtPosition(position)) {
            gender.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_male));
        } else {
            gender.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_female));
        }

        return convertView;
    }
}

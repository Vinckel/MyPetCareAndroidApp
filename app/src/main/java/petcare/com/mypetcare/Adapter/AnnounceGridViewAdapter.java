package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-04-13.
 */

public class AnnounceGridViewAdapter extends BaseAdapter {
    private List<String> imageUrlList;
    private LayoutInflater inf;
    private Context context;
    private int layout;
    private ImageLoader imageLoader;

    public AnnounceGridViewAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUrlList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String url) {
        imageUrlList.add(url);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.iv_gridview_announce_list);
        image.setImageUrl(imageUrlList.get(position), imageLoader);

        return convertView;
    }
}

package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Model.MissingData;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-05-01.
 */

public class MissingGridViewAdapter extends BaseAdapter {
    private List<MissingData> dataList;
    private LayoutInflater inf;
    private Context context;
    private int layout;
    private ImageLoader imageLoader;

    public MissingGridViewAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public MissingData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String id, String url, boolean isFound, String distance) {
        MissingData data = new MissingData();
        data.setId(id);
        data.setUrl(url);
        data.setDistance(distance);
        data.setFound(isFound);

        dataList.add(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.iv_gridview_missing);
        TextView distance = (TextView) convertView.findViewById(R.id.tv_gridview_missing_distance);
        if (dataList.get(position).isFound()) {
            distance.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            distance.setText("FOUND");
        } else {
            distance.setText(dataList.get(position).getDistance());
        }

        image.setImageUrl(dataList.get(position).getUrl(), imageLoader);

        return convertView;
    }
}

package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import petcare.com.mypetcare.Model.DiaryListData;
import petcare.com.mypetcare.Model.DiaryListViewHolder;
import petcare.com.mypetcare.Model.HospitalListData;
import petcare.com.mypetcare.Model.HospitalListViewHolder;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-03-23.
 */

public class HospitalListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<HospitalListData> list = new ArrayList<>();

    public HospitalListViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        HospitalListViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_hospital, null);
        HospitalListData data = list.get(position);
//        if (convertView == null) {
//            holder = new HospitalListViewHolder();
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.diary_listview, null);
        TextView name = (TextView) convertView.findViewById(R.id.tv_hospital_name);
        TextView distance = (TextView) convertView.findViewById(R.id.tv_hospital_distance);
        TextView description = (TextView) convertView.findViewById(R.id.tv_hospital_desc);
        ImageView view = (ImageView) convertView.findViewById(R.id.iv_hospital_view);
//            (LinearLayout) convertView.findViewById(R.id.ll_hostpital_tags);

        name.setText(data.getName());
        distance.setText(data.getDist());
        description.setText(data.getDesc());
        view.setImageDrawable(data.getView());

//            convertView.setTag(holder);
//        } else {
//            holder = (HospitalListViewHolder) convertView.getTag();
//        }

        return convertView;
    }

    public void addItem(String name, String desc, String dist, int view, List<String> tags) {
        HospitalListData data = new HospitalListData();

        data.setName(name);
        data.setDesc(desc);
        data.setDist(dist);
        data.setView(context.getResources().getDrawable(view));
        data.setTags(tags);

        list.add(data);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}

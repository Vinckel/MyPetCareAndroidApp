package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import petcare.com.mypetcare.Activity.CustomView.RoundedImageView;
import petcare.com.mypetcare.Model.MyInfoPetListData;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-04-22.
 */

public class MyInfoPetListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<MyInfoPetListData> list = new ArrayList<>();
    private ImageLoader imageLoader;

    public MyInfoPetListViewAdapter(Context context) {
        super();
        this.context = context;
        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MyInfoPetListData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_my_info_pet, null);
        MyInfoPetListData data = list.get(position);
        RoundedImageView image = (RoundedImageView) convertView.findViewById(R.id.iv_lv_my_info_pet_img);
        TextView name = (TextView) convertView.findViewById(R.id.tv_lv_my_info_pet_name);
        TextView birth = (TextView) convertView.findViewById(R.id.tv_lv_my_info_pet_birth);

//        image.setImageUrl(list.get(position).getImageUrl(), imageLoader);
        name.setText(data.getName());
        birth.setText(data.getBirth());

        return convertView;
    }

    public void addItem(String name, String birth, String url, int no) {
        MyInfoPetListData data = new MyInfoPetListData();

        data.setName(name);
        data.setBirth(birth);
        data.setImageUrl(url);
        data.setNo(no);

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

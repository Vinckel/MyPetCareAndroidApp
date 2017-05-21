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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import petcare.com.mypetcare.Model.MyPostListData;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.VolleySingleton;

/**
 * Created by KS on 2017-05-21.
 */

public class MyPostListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<MyPostListData> list = new ArrayList<>();
    private ImageLoader imageLoader;

    public MyPostListViewAdapter(Context context) {
        super();
        this.context = context;
        imageLoader = VolleySingleton.getInstance(this.context).getImageLoader();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MyPostListData getItem(int position) {
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
        convertView = inflater.inflate(R.layout.listview_my_post, null);
        MyPostListData data = list.get(position);

        ImageView typeImage = (ImageView) convertView.findViewById(R.id.iv_my_post_type);
        TextView type = (TextView) convertView.findViewById(R.id.tv_my_post_type);

        TextView typeDetail = (TextView) convertView.findViewById(R.id.tv_my_post_type_detail);
        TextView petName = (TextView) convertView.findViewById(R.id.tv_my_post_name);
        TextView date = (TextView) convertView.findViewById(R.id.tv_my_post_date);
        NetworkImageView petImage = (NetworkImageView) convertView.findViewById(R.id.iv_my_info_pet_img);

        if (StringUtils.equals("AR", data.getTypeName())) {
            typeImage.setImageResource(R.drawable.ic_report_n);
            type.setText("신고");

            if (StringUtils.equals("001", data.getTypeDetailName())) {
                typeDetail.setText("실종");
            } else if (StringUtils.equals("001", data.getTypeDetailName())) {
                typeDetail.setText("보호중");
            }
        } else if (StringUtils.equals("CB", data.getTypeName())) {
            typeImage.setImageResource(R.drawable.ic_pet_n);
            type.setText("분양");
            typeDetail.setText("교배");
        }


        petName.setText(data.getName());
        date.setText(data.getDate());

        petImage.setDefaultImageResId(R.drawable.img_list_empty_list);
        petImage.setErrorImageResId(R.drawable.img_list_empty_list);
        if (StringUtils.isNotBlank(data.getImageUrl())) {
            petImage.setImageUrl(data.getImageUrl(), imageLoader);
        }

        return convertView;
    }

    public void addItem(String name, String createDate, String divide, String type, String thumbImgUrl) {
        MyPostListData data = new MyPostListData();

        data.setName(name);
        data.setTypeName(divide);
        data.setTypeDetailName(type);
        data.setDate(createDate);
        data.setImageUrl(thumbImgUrl);

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

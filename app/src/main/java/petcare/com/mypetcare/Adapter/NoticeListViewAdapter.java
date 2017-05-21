package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import petcare.com.mypetcare.Model.NoticeListData;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-05-21.
 */

public class NoticeListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<NoticeListData> list = new ArrayList<>();

    public NoticeListViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NoticeListData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_notice, null);
        NoticeListData data = list.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.tv_notice_name);
        TextView date = (TextView) convertView.findViewById(R.id.tv_notice_date);

        title.setText(data.getTitle());
        date.setText(data.getDate());

        return convertView;
    }

    public void addItem(String id, String title, String date) {
        NoticeListData data = new NoticeListData();

        data.setTitle(title);
        data.setId(id);
        data.setDate(date);

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

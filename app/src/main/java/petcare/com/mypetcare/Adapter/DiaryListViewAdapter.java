package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import petcare.com.mypetcare.Model.DiaryListData;
import petcare.com.mypetcare.Model.DiaryListViewHolder;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-03-23.
 */

public class DiaryListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<DiaryListData> list = new ArrayList<>();

    public DiaryListViewAdapter(Context context) {
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
        DiaryListViewHolder holder;

        if (convertView == null) {
            holder = new DiaryListViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.diary_listview, null);

            holder.year = (TextView) convertView.findViewById(R.id.tv_diary_list_title_year);
            holder.date = (TextView) convertView.findViewById(R.id.tv_diary_list_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_diary_list_content);

            convertView.setTag(holder);
        } else {
            holder = (DiaryListViewHolder) convertView.getTag();
        }

        DiaryListData data = list.get(position);

        holder.year.setText(data.getYear());
        holder.date.setText(data.getDate());
        holder.content.setText(data.getContent());

        return convertView;
    }

    public void addItem(String date, String content) {
        DiaryListData data = new DiaryListData();
        data.setDate(date);
        data.setContent(content);
        list.add(data);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}

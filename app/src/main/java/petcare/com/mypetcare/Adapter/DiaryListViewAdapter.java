package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import petcare.com.mypetcare.Model.DiaryListData;
import petcare.com.mypetcare.Model.DiaryListViewHolder;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-03-23.
 */

public class DiaryListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<DiaryListData> list = new ArrayList<>();
    int[] images = {R.drawable.ic_hospital_n, R.drawable.ic_beauty_shop_n, R.drawable.ic_vaccination, R.drawable.ic_etc};

    public DiaryListViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DiaryListData getItem(int position) {
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
            holder.category = (ImageView) convertView.findViewById(R.id.iv_diary_list_category);

            convertView.setTag(holder);
        } else {
            holder = (DiaryListViewHolder) convertView.getTag();
        }

        DiaryListData data = list.get(position);

        if (data.getVisibleYear()) {
            holder.year.setVisibility(View.VISIBLE);
        } else {
            holder.year.setVisibility(View.INVISIBLE);
        }

        holder.date.setText(data.getDate());
        holder.content.setText(data.getContent());
        holder.category.setImageResource(images[data.getCategoryNo()]);

        return convertView;
    }

    public void markYearAtPosition(boolean isViewYear, int position) {
        list.get(position).setVisibleYear(isViewYear);
    }

    public void addItem(Date date, String content, Integer no, Integer categoryNo) {
        DiaryListData data = new DiaryListData();

        data.setVisibleYear(false);
        data.setYearAndDate(date);
        data.setContent(content);
        data.setNo(no);
        data.setCategoryNo(categoryNo);

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

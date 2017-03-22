package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import petcare.com.mypetcare.Model.NavigationListData;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-03-23.
 */

public class JoinPopupListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<String> list = new ArrayList<>();
    private Integer highlightPosition;

    public JoinPopupListViewAdapter(Context context, Integer highlightPosition) {
        super();
        this.context = context;
        this.highlightPosition = highlightPosition;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.join_listview_inner, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_join_inner);
        String text = list.get(position);
        tv.setText(text);

        if (highlightPosition != null && highlightPosition == position) {
            tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        return convertView;
    }

    public void addItem(String text) {
        list.add(text);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}

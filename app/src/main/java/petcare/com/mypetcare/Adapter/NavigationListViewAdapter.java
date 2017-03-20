package petcare.com.mypetcare.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import petcare.com.mypetcare.Model.NavigationListData;
import petcare.com.mypetcare.Model.ViewHolder;
import petcare.com.mypetcare.R;

/**
 * Created by KS on 2017-03-20.
 */

public class NavigationListViewAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<NavigationListData> list = new ArrayList<NavigationListData>();

    public NavigationListViewAdapter(Context context) {
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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_listview, null);

            holder.icon = (ImageView) convertView.findViewById(R.id.iv_nav_list_icon);
            holder.text = (TextView) convertView.findViewById(R.id.tv_nav_list_title);
            holder.badge = (ImageView) convertView.findViewById(R.id.iv_nav_list_badge);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NavigationListData data = list.get(position);

        if (data.getIcon() != null) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageDrawable(data.getIcon());
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        if (data.getBadge() != null) {
            holder.badge.setVisibility(View.VISIBLE);
            holder.badge.setImageDrawable(data.getBadge());
        } else {
            holder.badge.setVisibility(View.GONE);
        }

        holder.text.setText(data.getTitle());

        return convertView;
    }

    public void addItem(Integer icon, Integer title, Integer badge) {
        NavigationListData data = new NavigationListData();

        data.setIcon(icon != null ? context.getResources().getDrawable(icon) : null);
        data.setTitle(title != null ? context.getResources().getString(title) : null);
        data.setBadge(badge != null ? context.getResources().getDrawable(badge) : null);

        list.add(data);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}

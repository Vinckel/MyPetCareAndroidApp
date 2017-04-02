package petcare.com.mypetcare.Model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KS on 2017-03-27.
 */

public class HospitalListData {
    public Drawable view;
    public String name;
    public String desc;
    public List<String> tags;
    public String dist;

    public Drawable getView() {
        return view;
    }

    public void setView(Drawable view) {
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}

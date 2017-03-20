package petcare.com.mypetcare.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by KS on 2017-03-20.
 */

public class NavigationListData {
    private Drawable icon;
    private String title;
    private Drawable badge;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getBadge() {
        return badge;
    }

    public void setBadge(Drawable badge) {
        this.badge = badge;
    }
}

package petcare.com.mypetcare.Model;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by KS on 2017-03-27.
 */

public class MyInfoPetListData {
    public String imageUrl;
    public String name;
    public String birth;
    public Integer no;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
}

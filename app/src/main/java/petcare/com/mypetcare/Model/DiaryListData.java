package petcare.com.mypetcare.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KS on 2017-03-27.
 */

public class DiaryListData {
    private static final SimpleDateFormat SDF_VIEW_MD = new SimpleDateFormat("MM.dd");
    private static final SimpleDateFormat SDF_VIEW_Y = new SimpleDateFormat("yyyy");

    private Boolean isVisibleYear;
    private Date rawDate;
    private String year;
    private String date;

    private String content;
    private Integer no;

    public void setYearAndDate(Date date) {
        this.rawDate = date;
        this.year = SDF_VIEW_Y.format(date);
        this.date = SDF_VIEW_MD.format(date);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRawDate() {
        return rawDate;
    }

    public void setRawDate(Date rawDate) {
        this.rawDate = rawDate;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Boolean getVisibleYear() {
        return isVisibleYear;
    }

    public void setVisibleYear(Boolean visibleYear) {
        isVisibleYear = visibleYear;
    }


}

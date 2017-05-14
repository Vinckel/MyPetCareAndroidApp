package petcare.com.mypetcare.Model;

/**
 * Created by KS on 2017-05-01.
 */

public class MissingData {
    private String url;
    private Boolean isFound;
    private String distance;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isFound() {
        return isFound;
    }

    public void setFound(Boolean found) {
        isFound = found;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}

package petcare.com.mypetcare.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KS on 2017-05-03.
 */

public class HospitalDetailVO {
    @SerializedName("RESULT_CODE")
    private Integer resultCode;

    @SerializedName("DATA")
    private List<HospitalObject> data;

    @SerializedName("RESULT_MESSAGE")
    private String resultMessage;

    public class HospitalObject {
        @SerializedName("ID")
        private String locationId;

        @SerializedName("TITLE")
        private String name;

        @SerializedName("LATITUDE")
        private Double latitude;

        @SerializedName("LONGITUDE")
        private Double longitude;

        @SerializedName("CATEGORY")
        private String category;

        @SerializedName("PHONE")
        private String contact;

        @SerializedName("IMAGE_URL")
        private String imgurl;

        @SerializedName("ADDRESS")
        private String address;

        @SerializedName("NEW_ADDRESS")
        private String addressNew;

        @SerializedName("ADDRESS_B_CODE")
        private String addressCode;

        @SerializedName("PLACE_URL")
        private String placeUrl;

        @SerializedName("DISTANCE")
        private String distance;

        @SerializedName("DIRECTION")
        private String direction;

        @SerializedName("SEARCH_RADIUS")
        private String radius;

        @SerializedName("SEARCH_LAT")
        private Double searchLatitude;

        @SerializedName("SEARCH_LON")
        private Double searchLongitude;

        public String getRadius() {
            return radius;
        }

        public String getLocationId() {
            return locationId;
        }

        public String getName() {
            return name;
        }

        public Double getSearchLatitude() {
            return searchLatitude;
        }

        public Double getSearchLongitude() {
            return searchLongitude;
        }

        public String getCategory() {
            return category;
        }

        public String getContact() {
            return contact;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getAddress() {
            return address;
        }

        public String getAddressNew() {
            return addressNew;
        }

        public String getAddressCode() {
            return addressCode;
        }

        public String getPlaceUrl() {
            return placeUrl;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public String getDistance() {
            return distance;
        }

        public String getDirection() {
            return direction;
        }
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public List<HospitalObject> getData() {
        return data;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}

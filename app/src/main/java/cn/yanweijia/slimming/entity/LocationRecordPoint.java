package cn.yanweijia.slimming.entity;


import java.io.Serializable;

/**
 * Created by weijia on 15/10/2017.
 *
 * @author weijia
 */

public class LocationRecordPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * latitude
     */
    private Double lat;
    /**
     * longitude
     */
    private Double lng;
    /**
     * time
     */
    private Long time;

    public LocationRecordPoint() {
    }

    public LocationRecordPoint(Double lat, Double lng, Long time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

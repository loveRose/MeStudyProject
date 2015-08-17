package com.lvyerose.baidutest.beans;

/**
 * author: lvyeRose
 * objective:
 * mailbox: lvyerose@163.com
 * time: 15/7/29 15:39
 */
public class LocationBean {
    private double latitude;
    private double longitude;
    private String info;

    public LocationBean() {
    }

    public LocationBean(double latitude, double longitude, String info) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

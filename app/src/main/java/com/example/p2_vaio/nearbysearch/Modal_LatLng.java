package com.example.p2_vaio.nearbysearch;

/**
 * Created by p2 on 16/5/18.
 */

public class Modal_LatLng {

    private double latitude ;
    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Modal_LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

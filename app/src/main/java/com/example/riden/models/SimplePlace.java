package com.example.riden.models;

public class SimplePlace {
    double lat;
    double lng;

    public SimplePlace(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return lng;
    }

}

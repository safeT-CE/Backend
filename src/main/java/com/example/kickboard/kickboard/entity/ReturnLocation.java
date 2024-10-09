package com.example.kickboard.kickboard.entity;

public class ReturnLocation {
    private double latitude; // 위도
    private double longitude; // 경도
    private double radius; // 반경 (km)

    public ReturnLocation(double latitude, double longitude, double radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }
}
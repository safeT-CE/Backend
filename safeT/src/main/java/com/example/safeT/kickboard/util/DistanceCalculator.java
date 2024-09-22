package com.example.safeT.kickboard.util;

import com.example.safeT.kickboard.entity.Location;

public class DistanceCalculator {

    public static double calculateDistance(Location startLocation, Location endLocation) {
        double startLat = startLocation.getLatitude();
        double startLon = startLocation.getLongitude();
        double endLat = endLocation.getLatitude();
        double endLon = endLocation.getLongitude();

        double startLatRad = Math.toRadians(startLat);
        double startLonRad = Math.toRadians(startLon);
        double endLatRad = Math.toRadians(endLat);
        double endLonRad = Math.toRadians(endLon);

        double dLat = endLatRad - startLatRad;
        double dLon = endLonRad - startLonRad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(startLatRad) * Math.cos(endLatRad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        final double EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터)
        return EARTH_RADIUS * c; // 지구 반지름을 곱해 최종 거리 반환
    }
}

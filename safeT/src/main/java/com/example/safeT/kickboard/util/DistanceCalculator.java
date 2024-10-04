package com.example.kickboard.kickboard.util;

public class DistanceCalculator {

    // Haversine 공식을 사용하여 두 지점 간의 거리 계산
    public static double haversine(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // 지구의 반지름 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 (km)
    }
}

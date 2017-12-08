package com.wuweibi.bullet.utils;

/**
 * Created by marker on 16/8/12.
 */
public class GeoUtils {



    private static final double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }


    /**
     * 获取两点距离
     * @param lat1 纬度A
     * @param lng1 经度A
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}

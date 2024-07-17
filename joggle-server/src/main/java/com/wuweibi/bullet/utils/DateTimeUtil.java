package com.wuweibi.bullet.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtil {


    /**
     * 获取两个时间的小时差
     * @param form 来
     * @param to 到
     * @return
     */
    public static int getHourDiff(LocalDateTime form, LocalDateTime to) {
        // 计算时间差
        Duration duration = Duration.between(form, to);
        // 输出时间差
        long hours = duration.toHours(); // 获取小时数
        return (int) hours;
    }


    /**
     * 获取两个时间的时间查 如1天2小时30分钟
     */
    public static String diffDate(long from, long to) {
        String diff = "";
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long _diff = to - from;
        if (_diff <= 0) {
            diff = "0d";
            return diff;
        }

        // 计算差多少天
        long day = _diff / nd;
        // 计算差多少小时
        long hour = _diff % nd / nh;
        // 计算差多少分钟
        long min = _diff % nd % nh / nm;
        long mis = _diff % nd % nh % nm / ns;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        if (day > 0) {
            diff += day + "d ";
        }
        if (hour > 0) {
            diff += hour + "h ";
        }
        if (min > 0) {
            diff += min + "m";
        }
        if (mis > 0) {
            diff += mis + "s";
        }
        return diff;
    }

}

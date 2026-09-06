package com.wuweibi.bullet.dashboard.domain;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DeviceDateItemHourVO implements Comparator<DeviceDateItemHourVO> {

    @Schema(description = "时间 yyyy-MM-dd HH")
    private String time;

    @Schema(description = "全部流量MB")
    private BigDecimal flow = BigDecimal.ZERO;

    @Schema(description = "入网流量MB")
    private BigDecimal flowIn = BigDecimal.ZERO;

    @Schema(description = "出网流量MB")
    private BigDecimal flowOut = BigDecimal.ZERO;

    @Schema(description = "链接数量")
    private BigDecimal link = BigDecimal.ZERO;

    public int sorted(DeviceDateItemHourVO deviceDateItemVO) {
        long a = DateUtil.parse(time, "yyyy-MM-dd HH").getTime();
        long b = DateUtil.parse(deviceDateItemVO.getTime(), "yyyy-MM-dd HH").getTime();



        return CompareUtil.compare(a,b) ;
    }


    @Override
    public int compare(DeviceDateItemHourVO o1, DeviceDateItemHourVO o2) {

        long a = DateUtil.parse(o1.getTime(), "yyyy-MM-dd HH").getTime();
        long b = DateUtil.parse(o2.getTime(), "yyyy-MM-dd HH").getTime();



        return CompareUtil.compare(a,b) ;
    }
}

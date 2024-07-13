package com.wuweibi.bullet.utils;

import java.math.BigDecimal;

public final class BigDecimalUtils {


    /**
     * 环比工具类
     * @param newVal 新值
     * @param oldVal 旧值
     * @return
     */
    public static BigDecimal getChainRatio(BigDecimal newVal, BigDecimal oldVal){
        if (BigDecimal.ZERO.compareTo(oldVal) == 0) {
            return BigDecimal.valueOf(100.00);
        }
        return newVal.subtract(oldVal)
                .divide(oldVal).multiply(BigDecimal.valueOf(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}

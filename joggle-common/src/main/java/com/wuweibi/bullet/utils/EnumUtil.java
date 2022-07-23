package com.wuweibi.bullet.utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 枚举工具类
 *
 *
 * @author marker
 */
public class EnumUtil {


    /**
     * 枚举转Map
     * 方面转换数据
     * @param enumClass 枚举类
     * @param kMapper map k
     * @param vMapper map v
     * @param <E>
     * @return
     */
    public static <HK,HV,E>   Map<HK, HV> toMap(Class<E> enumClass,
                                                Function<? super E, ? extends HK > kMapper, Function<? super E, HV> vMapper){
        Enum[] var2 = (Enum[]) enumClass.getEnumConstants();
        int var3 = var2.length;
        Map<HK, HV> map = new HashMap<>(var3);

        for(int var4 = 0; var4 < var3; ++var4) {
            E e = (E) var2[var4];
            map.put( kMapper.apply(e), vMapper.apply(e));
        }
        return map;
    }


    /**
     * 枚举转数组
     * 应用场景：在接口参数校验时
     * @param enumClass 枚举类
     * @param kMapper 取数回调
     * @param <V> 值
     * @param <E> 类型
     * @return
     */
    public static  <V, E> V[] toArray(Class<E> enumClass, Function<? super E, ? extends V > kMapper) {
        Enum[] var2 = (Enum[]) enumClass.getEnumConstants();
        int var3 = var2.length;
        V[] array = null;
        for (int var4 = 0; var4 < var3; ++var4) {
            E e = (E) var2[var4];
            V v = kMapper.apply(e);
            if (var4 == 0) {
                array = (V[]) Array.newInstance(v.getClass(), var3);
            }
            array[var4] = v;
        }
        return array;
    }




    public static <HK,E> E getEnumByKey(Class<E> enumClass,
                                        Function<? super E, ? extends HK > kMapper, HK key){
        Enum[] var2 = (Enum[]) enumClass.getEnumConstants();
        int var3 = var2.length;
        for(int var4 = 0; var4 < var3; ++var4) {
            E e = (E) var2[var4];
            if(kMapper.apply(e).equals(key)){
                return e;
            }
        }
        return null;
    }

}

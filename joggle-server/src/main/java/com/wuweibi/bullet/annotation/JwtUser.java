package com.wuweibi.bullet.annotation;
/**
 * Created by marker on 2018/5/28.
 */

import java.lang.annotation.*;

/**
 * JWT获取登录用户注解
 *
 * @author marker
 * @create 2018-05-28 14:06
 **/
@Deprecated
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public  @interface JwtUser {

    String value() default "";


}


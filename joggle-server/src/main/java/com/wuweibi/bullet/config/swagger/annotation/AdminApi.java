package com.wuweibi.bullet.config.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理端
 * @author marker
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.TYPE})// 方法注解
public @interface AdminApi {

}

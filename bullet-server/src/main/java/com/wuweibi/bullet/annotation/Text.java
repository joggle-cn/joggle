package com.wuweibi.bullet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 文本描述注解，此注解主要是用于状态码的注释
 * 以便在需要的时候通过反射机制生成状态码对应的错误提示集合JSON
 * 
 * 【便于开发】
 * 
 * @author marker
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.FIELD})// 方法注解
public @interface Text {

	String value();
 
}
